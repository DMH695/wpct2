package com.tbxx.wpct.controller;

import com.alibaba.excel.EasyExcel;
import com.tbxx.wpct.dto.Result;
import com.tbxx.wpct.entity.Examine;
import com.tbxx.wpct.entity.PooledFee;
import com.tbxx.wpct.service.impl.PooledFeeServiceImpl;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.SneakyThrows;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.List;

/**
 * @Author ZXX
 * @ClassName PooledFeeController
 * @Description TODO
 * @DATE 2022/10/10 19:19
 */

@CrossOrigin
@Api(tags = "公摊费模块")
@RestController
@RequestMapping("/pooled")
public class PooledFeeController {
    @Autowired
    PooledFeeServiceImpl pooledFeeService;

    /**
     * control  lift_fee为电梯费 water_fee为水费 electricity_fee为电费
     */
    //@RequiresPermissions("pooled:list")
    @ApiOperation("公摊列表")
    @GetMapping("/list")
    public Result pooledList(@RequestParam int pageNum){
        return pooledFeeService.pooledList(pageNum);
    }

    //@RequiresPermissions("pooled:add")
    @ApiOperation("新增公摊费")
    @PostMapping("/add")
    public Result addpooled(@RequestBody PooledFee pooledFee,@RequestParam String control){
        return pooledFeeService.addpooled(pooledFee,control);
    }

    //@RequiresPermissions("pooled:remove")
    @ApiOperation("删除公摊费")
    @GetMapping("/remove")
    public Result removepooled(@RequestParam int id){
        return pooledFeeService.removePooled(id);
    }

    //@RequiresPermissions("pooled:update")
    @ApiOperation("改公摊费")
    @PostMapping("/update")
    public Result updatepooled(@RequestBody PooledFee pooledFee,@RequestParam String control){
        return pooledFeeService.updatepooled(pooledFee,control);
    }

    @ApiOperation("查某一小区公摊费")
    @PostMapping("/cost")
    public Result singlepooled(@RequestParam String villageName ,@RequestParam String control){
        return pooledFeeService.singlepooled(villageName,control);
    }

    @SneakyThrows
    @ApiOperation("获取公摊费模板")
    @GetMapping("/get/template")
    public void getTemplate(HttpServletResponse response){
        pooledFeeService.getTemplate(response);
    }

    @ApiOperation("导入公摊费")
    @PostMapping("/import")
    public Result importPooled(@RequestParam MultipartFile file){
        if (file.isEmpty()){
            Result.fail("empty file");
        }
        return pooledFeeService.importPooled(file);
    }




}
