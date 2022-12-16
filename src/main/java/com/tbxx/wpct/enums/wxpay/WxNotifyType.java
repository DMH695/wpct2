package com.tbxx.wpct.enums.wxpay;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum WxNotifyType {

	/**
	 * 支付通知
	 */
	NATIVE_NOTIFY("/wenxin/jsapi/notify"),


	/**
	 * 退款结果通知
	 */
	REFUND_NOTIFY("/wenxin/refunds/notify");

	/**
	 * 类型
	 */
	private final String type;
}
