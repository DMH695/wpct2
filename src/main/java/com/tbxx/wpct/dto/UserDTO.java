package com.tbxx.wpct.dto;


import lombok.Data;

import java.time.LocalDateTime;

/**
 * @Author ZXX
 * @ClassName UserDTO
 * @Description
 * @DATE 2022/9/30 11:44
 */

@Data
public class UserDTO {
    /**
     * 用户id
     */
    private Integer ID;

    /**
     * 用户名
     */
    private String userName;

    /**
     * 昵称
     */
    private String nickName;

    /**
     * 用户对应的角色id
     */
    private Integer roleId;


    /**
     * 创建时间
     */
    private LocalDateTime createTime;

    /**
     * 更新时间
     */
    private LocalDateTime updateTime;



}
