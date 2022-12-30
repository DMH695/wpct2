package com.tbxx.wpct.service.impl;

import cn.hutool.core.util.RandomUtil;
import com.google.gson.Gson;
import com.tbxx.wpct.entity.OrderInfo;
import com.tbxx.wpct.entity.RefundInfo;
import com.tbxx.wpct.enums.OrderStatus;
import com.tbxx.wpct.enums.wxpay.WxApiType;
import com.tbxx.wpct.enums.wxpay.WxRefundStatus;
import com.tbxx.wpct.enums.wxpay.WxTradeState;
import com.tbxx.wpct.mapper.OrderInfoMapper;
import com.tbxx.wpct.service.WechatPayService;
import com.tbxx.wpct.util.HttpUtils;
import com.tbxx.wpct.config.WxPayConfig;
import com.wechat.pay.contrib.apache.httpclient.exception.HttpCodeException;
import com.wechat.pay.contrib.apache.httpclient.exception.NotFoundException;
import com.wechat.pay.contrib.apache.httpclient.notification.Notification;
import com.wechat.pay.contrib.apache.httpclient.notification.NotificationHandler;
import com.wechat.pay.contrib.apache.httpclient.notification.NotificationRequest;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.util.EntityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.security.GeneralSecurityException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.locks.ReentrantLock;

/**
 * @Author ZXX
 * @ClassName WechatPayServiceImpl
 * @Description
 * @DATE 2022/10/7 10:24
 */

@Slf4j
@Service
public class WechatPayServiceImpl implements WechatPayService {

    @Resource
    WxPayConfig wxPayConfig;


    @Resource(name = "WxPayClient")
    private CloseableHttpClient httpClient;

    @Resource(name = "wxPayNoSignClient")
    private CloseableHttpClient wxPayNoSignClient;


    @Resource
    OrderInfoMapper orderInfoMapper;

    @Autowired
    OrderInfoServiceImpl orderInfoService;

    @Autowired
    PaymentInfoServiceImpl paymentInfoService;

    @Autowired
    RefundInfoServiceImpl refundsInfoService;

    private final ReentrantLock lock = new ReentrantLock();


    /**
     * 押金下单专用
     */
    @Override
    public String depositJsapiPay(OrderInfo orderInfo, String openid) throws Exception {

        log.warn("调用统一下单api(押金)");
        HttpPost httpPost = new HttpPost(wxPayConfig.getDomain().concat("/v3/pay/transactions/jsapi"));

        //请求body参数
        Gson gson = new Gson();
        Map<String, Object> paramsMap = new HashMap<>();

        paramsMap.put("appid", wxPayConfig.getAppid());
        paramsMap.put("mchid", wxPayConfig.getMchId());
        paramsMap.put("description", orderInfo.getTitle());
        paramsMap.put("out_trade_no", orderInfo.getOrderNo());   //test
        paramsMap.put("notify_url", "https://4s3471264h.zicp.fun/weixin/jsapi/notify");  //test

        Map amountMap = new HashMap();
        amountMap.put("total", orderInfo.getTotalFee());
        amountMap.put("currency", "CNY");

        Map payerMap = new HashMap();
        payerMap.put("openid", openid);  //test

        paramsMap.put("amount", amountMap);
        paramsMap.put("payer", payerMap);

        //将参数转换成json字符串
        String jsonParams = gson.toJson(paramsMap);
        log.info("请求参数:{}", jsonParams);

        StringEntity entity = new StringEntity(jsonParams, "utf-8");
        entity.setContentType("application/json");
        httpPost.setEntity(entity);
        httpPost.setHeader("Accept", "application/json");

        //完成签名并执行请求
        CloseableHttpResponse response = httpClient.execute(httpPost);

        try {
            String bodyAsString = EntityUtils.toString(response.getEntity());
            int statusCode = response.getStatusLine().getStatusCode();
            if (statusCode == 200) { //处理成功
                log.info("成功, 返回结果 = " + bodyAsString);
            } else if (statusCode == 204) { //处理成功，无返回Body
                log.info("成功");
            } else {
                log.info("JSAPI下单失败,响应码 = " + statusCode + ",返回结果 = " +
                        bodyAsString);
                throw new IOException("request failed");
            }

            String nonceStr = RandomUtil.randomString(32);// 随机字符串
            String timeStamp = String.valueOf(System.currentTimeMillis() / 1000);// 时间戳

            //响应结果
            Map<String, String> resultMap = gson.fromJson(bodyAsString,
                    HashMap.class);
            String prepayId = resultMap.get("prepay_id");

            /*
            //存入 预支付交易会话标识 防止调用下单接口
            orderInfo.setPrepayId(prepayId);
            QueryWrapper<OrderInfo> queryWrapper = new QueryWrapper<>();
            queryWrapper.eq("id",orderId);
            orderInfoMapper.update(orderInfo,queryWrapper);
             */


            String Sign = wxPayConfig.getSign(wxPayConfig.getAppid(), Long.parseLong(timeStamp), nonceStr, "prepay_id=" + prepayId);

            resultMap.put("timeStamp", timeStamp);
            resultMap.put("nonceStr", nonceStr);
            resultMap.put("appId", wxPayConfig.getAppid());
            resultMap.put("signType", "RSA");
            resultMap.put("paySign", Sign);

            String resultJson = gson.toJson(resultMap);
            log.warn("resultJson是=====>{}", resultJson);

            return resultJson;
        } finally {
            response.close();
        }
    }


