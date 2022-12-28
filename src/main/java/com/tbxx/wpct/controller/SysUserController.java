package com.tbxx.wpct.controller;

import com.tbxx.wpct.dto.Result;
import com.tbxx.wpct.entity.SysUser;
import com.tbxx.wpct.service.impl.SysUserServiceImpl;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;


/**
 * @Author ZXX
 * @ClassName SysUserController
 * @Description
 * @DATE 2022/9/29 21:04
 */

@CrossOrigin 
@Api(tags = "用户接口")
@Slf4j
@RestController
@RequestMapping("/user")
public class SysUserController {
    @Resource
    private SysUserServiceImpl userService;

    /**
     * 新增用户
     */
    //@RequiresPermissions("user:add")
    @ApiOperation("新增用户")
    @PostMapping("/add")
    public Result insertUser(@RequestBody SysUser user){
        return userService.insertUser(user);
    }

    /**
     * 删除用户
     */
    //@RequiresPermissions("user:remove")
    @ApiOperation("删除用户")
    @GetMapping("/remove")
    public Result removeUser(@RequestParam Integer ID){
        return userService.removeUser(ID);
    }

    //@RequiresPermissions("user:update")
    @ApiOperation("更新用户信息")
    @PostMapping("/update")
    public Result updateUser(@RequestBody SysUser user){
        return userService.updateUser(user);
    }

    //@RequiresPermissions("user:list")
    @ApiOperation("查看用户列表")
    @GetMapping("/list")
    public Result UserList(@RequestParam int pageNum,@RequestParam int pageSize){
        return Result.ok(userService.UserList(pageNum,pageSize));
    }



}
