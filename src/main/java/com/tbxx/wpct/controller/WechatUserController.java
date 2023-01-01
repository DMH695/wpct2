package com.tbxx.wpct.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.tbxx.wpct.dto.Result;
import com.tbxx.wpct.dto.SR;
import com.tbxx.wpct.dto.WechatUserDTO;
import com.tbxx.wpct.entity.BuildInfo;
import com.tbxx.wpct.entity.WechatUser;
import com.tbxx.wpct.service.SendMsgService;
import com.tbxx.wpct.service.impl.BuildInfoServiceImpl;
import com.tbxx.wpct.service.impl.SendMsgServiceImpl;
import com.tbxx.wpct.service.impl.WechatPayServiceImpl;
import com.tbxx.wpct.service.impl.WechatUserServiceImpl;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

/**
 * @Author ZXX
 * @ClassName WechatUserController
 * @Description
 * @DATE 2022/10/7 18:40
 */
@CrossOrigin
@Api(tags = "微信用户")
@Slf4j
@RestController
@RequestMapping("/wenxin")
public class WechatUserController {

    @Autowired
    WechatUserServiceImpl wechatUserService;
    @Autowired
    SendMsgServiceImpl sendMsgService;

    @Lazy
    @Autowired
    BuildInfoServiceImpl buildInfoService;


    @ApiOperation("微信用户注册")
    @PostMapping ("/register")
    public Result register(@RequestBody WechatUser wechatUser){
        return wechatUserService.register(wechatUser);
    }

    @ApiOperation("微信用户信息")
    @PostMapping("/info")
    public Result getInfo(@RequestBody String openid){
       return wechatUserService.getInfo(openid);
    }

    //@RequiresPermissions("weixin:list")
    @ApiOperation("微信用户信息(后台)")
    @GetMapping("/binfo")
    public Result getInfoToBackend(@RequestParam int pageNum, @RequestParam int pageSize){
        return Result.ok(wechatUserService.splitpage(pageNum,pageSize));
    }

    @ApiOperation("微信用户催缴")
    @GetMapping("/sendHasten")
    public void sendHasten( WechatUserDTO wechatUserDTO)throws InterruptedException{
        sendMsgService.sendHasten(wechatUserDTO);
    }
}
