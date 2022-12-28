package com.tbxx.wpct.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.tbxx.wpct.dto.Result;
import com.tbxx.wpct.entity.BuildInfo;
import com.tbxx.wpct.entity.Deposit;
import com.tbxx.wpct.mapper.DepositMapper;
import com.tbxx.wpct.service.impl.DepositServiceImpl;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;


@CrossOrigin
@Api(tags = "押金管理")
@Slf4j
@RestController
@RequestMapping("/deposit")
public class DepositController {

    @Autowired
    @Lazy
    private DepositServiceImpl depositService;
    @Resource
    private DepositMapper depositMapper;

    //@RequiresPermissions("deposit:add")
    @ApiOperation("新增小区押金")
    @PostMapping("/add")
    public Result addDeposit(@RequestBody Deposit deposit) {
        Integer money = deposit.getDeposit();
        deposit.setDeposit(money * 100);  //数据库单位是分
        depositService.save(deposit);
        return Result.ok("添加成功");
    }

    //@RequiresPermissions("deposit:remove")
    @ApiOperation("删除小区押金")
    @GetMapping("/remove")
    public Result removeDeposit(@RequestParam Integer id) {
        depositService.removeById(id);
        return Result.ok("删除成功");
    }


    @ApiOperation("小区押金列表")
    @GetMapping("/list")
    public Result listDeposit() {
        List<Deposit> list = depositService.list();
        return Result.ok(list);
    }

    //@RequiresPermissions("deposit:update")
    @ApiOperation("修改小区押金")
    @PostMapping("/update")
    public Result userDeposit(@RequestBody Deposit deposit) {
        QueryWrapper<Deposit> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("id", deposit.getId());
        depositMapper.update(deposit, queryWrapper);
        return Result.ok("修改成功");
    }

    @ApiOperation("查看小区押金")
    @GetMapping("/query")
    public Result checkDeposit(@RequestParam String houseType) {
        Deposit deposit = depositService.query().eq("house_type", houseType).one();
        return Result.ok(deposit.getDeposit());
    }


    @ApiOperation("押金下单")
    @PostMapping("/pay")
    public Result wechatPayDeposit(@RequestBody BuildInfo buildInfo, @RequestParam Integer money, @RequestParam String openid) throws Exception {
        return depositService.jsapiPay(buildInfo, money, openid);
    }

    //@RequiresPermissions("deposit:list")
    @ApiOperation("押金列表（后台）")
    @PostMapping("/blist")
    public Result DepositBList(@RequestParam int pageNum) {
        return depositService.DepositList(pageNum);
    }

    @ApiOperation("扣押金") //扣数据库
    @PostMapping("/delete")
    public Result deleteMoney(@RequestParam Integer money, @RequestBody BuildInfo buildInfo) {
        money = money * 100;
        return depositService.setMoney(money, buildInfo);
    }


}
