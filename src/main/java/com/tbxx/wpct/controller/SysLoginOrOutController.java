package com.tbxx.wpct.controller;

import com.tbxx.wpct.dto.LoginFormDTO;
import com.tbxx.wpct.dto.Result;
import com.tbxx.wpct.dto.SR;
import com.tbxx.wpct.service.impl.SysUserServiceImpl;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

/**
 * @Author ZXX
 * @ClassName SysLoginOrOutController
 * @Description
 * @DATE 2022/9/30 11:45
 */

@CrossOrigin 
@Api(tags = "后台管理系统登录操作接口")
@RestController
@RequestMapping("/log")
public class SysLoginOrOutController {
    @Resource
    SysUserServiceImpl userService;

    /**
     * 登录
     */
    @ApiOperation("登录")
    @PostMapping("/login")
    public SR authLogin(@RequestBody LoginFormDTO loginForm, HttpSession session){
        return userService.authLogin(loginForm, session);
    }

    /**
     * 登出
     */
    @ApiOperation("登出")
    @GetMapping("/logout")
    public Result Logout(HttpServletRequest request){
        return userService.logout(request);
    }
}
