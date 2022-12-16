package com.tbxx.wpct.service.impl;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.google.gson.Gson;
import com.tbxx.wpct.entity.OrderInfo;
import com.tbxx.wpct.entity.RefundInfo;
import com.tbxx.wpct.enums.wxpay.WxRefundStatus;
import com.tbxx.wpct.mapper.RefundInfoMapper;
import com.tbxx.wpct.service.OrderInfoService;
import com.tbxx.wpct.service.RefundInfoService;
import com.tbxx.wpct.util.OrderNoUtils;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Service
public class RefundInfoServiceImpl extends ServiceImpl<RefundInfoMapper, RefundInfo> implements RefundInfoService {

    @Autowired
    private OrderInfoServiceImpl orderInfoService;

    /**
     * 根据订单号创建退款订单
     *
     * @param orderNo
     * @return
     */
    @Override
    public RefundInfo createRefundByOrderNo(String orderNo, String reason, Integer refundFee) {
        //根据订单号获取订单信息
        OrderInfo orderInfo = orderInfoService.getOrderByOrderNo(orderNo);
        Integer totalFee = orderInfo.getTotalFee();//原订单金额(分)
        if (totalFee < refundFee) {
            throw new RuntimeException("退款金额不得超过付款金额！");
        }

        //根据订单号生成退款订单
        RefundInfo refundInfo = new RefundInfo();
        refundInfo.setOrderNo(orderNo);//订单编号
        refundInfo.setRefundNo(OrderNoUtils.getRefundNo());//退款单编号
        refundInfo.setTotalFee(totalFee);//原订单金额(分)
        refundInfo.setRefund(refundFee);//退款金额(分)
        refundInfo.setReason(reason);//退款原因

        //保存预退款订单
        baseMapper.insert(refundInfo);
        return refundInfo;
    }


    /**
     * 记录退款记录
     *
     * @param content
     */
    @Override
    public void updateRefund(String content) {

        //将json字符串转换成Map
        Gson gson = new Gson();
        Map<String, String> resultMap = gson.fromJson(content, HashMap.class);

        log.warn("resultMap是===>{}", resultMap);

        //根据退款单编号修改退款单
        QueryWrapper<RefundInfo> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("refund_no", resultMap.get("out_refund_no"));

        //设置要修改的字段
        RefundInfo refundInfo = new RefundInfo();

        refundInfo.setRefundId(resultMap.get("refund_id"));//微信支付退款单号

        //查询退款和申请退款中的返回参数
        if (resultMap.get("status") != null) {
            refundInfo.setRefundStatus(resultMap.get("status"));//退款状态
            refundInfo.setContentReturn(content);//将全部响应结果存入数据库的content字段

            log.warn("content===>{}", content);
        }

        //退款回调中的回调参数
        if (resultMap.get("refund_status") != null) {
            refundInfo.setRefundStatus(resultMap.get("refund_status"));//退款状态
            refundInfo.setContentNotify(content);//将全部响应结果存入数据库的content字段
        }

        baseMapper.update(refundInfo, queryWrapper);
    }


    /**
     * 找出申请退款超过minutes分钟并且未成功的退款单
     *
     * @param minutes
     * @return
     */
    @Override
    public List<RefundInfo> getNoRefundOrderByDuration(int minutes) {

        //minutes之前的时间
        LocalDateTime beforeTime = LocalDateTime.now().minus(Duration.ofMinutes(minutes));

        QueryWrapper<RefundInfo> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("refund_status", WxRefundStatus.PROCESSING.getType());  //退款中的订单
        queryWrapper.le("create_time", beforeTime);
        List<RefundInfo> refundInfoList = baseMapper.selectList(queryWrapper);

        return refundInfoList;

    }


}
