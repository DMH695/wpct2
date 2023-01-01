package com.tbxx.wpct.controller;

import cn.hutool.core.util.RandomUtil;
import cn.hutool.crypto.digest.DigestUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.google.gson.Gson;
import com.tbxx.wpct.dto.Result;
import com.tbxx.wpct.entity.WechatUser;
import com.tbxx.wpct.service.impl.WechatPayServiceImpl;
import com.tbxx.wpct.service.impl.WechatUserServiceImpl;
import com.tbxx.wpct.config.WxPayConfig;
import com.tbxx.wpct.util.HttpUtils;
import com.tbxx.wpct.util.wx.HttpUtil;
import com.tbxx.wpct.util.wx.WeiXinUtil;
import com.wechat.pay.contrib.apache.httpclient.exception.HttpCodeException;
import com.wechat.pay.contrib.apache.httpclient.exception.NotFoundException;
import com.wechat.pay.contrib.apache.httpclient.exception.ParseException;
import com.wechat.pay.contrib.apache.httpclient.exception.ValidationException;
import com.wechat.pay.contrib.apache.httpclient.notification.Notification;
import com.wechat.pay.contrib.apache.httpclient.notification.NotificationHandler;
import com.wechat.pay.contrib.apache.httpclient.notification.NotificationRequest;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.impl.client.HttpClients;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.security.GeneralSecurityException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.SignatureException;
import java.util.*;
import java.util.concurrent.TimeUnit;

import static com.tbxx.wpct.util.constant.RedisConstants.ACCESS_TOKEN;
import static com.tbxx.wpct.util.constant.RedisConstants.ACCESS_TOKEN_TTL;

/**
 * @Author ZXX
 * @ClassName WeChatPayController
 * @Description
 * @DATE 2022/10/6 13:48
 */

@Api(tags = "微信支付JSAPI  ")
@Slf4j
@RestController
@RequestMapping("/weixin")
@CrossOrigin
public class WeChatPayController {

    @Resource
    private  WxPayConfig wxPayConfig;

    @Autowired
    private  WechatPayServiceImpl wechatPayService;

    @Autowired
    private  WechatUserServiceImpl wechatUserService;




    private String domain = "https://4s3471264h.zicp.fun";  //"http://fjwpct.com";  //"http://dadanb.top";  //"https://4s3471264h.zicp.fun";

    String app1 = "wxb7756386a217f9f1";
    String app2 = "705ab7713492d438d4181c211e82f0ec";


    @ApiOperation("获取授权码")
    @GetMapping("/auth")
    @CrossOrigin
    public void auth(HttpServletResponse response, HttpServletRequest request) throws IOException {
        //1.
        String openid = request.getParameter("openid");

        System.out.println("我拿到openid是" + openid);

        if (openid == null || openid == "" || openid == "null") { //让用户拿code 就是注册

            //String path = domain + "/#/wxauth";
            String path = domain + "/weixin/callback";//"/#/wxauth"; //"/weixin/callback"; "/#/weixinlogin"   "/auth.html" "/#/wxauth"
            try {
                path = URLEncoder.encode(path, "UTF-8");
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }

            String url = "https://open.weixin.qq.com/connect/oauth2/authorize?" +
                    "appid=" + wxPayConfig.getAppid() +
                    "&redirect_uri=" + path +
                    "&response_type=code" +
                    "&scope=snsapi_userinfo" +
                    "&state=1" +
                    "#wechat_redirect ";
            response.sendRedirect(url);
        } else { //验证通过 让用户登录
            String url = domain + "/?openid=" + openid + "#/wxauth";
            System.out.println("我要跳转网页是" + url);
            response.sendRedirect(url);
        }

    }


