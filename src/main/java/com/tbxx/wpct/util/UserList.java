package com.tbxx.wpct.util;

import lombok.Data;

import java.time.LocalDateTime;

/**
 * @Author ZXX
 * @ClassName UserList
 * @Description  查询用户列表
 * @DATE 2022/10/1 16:04
 */

@Data
public class UserList {
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
     * 用户对应的角色名
     */
    private String roleName;

    /**
     * 创建时间
     */
    private LocalDateTime createTime;

    /**
     * 更新时间
     */
    private LocalDateTime updateTime;
}
