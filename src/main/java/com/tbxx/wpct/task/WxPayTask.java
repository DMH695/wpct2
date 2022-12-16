package com.tbxx.wpct.task;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.google.gson.Gson;
import com.tbxx.wpct.config.WxPayConfig;
import com.tbxx.wpct.entity.Consumption;
import com.tbxx.wpct.entity.OrderInfo;
import com.tbxx.wpct.entity.RefundInfo;
import com.tbxx.wpct.mapper.ConsumptionMapper;
import com.tbxx.wpct.service.OrderInfoService;
import com.tbxx.wpct.service.impl.*;

import com.tbxx.wpct.util.wx.WeiXinUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.TimeUnit;

import static com.tbxx.wpct.util.constant.RedisConstants.ACCESS_TOKEN;
import static com.tbxx.wpct.util.constant.RedisConstants.ACCESS_TOKEN_TTL;



@Slf4j
@Component
public class WxPayTask {
    /**
     * 测试
     * (cron="秒 分 时 日 月 周")
     * *：每隔一秒执行
     * 0/3：从第0秒开始，每隔3秒执行一次
     * 1-3: 从第1秒开始执行，到第3秒结束执行
     * 1,2,3：第1、2、3秒执行
     * ?：不指定，若指定日期，则不指定周，反之同理
     */
//    @Scheduled(cron="0/3 * * * * ?")
//    public void task1() {
//        log.info("task1 执行====>{}", LocalDateTime.now());
//    }

    @Autowired
    private OrderInfoServiceImpl orderInfoService;

    @Autowired
    private WechatPayServiceImpl wxPayService;

    @Autowired
    private RefundInfoServiceImpl refundInfoService;

    @Resource
    private ConsumptionMapper consumptionMapper;

    @Autowired
    private ConsumptionServiceImpl consumptionService;

    @Resource
    private StringRedisTemplate stringRedisTemplate;

    @Resource
    private WxPayConfig wxPayConfig;



//    /**
//     * 从第0秒开始每隔30秒执行1次，查询创建超过5分钟，并且未支付的订单
//     */
//    @Scheduled(cron = "0/30 * * * * ?")
//    public void orderConfirm() throws Exception {
//        log.warn("====orderConfirm被执行====");
//
//        for (OrderInfo orderInfo : orderInfoService.getNoPayOrderByDuration(5)) {
//            String orderNo = orderInfo.getOrderNo();
//            log.warn("超时订单 ===> {}", orderNo);
//
//            //核实订单状态：调用微信支付查单接口
//            wxPayService.checkOrderStatus(orderNo);
//        }
//    }
//
//    /**
//     * 从第0秒开始每隔30秒执行1次，查询创建超过5分钟，并且未成功的退款单
//     */
//    @Scheduled(cron = "0/30 * * * * ?")
//    public void refundConfirm() throws Exception {
//        log.warn("====refundConfirm 被执行====");
//        //找出申请退款超过5分钟并且未成功的退款单
//        List<RefundInfo> refundInfoList = refundInfoService.getNoRefundOrderByDuration(5);
//        for (RefundInfo refundInfo : refundInfoList) {
//            String refundNo = refundInfo.getRefundNo();
//            log.warn("超时未退款的退款单号 ===> {}", refundNo);
//            //核实订单状态：调用微信支付查询退款接口
//            wxPayService.checkRefundStatus(refundNo);
//        }
//    }

//    /**
//     * 从第0秒开始每隔30秒执行1次,发送未写公摊费 未发送的缴费单
//     */
//    @Scheduled(cron = "0/30 * * * * ?")
//    public void orderInfo() throws Exception {
//        List<Consumption> consumptions = consumptionMapper.selectOrderStatus();
//        for (Consumption consumption : consumptions) {
//            if (consumption.getGwaterFee() > 0 && consumption.getLiftFee() > 0 && consumption.getElectricityFee() > 0) {
//
//                consumptionService.update().eq("build_id", consumption.getBuildId())
//                        .set("monthCost", consumption.getMonthCost() + consumption.getGwaterFee() + consumption.getLiftFee()
//                                + consumption.getElectricityFee()).update();
//
//                orderInfoService.update().eq("check_id", consumption.getBuildId()).
//                        set("status", 1).update();
//            }
//
//        }
//    }



}