    /**
     * @return 预支付交易会话标识 prepay_id string[1,64](押金)
     * 请求URL：https://api.mch.weixin.qq.com/v3/pay/transactions/jsapi
     */
    @Override
    public String jsapiPay(String openid, String orderId) throws Exception {
        log.warn("生成订单");
        OrderInfo orderInfo = orderInfoMapper.selectById(orderId);

        log.warn("调用统一下单api");
        HttpPost httpPost = new HttpPost(wxPayConfig.getDomain().concat("/v3/pay/transactions/jsapi"));

        //请求body参数
        Gson gson = new Gson();
        Map<String, Object> paramsMap = new HashMap<>();

        paramsMap.put("appid", wxPayConfig.getAppid());
        paramsMap.put("mchid", wxPayConfig.getMchId());
        paramsMap.put("description", orderInfo.getTitle());
        paramsMap.put("out_trade_no", orderInfo.getOrderNo());   //test
        paramsMap.put("notify_url", "https://wpctjt.com/weixin/jsapi/notify");  //test

        Map amountMap = new HashMap();
        amountMap.put("total", orderInfo.getTotalFee());
        amountMap.put("currency", "CNY");

        Map payerMap = new HashMap();
        payerMap.put("openid", openid);

        paramsMap.put("amount", amountMap);
        paramsMap.put("payer", payerMap);

        //将参数转换成json字符串
        String jsonParams = gson.toJson(paramsMap);
        log.info("请求参数:{}", jsonParams);

        StringEntity entity = new StringEntity(jsonParams, "utf-8");
        entity.setContentType("application/json");
        httpPost.setEntity(entity);
        httpPost.setHeader("Accept", "application/json");

        //完成签名并执行请求
        CloseableHttpResponse response = httpClient.execute(httpPost);

        try {
            String bodyAsString = EntityUtils.toString(response.getEntity());
            int statusCode = response.getStatusLine().getStatusCode();
            if (statusCode == 200) { //处理成功
                log.info("成功, 返回结果 = " + bodyAsString);
            } else if (statusCode == 204) { //处理成功，无返回Body
                log.info("成功");
            } else {
                log.info("JSAPI下单失败,响应码 = " + statusCode + ",返回结果 = " +
                        bodyAsString);
                throw new IOException("request failed");
            }

            String nonceStr = RandomUtil.randomString(32);// 随机字符串
            String timeStamp = String.valueOf(System.currentTimeMillis() / 1000);// 时间戳

            //响应结果
            Map<String, String> resultMap = gson.fromJson(bodyAsString,
                    HashMap.class);
            String prepayId = resultMap.get("prepay_id");

            /*
            //存入 预支付交易会话标识 防止调用下单接口
            orderInfo.setPrepayId(prepayId);
            QueryWrapper<OrderInfo> queryWrapper = new QueryWrapper<>();
            queryWrapper.eq("id",orderId);
            orderInfoMapper.update(orderInfo,queryWrapper);
             */


            String Sign = wxPayConfig.getSign(wxPayConfig.getAppid(), Long.parseLong(timeStamp), nonceStr, "prepay_id=" + prepayId);

            resultMap.put("timeStamp", timeStamp);
            resultMap.put("nonceStr", nonceStr);
            resultMap.put("appId", wxPayConfig.getAppid());
            resultMap.put("signType", "RSA");
            resultMap.put("paySign", Sign);

            String resultJson = gson.toJson(resultMap);
            log.warn("resultJson是=====>{}", resultJson);

            return resultJson;
        } finally {
            response.close();
        }
    }


