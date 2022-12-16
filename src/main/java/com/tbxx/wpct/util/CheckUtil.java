package com.tbxx.wpct.util;

import static com.tbxx.wpct.util.OrderNoUtils.getNo;

/**
 * @Author ZXX
 * @ClassName CheckUtil
 * @Description TODO
 * @DATE 2022/10/10 14:50
 */

public class CheckUtil {


    public static String getCheckNo() {
        return "CHECK_" + getNo();
    }
}
