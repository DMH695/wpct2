package com.tbxx.wpct.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.tbxx.wpct.dto.LoginFormDTO;
import com.tbxx.wpct.dto.Result;
import com.tbxx.wpct.dto.SR;
import com.tbxx.wpct.entity.SysUser;
import com.tbxx.wpct.util.page.PageResult;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

/**
 * @Author ZXX
 * @InterfaceName SysUserService
 * @Description
 * @DATE 2022/9/29 18:24
 */

public interface SysUserService extends IService<SysUser> {

    Result insertUser(SysUser sysUser);

    Result removeUser(Integer ID);

    Result updateUser(SysUser sysUser);

    SR authLogin(LoginFormDTO loginForm, HttpSession session);

    SysUser QueryUser(String username);

    Result logout(HttpServletRequest request);

    void updateUserRole(Integer roleId);

    PageResult UserList(int pageNum, int pageSize);
}
