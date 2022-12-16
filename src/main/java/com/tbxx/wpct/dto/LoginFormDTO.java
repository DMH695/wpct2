package com.tbxx.wpct.dto;

import lombok.Data;

/**
 * @Author ZXX
 * @ClassName LoginFormDTO
 * @Description
 * @DATE 2022/9/30 11:45
 */

@Data
public class LoginFormDTO {
    /**
     * 账号
     */
    private String userName;

    /**
     * 密码
     */
    private String password;
}