    @ApiOperation("获得微信用户信息")
    @CrossOrigin
    @RequestMapping(value = "/callback", method = RequestMethod.GET)
    public Result callback(HttpServletResponse response, HttpServletRequest request) throws IOException {

        log.warn("我带着code来了");
        String code = request.getParameter("code");
        log.warn("我拿到code了==>{}", code);
        // String state = request.getParameter("state");

        /**
         * 获取code后，请求以下链接获取access_token：
         * https://api.weixin.qq.com/sns/oauth2/access_token?appid=APPID&secret=SECRET&code=CODE&grant_type=authorization_code
         */
        //2.通过code换取网页token
        String url = "https://api.weixin.qq.com/sns/oauth2/access_token?" +
                "appid=" + wxPayConfig.getAppid() +
                "&secret=" + wxPayConfig.getAppSecret() +
                "&code=" + code +
                "&grant_type=authorization_code";
        String s = HttpUtil.get(url);
        JSONObject object = JSON.parseObject(s);

        log.warn("json是{}", object);

        /**
         {
         "access_token":"ACCESS_TOKEN",
         "expires_in":7200,
         "refresh_token":"REFRESH_TOKEN",
         "openid":"OPENID",
         "scope":"SCOPE"
         }
         */
        String accessToken = object.getString("access_token");
        String openid = object.getString("openid");
        log.warn("token==>{}", accessToken);
        log.warn("openid==>{}", openid);

        //3.根据openid和token获取用户基本信息
        /**
         * http：GET（请使用https协议）
         * https://api.weixin.qq.com/sns/userinfo?access_token=ACCESS_TOKEN&openid=OPENID&lang=zh_CN
         */
        String userUrl = "https://api.weixin.qq.com/sns/userinfo?" +
                "access_token=" + accessToken +
                "&openid=" + openid +
                "&lang=zh_CN";

        // String userRes = WeiXinUtil.httpRequest(userUrl, "GET", null);
        String userRes = HttpUtil.get(userUrl);
        log.warn("用户的信息==>{}", userRes);

        //将openid返回给前端  10-19
        Gson gson = new Gson();
        HashMap getMap = gson.fromJson(userRes, HashMap.class);
        String getOpenid = (String) getMap.get("openid");
        Map<String,String> rMap = new HashMap<>();
        rMap.put("openid",getOpenid);
        log.warn("传给前端的是====>{}",rMap);


        JSONObject jsonObject = JSONObject.parseObject(userRes);
        WechatUser user = wechatUserService.query().eq("openid", openid).one();
        if (user == null) {   //TODO *一个人也可以绑定多个房屋信息 这里逻辑是一个openid（用户）只能绑定一套房屋
            //保存用户信息
            WechatUser wechatUser = new WechatUser();
            wechatUser.setOpenid(jsonObject.getString("openid"));
            wechatUser.setNickname(jsonObject.getString("nickname"));
            wechatUserService.save(wechatUser);
        }
//        if (user == null || user.getPid() == null) {
//            //TODO 跳转注册页面
//        }
//        // TODO 绑定身份证-> 跳转首页


        userUrl = "https://60z8193p42.goho.co//zqb/new.html" + "?openid=" + jsonObject.getString("openid");

        //TODO 如果没有授权登录 跳转注册页面  未完成
        //response.sendRedirect(userUrl);
        if (wechatUserService.getByOpenid(openid).getNumber() == null || "".equals(wechatUserService.getByOpenid(openid).getNumber())){
            rMap.put("isRegister",Boolean.FALSE.toString());
        }else {
            rMap.put("isRegister",Boolean.TRUE.toString());
        }
        return Result.ok(rMap);
    }


    /**
     * 初始化前端 wx.config必要参数
     */
    @ApiOperation("获取jsapiSDK")
    @PostMapping("/jsapi/sdk")
    public Result wechatPaySDK() {
        Gson gson = new Gson();

        // https://api.weixin.qq.com/cgi-bin/token?grant_type=client_credential&appid=APPID&secret=APPSECRET
        String url1 = "https://api.weixin.qq.com/cgi-bin/token?grant_type=client_credential&appid=" + wxPayConfig.getAppid() + "&secret=" + wxPayConfig.getAppSecret();
        String resu1 = WeiXinUtil.httpRequest(url1, "GET", null);
        log.warn("结果1是==>{}", resu1);

        HashMap<String, Object> map = gson.fromJson(resu1, HashMap.class);
        String access_token = (String) map.get("access_token");
        log.warn("access_token{}",access_token);



        // https://api.weixin.qq.com/cgi-bin/ticket/getticket?access_token="+access_token+"&type=jsapi";
        url1 = "https://api.weixin.qq.com/cgi-bin/ticket/getticket?access_token=" + access_token + "&type=jsapi";
        String resu2 = WeiXinUtil.httpRequest(url1, "GET", null);
        log.warn("结果2是==>{}", resu2);

        HashMap<String, Object> map2 = gson.fromJson(resu2, HashMap.class);
        String ticket = (String) map2.get("ticket");
        String nonceStr = RandomUtil.randomString(32);// 随机字符串
        String timeStamp = String.valueOf(System.currentTimeMillis() / 1000);// 时间戳
        String url = "http://wpct.x597.com";   //test
        //String url = "http://fjwpct.com";

        String jsapi_ticket = "jsapi_ticket=" + ticket + "&noncestr=" + nonceStr + "&timestamp=" + timeStamp + "&url=" + url;

        String resSign = DigestUtil.sha1Hex(jsapi_ticket);
        HashMap<String, Object> respJsonMap = new HashMap();

        respJsonMap.put("appId", wxPayConfig.getAppid());
        respJsonMap.put("timestamp", timeStamp);
        respJsonMap.put("nonceStr", nonceStr);
        respJsonMap.put("signature", resSign);

        String respJson = gson.toJson(respJsonMap);

        return Result.ok(respJson);
    }