    /**
     * jsapi支付结果
     *
     * @return 通知
     */
    @Override
    public String payNotify(HttpServletRequest request, HttpServletResponse response) throws GeneralSecurityException, NotFoundException, IOException, HttpCodeException {
        Gson gson = new Gson();
        Map<String, String> map = new HashMap<>();  //应答对象  Json格式


        try {
            //处理通知参数
            String body = HttpUtils.readData(request);
            String wechatPaySerial = request.getHeader("Wechatpay-Serial");
            String nonce = request.getHeader("Wechatpay-Nonce");
            String timestamp = request.getHeader("Wechatpay-Timestamp");
            String signature = request.getHeader("Wechatpay-Signature");
            HashMap<String, Object> bodyMap = gson.fromJson(body, HashMap.class);

            /**
             * {"id":"c17f5eaf-0a90-5e4c-9ec8-e1f068addfcf",
             * "create_time":"2022-10-04T13:25:40+08:00",
             * "resource_type":"encrypt-resource","event_type":
             * "TRANSACTION.SUCCESS","summary":"支付成功",
             * "resource":{"original_type":"transaction","algorithm":"AEAD_AES_256_GCM","ciphertext":"gTK9I96p3gXvyN6c9tkLrv3ogD/adDzjFJxvLDWpD9cOybuefaxMxdh/6OxW64wdBBR8IWdCq+nqs,
             * "associated_data":"transaction",
             * "nonce":"VBNu9IF6GGnX"}}
             */
            String requestId = (String) bodyMap.get("id");
            log.info("支付通知的id ===> {}", requestId);
            log.info("支付通知的完整数据 ===> {}", body);   //对称解密ciphertext

            //构建request，传入必要参数(wxPaySDK0.4.8带有request方式验签的方法 github)
            NotificationRequest Nrequest = new NotificationRequest.Builder()
                    .withSerialNumber(wechatPaySerial)
                    .withNonce(nonce)
                    .withTimestamp(timestamp)
                    .withSignature(signature)
                    .withBody(body)
                    .build();

            NotificationHandler handler = new NotificationHandler(wxPayConfig.getVerifier(), wxPayConfig.getApiV3Key().getBytes(StandardCharsets.UTF_8));
            //验签和解析请求体(只有这里会报错)
            Notification notification = handler.parse(Nrequest);
            log.info("验签成功");

            //从notification获取请求报文(对称解密)
            String plainText = notification.getDecryptData();
            log.info("请求报文===>{}", plainText);
            //将密文转成map 方便拿取
            HashMap resultMap = gson.fromJson(plainText, HashMap.class);
            log.info("请求报文map===>{}", resultMap);


            log.warn("收到支付结果通知，处理订单........");
            //TODO 处理订单
            //////////////////////////////////////////////////
            processOrder(plainText);
            /**
             * 验签结果 ===> {"mchid":"1558950191","appid":"wx74862e0dfcf69954",
             * "out_trade_no":"ORDER_20221004132527865","transaction_id":"4200001569202210040857712725",
             * "trade_type":"NATIVE","trade_state":"SUCCESS","trade_state_desc":"支付成功",
             * "bank_type":"OTHERS","attach":"","success_time":"2022-10-04T13:25:40+08:00",
             * payer":{"openid":"oHwsHuCj4_t6OMpypikZIQ1r-FXY"},
             * "amount":{"total":1,"payer_total":1,"currency":"CNY","payer_currency":"CNY"}}
             */

            //成功应答
            response.setStatus(200);
            map.put("code", "SUCCESS");
            map.put("message", "成功");
            return gson.toJson(map);

        } catch (Exception e) {
            e.printStackTrace();
            log.error("验签失败");

            //应答失败
            response.setStatus(500);
            map.put("code", "ERROR");
            map.put("message", "验签失败");
            return gson.toJson(map);
        }

    }


