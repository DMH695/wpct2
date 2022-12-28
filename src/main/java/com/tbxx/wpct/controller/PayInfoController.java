package com.tbxx.wpct.controller;

import com.github.pagehelper.PageInfo;
import com.tbxx.wpct.dto.PayInfoVo;
import com.tbxx.wpct.entity.PayInfo;
import com.tbxx.wpct.service.PayInfoService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin
@Api(tags = "缴费管理")
@Slf4j
@RestController
@RequestMapping("/payInfo")
public class PayInfoController {
    @Autowired
    PayInfoService payInfoService;
    @RequiresPermissions("payInfo:list")
    @ApiOperation("信息列表")
    @GetMapping("/list")
    //缴费多条件查询
    public PageInfo selectCondition(@RequestParam int pageNum,@RequestParam int pageSize,@RequestParam(required = false) PayInfoVo vo) {
        return payInfoService.splitpage(pageNum,pageSize,vo);
    }
}