    @ApiOperation("JSAPI下单")
    @PostMapping("/jsapi/pay")
    public Result wechatPay(@RequestParam String openid, @RequestParam(name = "id") String orderId) throws Exception {
        log.warn("JSAPI下单");
        String resultJson = wechatPayService.jsapiPay(openid, orderId);
        return Result.ok(resultJson);
    }


    @ApiOperation("支付结果通知")
    @PostMapping("/jsapi/notify")
    public Result wechatPayNotify(HttpServletRequest request, HttpServletResponse response) throws GeneralSecurityException, NotFoundException, IOException, HttpCodeException {
        log.warn("支付结果通知");
        String notify = wechatPayService.payNotify(request, response);
        return Result.ok(notify);
    }


    /**
     * 用户取消订单
     * //TODO 实际可能用不到
     */
    @ApiOperation("用户取消订单")
    @PostMapping("/cancel/{orderNo}")
    public Result cancel(@PathVariable String orderNo) throws Exception {
        log.info("取消订单");
        wechatPayService.cancelOrder(orderNo);
        return Result.ok("订单已取消");
    }

    /**
     * 查询订单
     */
    @ApiOperation("查询订单：测试订单状态用")
    @GetMapping("query/{orderNo}")
    public Result queryOrder(@PathVariable String orderNo) throws IOException {
        log.info("查询订单");
        String bodyAsString = wechatPayService.queryOrder(orderNo);
        return Result.ok("查询成功", bodyAsString);
    }


    @ApiOperation("申请退款")
    @PostMapping("/refunds/{orderNo}/{reason}/{refundFee}")
    public Result refunds(@PathVariable String orderNo, @PathVariable String reason, @PathVariable Integer refundFee)
            throws Exception {
        log.info("申请退款");
        wechatPayService.refund(orderNo, reason,refundFee);
        return Result.ok();
    }

    /**
     * 查询退款
     *
     * @param refundNo
     * @return
     * @throws Exception
     */
    @ApiOperation("查询退款：测试用")
    @GetMapping("/query-refund/{refundNo}")
    public Result queryRefund(@PathVariable String refundNo) throws Exception {
        log.info("查询退款");
        String result = wechatPayService.queryRefund(refundNo);
        return Result.ok(result);
    }


    /**
     * 退款结果通知
     * 退款状态改变后，微信会把相关退款结果发送给商户。
     */
    @ApiOperation("退款结果通知")
    @PostMapping("/refunds/notify")
    public String refundsNotify(HttpServletRequest request, HttpServletResponse response) throws ValidationException, ParseException {

        log.info("退款通知执行");
        Gson gson = new Gson();
        Map<String, String> map = new HashMap<>(); //应答对象

        try {
            //处理通知参数
            String body = HttpUtils.readData(request);
            String wechatPaySerial = request.getHeader("Wechatpay-Serial");
            String nonce = request.getHeader("Wechatpay-Nonce");
            String timestamp = request.getHeader("Wechatpay-Timestamp");
            String signature = request.getHeader("Wechatpay-Signature");
            HashMap<String, Object> bodyMap = gson.fromJson(body, HashMap.class);
            String requestId = (String) bodyMap.get("id");
            log.info("退款通知执行 ===> {}", requestId);
            log.info("退款通知的完整数据 ===> {}", body);   //对称解密ciphertext

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

            //处理退款单
            wechatPayService.processRefund(resultMap);

            //成功应答
            response.setStatus(200);
            map.put("code", "SUCCESS");
            map.put("message", "成功");
            return gson.toJson(map);
        } catch (Exception e) {
            e.printStackTrace();
            //失败应答
            response.setStatus(500);
            map.put("code", "ERROR");
            map.put("message", "失败");
            return gson.toJson(map);
        }

    }

    @ApiOperation("获取账单url：测试用")
    @GetMapping("/querybill/{billDate}/{type}")   //type：tradebill|fundflowbill
    public Result queryTradeBill(@PathVariable String billDate, @PathVariable String type) throws Exception {
        log.info("获取账单url");
        String downloadUrl = wechatPayService.queryBill(billDate, type);
        return Result.ok("获取账单url成功", downloadUrl);
    }

    @ApiOperation("下载账单")
    @GetMapping("/downloadbill/{billDate}/{type}")
    public Result downloadBill(@PathVariable String billDate, @PathVariable String type) throws Exception {
        log.info("下载账单");
        String result = wechatPayService.downloadBill(billDate, type);
        return Result.ok(result);
    }

}