    /**
     * 处理订单
     */
    @Override
    public void processOrder(String plainText) {
        log.info("处理订单");
        Gson gson = new Gson();

        //拿到map格式
        Map<String, Object> plainTextMap = gson.fromJson(plainText, HashMap.class);
        String orderNo = (String) plainTextMap.get("out_trade_no");

        /*在对业务数据进行状态检查和处理之前，
        要采用数据锁进行并发控制，
        以避免函数重入造成的数据混乱*/
        //尝试获取锁：
        // 成功获取则立即返回true，获取失败则立即返回false。不必一直等待锁的释放
        if (lock.tryLock()) {
            try {
                //处理重复通知
                //保证接口调用的幂等性：无论接口被调用多少次，产生的结果是一致的
                String orderStatus = orderInfoService.getOrderStatus(orderNo);
                if (!OrderStatus.NOTPAY.getType().equals(orderStatus)) {
                    return;
                }
                //更新订单状态
                orderInfoService.updateStatusByOrderNo(orderNo, OrderStatus.SUCCESS);
                //记录支付日志
                paymentInfoService.createPaymentInfo(plainText);
            } finally {
                lock.unlock();
            }
        }
    }


    /**
     * 用户取消未支付订单
     */
    @Override
    public void cancelOrder(String orderNo) throws Exception {
        //调用微信支付的关单接口
        this.closeOrder(orderNo);
        //更新商户端的订单状态
        orderInfoService.updateStatusByOrderNo(orderNo, OrderStatus.CANCEL);
    }

    /**
     * 调用微信统一关闭订单接口
     */
    private void closeOrder(String orderNo) throws Exception {
        log.info("关单接口的调用，订单号 ===> {}", orderNo);

        //创建远程请求对象
        String url = String.format(WxApiType.CLOSE_ORDER_BY_NO.getType(), orderNo);
        url = wxPayConfig.getDomain().concat(url);
        HttpPost httpPost = new HttpPost(url);

        //组装json请求体
        Gson gson = new Gson();
        Map<String, String> paramsMap = new HashMap<>();
        paramsMap.put("mchid", wxPayConfig.getMchId());
        String jsonParams = gson.toJson(paramsMap);
        log.info("请求参数 ===> {}", jsonParams);

        //将请求参数设置到请求对象中
        StringEntity entity = new StringEntity(jsonParams, "utf-8");
        entity.setContentType("application/json");
        httpPost.setEntity(entity);
        httpPost.setHeader("Accept", "application/json");

        //完成签名并执行请求
        try (CloseableHttpResponse response = httpClient.execute(httpPost)) {
            int statusCode = response.getStatusLine().getStatusCode();//响应状态码
            if (statusCode == 200) { //处理成功
                log.info("成功200");
            } else if (statusCode == 204) { //处理成功，无返回Body
                log.info("成功204");
            } else {
                log.info("JSAPI关单失败,响应码 = " + statusCode);
                throw new Exception("request failed");
            }
        }
    }


