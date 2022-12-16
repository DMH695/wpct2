package com.tbxx.wpct.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.tbxx.wpct.entity.OrderInfo;
import com.tbxx.wpct.enums.OrderStatus;

import java.util.List;

/**
 * @Author ZXX
 * @InterfaceName OrderInfoService
 * @Description TODO
 * @DATE 2022/10/9 17:58
 */

public interface OrderInfoService extends IService<OrderInfo> {


    String getOrderStatus(String orderNo);

    void updateStatusByOrderNo(String orderNo, OrderStatus orderStatus);

    OrderInfo getOrderByOrderNo(String orderNo);

    List<OrderInfo> getNoPayOrderByDuration(int minutes);
}
