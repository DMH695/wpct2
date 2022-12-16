package com.tbxx.wpct.util.constant;

/**
 * @Author ZXX
 * @ClassName SysConstant
 * @Description 常量
 * @DATE 2022/9/29 18:42
 */

public class SysConstant {


    /**
     * 密码正则。6~20位的字母、数字、下划线
     */
    public static final String PASSWORD_REGEX = "^\\w{6,20}$";

    /**
     * 手机号正则
     */
    public static final String PHONE_REGEX = "^1([38][0-9]|4[579]|5[0-3,5-9]|6[6]|7[0135678]|9[89])\\d{8}$";

    /**
     * 身份证正则18
     */
    public static final String PID_REGEX18 = "^[1-9][0-9]{5}(18|19|20)[0-9]{2}((0[1-9])|(10|11|12))(([0-2][1-9])|10|20|30|31)[0-9]{3}([0-9]|(X|x))";

    /**
     * 身份证正则15
     */
    public static final String PID_REGEX15 = "^[1-9][0-9]{5}[0-9]{2}((0[1-9])|(10|11|12))(([0-2][1-9])|10|20|30|31)[0-9]{2}[0-9]";

}