    /**
     * 查单接口调用
     *
     * @param orderNo
     */
    @Override
    public String queryOrder(String orderNo) throws IOException {
        log.info("查单接口调用====>{}", orderNo);

        String url = String.format(WxApiType.ORDER_QUERY_BY_NO.getType(), orderNo);
        url = wxPayConfig.getDomain().concat(url).concat("?mchid=").concat(wxPayConfig.getMchId());


        HttpGet httpGet = new HttpGet(url);
        httpGet.setHeader("Accept", "application/json");

        //完成签名并执行请求
        CloseableHttpResponse response = httpClient.execute(httpGet);

        try {
            String bodyAsString = EntityUtils.toString(response.getEntity());//响应体
            int statusCode = response.getStatusLine().getStatusCode();//响应状态码
            if (statusCode == 200) { //处理成功
                log.info("成功, 返回结果 = " + bodyAsString);
            } else if (statusCode == 204) { //处理成功，无返回Body
                log.info("成功");
            } else {
                log.info("JSAPI查单失败,响应码 = " + statusCode + ",返回结果 = " +
                        bodyAsString);
                throw new IOException("request failed");
            }
            return bodyAsString;
        } finally {
            response.close();
        }
    }

    /**
     * 根据订单号查询微信支付查单接口，核实订单状态
     * 如果订单已支付，则更新商户端订单状态，并记录支付日志
     * 如果订单未支付，则调用关单接口关闭订单，并更新商户端订单状态
     *
     * @param orderNo
     */
    @Override
    public void checkOrderStatus(String orderNo) throws Exception {
        log.warn("根据订单号查询订单状态===>{}", orderNo);

        //调用微信支付查单接口
        String result = this.queryOrder(orderNo);

        Gson gson = new Gson();
        Map resultMap = gson.fromJson(result, HashMap.class);

        //获取微信支付端的订单状态
        Object tradeState = resultMap.get("trade_state");
        //判断订单状态
        if (WxTradeState.SUCCESS.getType().equals(tradeState)) {
            log.warn("核实订单已支付 ===> {}", orderNo);
            //如果确认订单已支付则更新本地订单状态
            orderInfoService.updateStatusByOrderNo(orderNo, OrderStatus.SUCCESS);
            //记录支付日志
            paymentInfoService.createPaymentInfo(result);
        }
        if (WxTradeState.NOTPAY.getType().equals(tradeState)) {
            log.warn("核实订单未支付 ===> {}", orderNo);
            //如果订单未支付，则调用关单接口
            this.closeOrder(orderNo);
            //更新本地订单状态
            orderInfoService.updateStatusByOrderNo(orderNo, OrderStatus.CLOSED);
        }
    }


