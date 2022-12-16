package com.tbxx.wpct.controller;

import com.tbxx.wpct.dto.Result;
import com.tbxx.wpct.entity.Consumption;
import com.tbxx.wpct.entity.PayInfo;
import com.tbxx.wpct.entity.SysRole;
import com.tbxx.wpct.service.impl.CheckServiceImpl;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

/**
 * @Author ZXX
 * @ClassName CheckController
 * @Description TODO
 * @DATE 2022/10/8 21:09
 */

@CrossOrigin 
@Api(tags = "账单管理")
@Slf4j
@RestController
@RequestMapping("/check")
public class CheckController {

    @Autowired
    CheckServiceImpl checkService;

    /**
     * 缴费添加(同步)
     */
    //@RequiresPermissions("zujin:add")
    @ApiOperation("新增缴费")
    @PostMapping("/add")
    public Result addCheck(@RequestBody PayInfo payinfo) {
        return checkService.addCheck(payinfo);
    }

    //@RequiresPermissions("zujin:list")
    @ApiOperation("后台缴费列表")
    @PostMapping("/blist")
    public Result ChecksList(@RequestParam int pageNum,@RequestParam String month) {
        return checkService.checksList(pageNum,month);
    }

    /**
     * 修改缴费(同步)
     */
    //@RequiresPermissions("zujin:update")
    @ApiOperation("修改缴费")
    @PostMapping("/update")
    public Result checkUpdate(@RequestBody PayInfo payinfo) {
        return checkService.checkUpdate(payinfo);

    }

    //@RequiresPermissions("zujin:remove")
    @ApiOperation("删除缴费")
    @PostMapping("/delete")
    public Result deleteCheck(@RequestParam("payinfoId") String checkid,@RequestParam(name = "orderNo") String orderId) {
        return checkService.deleteCheck(checkid,orderId);
    }



    /**
     * 前端微信用户缴费列表
     */
    @ApiOperation("前台微信用户列表")
    @PostMapping("/flist")
    public Result CheckList(@RequestParam String openid) {
        return checkService.checklist(openid);
    }


    /**
     * 前台查看明细
     */
    @ApiOperation("前台查看明细")
    @PostMapping("/fees")
    public Result checkFee(@RequestBody String checkId){
        return checkService.checkFee(checkId);
    }


}
