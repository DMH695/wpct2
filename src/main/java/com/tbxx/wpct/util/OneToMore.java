package com.tbxx.wpct.util;

import com.tbxx.wpct.entity.SysPerm;
import com.tbxx.wpct.entity.SysRole;
import com.tbxx.wpct.entity.SysUser;
import lombok.Data;

import java.util.List;

/**
 * @Author ZXX
 * @ClassName OneToMore
 * @Description 一对多
 * @DATE 2022/10/1 14:07
 */

@Data
public class OneToMore {
    private List<SysUser> users;
    private List<SysPerm> menus;
    private List<SysPerm> permissions;
    private List<SysRole> roles;
    private String roleId;
    private String roleName;
}