    /**
     * 退款
     */
    @Transactional(rollbackFor = Exception.class)
    @Override
    public void refund(String orderNo, String reason, Integer refundFee) throws Exception {
        log.info("===创建初始退款单记录===");

        //根据订单编号创建退款单
        RefundInfo refundsInfo = refundsInfoService.createRefundByOrderNo(orderNo, reason, refundFee);


        //调用统一退款API
        String url = wxPayConfig.getDomain().concat(WxApiType.DOMESTIC_REFUNDS.getType());
        HttpPost httpPost = new HttpPost(url);

        // 请求body参数
        Gson gson = new Gson();
        Map paramsMap = new HashMap();
        paramsMap.put("out_trade_no", orderNo);//订单编号
        paramsMap.put("out_refund_no", refundsInfo.getRefundNo());//退款单编号
        paramsMap.put("reason", reason);//退款原因
        paramsMap.put("notify_url", "https://wpctjt.com/wenxin/refunds/notify");//TODO 退款通知地址  改回公众号的

        Map amountMap = new HashMap();
        amountMap.put("refund", refundsInfo.getRefund());//退款金额
        amountMap.put("total", refundsInfo.getTotalFee());//原订单金额
        amountMap.put("currency", "CNY");//退款币种
        paramsMap.put("amount", amountMap);

        //将参数转换成json字符串
        String jsonParams = gson.toJson(paramsMap);
        log.info("请求参数 ===>" + jsonParams);

        StringEntity entity = new StringEntity(jsonParams, "utf-8");
        entity.setContentType("application/json");//设置请求报文格式
        httpPost.setEntity(entity);//将请求报文放入请求对象
        httpPost.setHeader("Accept", "application/json");//设置响应报文格式

        //完成签名并执行请求，并完成验签
        CloseableHttpResponse response = httpClient.execute(httpPost);

        try {
            //解析响应结果
            String bodyAsString = EntityUtils.toString(response.getEntity());
            int statusCode = response.getStatusLine().getStatusCode();
            if (statusCode == 200) {
                log.info("成功, 退款返回结果 = " + bodyAsString);
            } else if (statusCode == 204) {
                log.info("成功");
            } else {
                throw new RuntimeException("退款异常, 响应码 = " + statusCode + ", 退款返回结果 = " + bodyAsString);
            }
            //更新订单状态
            orderInfoService.updateStatusByOrderNo(orderNo, OrderStatus.REFUND_PROCESSING);

            //更新退款单
            refundsInfoService.updateRefund(bodyAsString);

        } finally {
            response.close();
        }
    }


    /**
     * 查询退款接口调用
     *
     * @param refundNo
     * @return
     */
    @Override
    public String queryRefund(String refundNo) throws Exception {
        log.info("查询退款接口调用 ===> {}", refundNo);

        String url = String.format(WxApiType.DOMESTIC_REFUNDS_QUERY.getType(), refundNo);
        url = wxPayConfig.getDomain().concat(url);

        //创建远程Get 请求对象
        HttpGet httpGet = new HttpGet(url);
        httpGet.setHeader("Accept", "application/json");

        //完成签名并执行请求
        CloseableHttpResponse response = httpClient.execute(httpGet);

        try {
            String bodyAsString = EntityUtils.toString(response.getEntity());
            int statusCode = response.getStatusLine().getStatusCode();
            if (statusCode == 200) {
                log.info("成功, 查询退款返回结果 = " + bodyAsString);
            } else if (statusCode == 204) {
                log.info("成功");
            } else {
                throw new RuntimeException("查询退款异常, 响应码 = " + statusCode + ", 查询退款返回结果 = " + bodyAsString);
            }
            return bodyAsString;
        } finally {
            response.close();
        }
    }

    /**
     * 根据退款单号核实退款单状态
     *
     * @param refundNo
     * @return
     */
    @Transactional(rollbackFor = Exception.class)
    @Override
    public void checkRefundStatus(String refundNo) throws Exception {

        log.warn("根据退款单号核实退款单状态 ===> {}", refundNo);

        //调用查询退款单接口
        String result = this.queryRefund(refundNo);

        //组装json请求体字符串
        Gson gson = new Gson();
        Map<String, String> resultMap = gson.fromJson(result, HashMap.class);

        //获取微信支付端退款状态
        String status = resultMap.get("status");
        String orderNo = resultMap.get("out_trade_no");

        if (WxRefundStatus.SUCCESS.getType().equals(status)) {
            log.warn("核实订单已退款成功 ===> {}", refundNo);
            //如果确认退款成功，则更新订单状态
            orderInfoService.updateStatusByOrderNo(orderNo,
                    OrderStatus.REFUND_SUCCESS);
            //更新退款单
            refundsInfoService.updateRefund(result);
        }

        if (WxRefundStatus.ABNORMAL.getType().equals(status)) {
            log.warn("核实订单退款异常 ===> {}", refundNo);
            //如果确认退款成功，则更新订单状态
            orderInfoService.updateStatusByOrderNo(orderNo,
                    OrderStatus.REFUND_ABNORMAL);
            //更新退款单
            refundsInfoService.updateRefund(result);
        }
    }

