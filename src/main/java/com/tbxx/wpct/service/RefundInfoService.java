package com.tbxx.wpct.service;


import com.baomidou.mybatisplus.extension.service.IService;
import com.tbxx.wpct.entity.RefundInfo;


import java.util.List;

public interface RefundInfoService extends IService<RefundInfo> {

    RefundInfo createRefundByOrderNo(String orderNo, String reason,Integer refundFee);

    void updateRefund(String content);

    List<RefundInfo> getNoRefundOrderByDuration(int minutes);
}
