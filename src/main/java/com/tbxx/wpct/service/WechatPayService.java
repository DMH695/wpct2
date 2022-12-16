package com.tbxx.wpct.service;

import com.tbxx.wpct.entity.OrderInfo;
import com.wechat.pay.contrib.apache.httpclient.exception.HttpCodeException;
import com.wechat.pay.contrib.apache.httpclient.exception.NotFoundException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.security.GeneralSecurityException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.SignatureException;
import java.util.HashMap;
import java.util.Map;

/**
 * @Author ZXX
 * @InterfaceName WechatPayService
 * @Description
 * @DATE 2022/10/7 10:23
 */

public interface WechatPayService {
    String depositJsapiPay(OrderInfo orderInfo, String openid) throws Exception;

    String  jsapiPay(String openid, String orderId) throws Exception;

    String payNotify(HttpServletRequest request, HttpServletResponse response) throws GeneralSecurityException, NotFoundException, IOException, HttpCodeException;


    void processOrder(String plainText);

    void cancelOrder(String orderNo) throws Exception;

    String queryOrder(String orderNo) throws IOException;

    void checkOrderStatus(String orderNo) throws Exception;

    void refund(String orderNo, String reason,Integer refundFee) throws Exception;

    String queryRefund(String refundNo) throws Exception;

    void checkRefundStatus(String refundNo) throws Exception;

    void processRefund(HashMap<String, Object> resultMap) throws  Exception;

    String queryBill(String billDate, String type) throws Exception;

    String downloadBill(String billDate, String type) throws Exception;
}

