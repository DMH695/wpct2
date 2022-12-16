package com.tbxx.wpct.util;

import cn.hutool.core.lang.UUID;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * 订单号工具类
 *
 * @Author qy
 * @since 1.0
 */
public class OrderNoUtils {

    /**
     * 获取订单编号(缴费)
     * @return
     */
    public static String getOrderNo() {
        return "ORDER_" + getNo();
    }

    /**
     * 获取退款单编号(缴费)
     * @return
     */
    public static String getRefundNo() {
        return "REFUND_" + getNo();
    }

    /**
     * 获取订单编号(押金)
     * @return
     */
    public static String getOrderDe() {
        return "ORDERDE_" + getNo();
    }

    /**
     * 获取退款单编号(押金)
     * @return
     */
    public static String getRefundDe() {
        return "REFUNDDE_" + getNo();
    }


    /**
     * 获取编号
     * @return
     */
    public static String getNo() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
        String newDate = sdf.format(new Date());
//        String result = "";
//        Random random = new Random();
//        for (int i = 0; i < 3; i++) {
//            result += random.nextInt(10);
//        }

        String result = UUID.randomUUID(true).toString().substring(1,10);
        return newDate + result;
    }

}
