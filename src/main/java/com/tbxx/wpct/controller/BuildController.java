package com.tbxx.wpct.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.tbxx.wpct.dto.Result;
import com.tbxx.wpct.entity.Build;
import com.tbxx.wpct.service.BuildService;
import com.tbxx.wpct.service.impl.BuildServiceImpl;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.ibatis.annotations.Delete;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

@CrossOrigin
@Api(tags = "楼栋管理模块")
@RestController
@RequestMapping("/build")
public class BuildController {

    @Autowired
    BuildServiceImpl buildService;

    @ApiOperation("插入新的楼栋")
    @PostMapping("/insert")
    public Result insert(@RequestBody Build build){
        return Result.ok(buildService.save(build));
    }

    @ApiOperation("删除楼栋")
    @DeleteMapping("/remove")
    public Result remove(@RequestParam Integer id){
        QueryWrapper<Build> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("id", id);
        return Result.ok(buildService.remove(queryWrapper));
    }

    @ApiOperation("更新楼栋信息")
    @PostMapping("/update")
    public Result update(@RequestBody Build build){
        return Result.ok(buildService.updateById(build));
    }
}
