package com.tbxx.wpct.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.tbxx.wpct.dto.Result;
import com.tbxx.wpct.entity.Village;
import com.tbxx.wpct.service.VillageService;
import com.tbxx.wpct.service.impl.VillageServiceImpl;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@CrossOrigin
@Api(tags = "小区管理模块")
@RestController
@RequestMapping("/village")
public class VillageController {

    @Autowired
    VillageServiceImpl villageService;

    @ApiOperation("插入新的小区")
    @PostMapping("/insert")
    public Result insert(@RequestBody Village village){
        return Result.ok(villageService.save(village));
    }

    @ApiOperation("删除小区")
    @DeleteMapping("/remove")
    public Result remove(@RequestParam Integer id){
        QueryWrapper<Village> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("id", id);
        return Result.ok(villageService.remove(queryWrapper));
    }

    @ApiOperation("更新小区信息")
    @PostMapping("/update")
    public Result update(@RequestBody Village village){
        return Result.ok(villageService.updateById(village));
    }

    @ApiOperation("获取小区下楼房树形结构信息")
    @GetMapping("/tree")
    public Result tree(@RequestParam Integer pageSize, @RequestParam Integer pageNum){
        return villageService.getTree(pageSize,pageNum);
    }


}