    /**
     * 处理退款单
     */
    @Transactional(rollbackFor = Exception.class)
    @Override
    public void processRefund(HashMap<String, Object> resultMap) throws Exception {
        log.info("退款单");
        Gson gson = new Gson();

        String orderNo = (String) resultMap.get("out_trade_no");
        if (lock.tryLock()) {
            try {
                String orderStatus = orderInfoService.getOrderStatus(orderNo);
                if (!OrderStatus.REFUND_PROCESSING.getType().equals(orderStatus)) {
                    return;
                }
                //更新订单状态
                orderInfoService.updateStatusByOrderNo(orderNo, OrderStatus.REFUND_SUCCESS);
                //更新退款单
                refundsInfoService.updateRefund(gson.toJson(resultMap));
            } finally {
                //要主动释放锁
                lock.unlock();
            }

        }
    }

    /**
     * 申请账单
     *
     * @param billDate
     * @param type
     * @return
     * @throws Exception
     */
    @Override
    public String queryBill(String billDate, String type) throws Exception {
        log.warn("申请账单接口调用{}，{}", billDate, type);

        String url = "";
        if ("tradebill".equals(type)) { //交易账单
            url = WxApiType.TRADE_BILLS.getType();
        } else if ("fundflowbill".equals(type)) { //资金账单
            url = WxApiType.FUND_FLOW_BILLS.getType();
        } else {
            log.error("账单类型错误");
            throw new RuntimeException("不支持的账单类型");
        }

        url = wxPayConfig.getDomain().concat(url).concat("?bill_date=").concat(billDate);

        //创建远程Get 请求对象
        HttpGet httpGet = new HttpGet(url);
        httpGet.addHeader("Accept", "application/json");

        //获得响应结果
        CloseableHttpResponse response = httpClient.execute(httpGet);

        try {
            String bodyAsString = EntityUtils.toString(response.getEntity());
            int statusCode = response.getStatusLine().getStatusCode();
            if (statusCode == 200) {
                log.info("成功, 申请账单返回结果 = " + bodyAsString);
            } else if (statusCode == 204) {
                log.info("成功");
            } else {
                throw new RuntimeException("申请账单异常, 响应码 = " + statusCode + ", 申请账单返回结果 = " + bodyAsString);
            }
            //获取账单下载地址
            Gson gson = new Gson();
            Map<String, String> resultMap = gson.fromJson(bodyAsString,
                    HashMap.class);
            return resultMap.get("download_url");
        } finally {
            response.close();
        }
    }


    /**
     * 下载账单
     *
     * @param billDate
     * @param type
     * @return
     * @throws Exception
     */
    @Override
    public String downloadBill(String billDate, String type) throws Exception {
        log.warn("下载账单接口调用 {}, {}", billDate, type);

        //获取账单url地址
        String downloadUrl = this.queryBill(billDate, type);
        //创建远程get 请求对象
        HttpGet httpGet = new HttpGet(downloadUrl);
        httpGet.addHeader("Accept", "application/json");

        CloseableHttpResponse response = wxPayNoSignClient.execute(httpGet);

        try {
            String bodyAsString = EntityUtils.toString(response.getEntity());
            int statusCode = response.getStatusLine().getStatusCode();
            if (statusCode == 200) {
                log.info("成功, 下载账单返回结果 = " + bodyAsString);
            } else if (statusCode == 204) {
                log.info("成功");
            } else {
                throw new RuntimeException("下载账单异常, 响应码 = " + statusCode + ", 下载账单返回结果 = " + bodyAsString);
            }
            return bodyAsString;
        } finally {
            response.close();
        }
    }

}
