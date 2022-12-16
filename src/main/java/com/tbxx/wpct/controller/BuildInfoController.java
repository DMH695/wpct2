package com.tbxx.wpct.controller;

import com.tbxx.wpct.dto.Result;
import com.tbxx.wpct.entity.BuildInfo;
import com.tbxx.wpct.service.impl.BuildInfoServiceImpl;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * @Author ZXX
 * @ClassName BuildInfoController
 * @Description TODO
 * @DATE 2022/10/8 19:42
 */

@CrossOrigin 
@Api(tags = "房屋信息")
@Slf4j
@RestController
@RequestMapping("/build")
public class BuildInfoController {


    @Autowired
    BuildInfoServiceImpl buildInfoService;


    @ApiOperation("用户新增绑定房屋信息")
    @PostMapping("/add")
    public Result addBindBuild(@RequestBody BuildInfo buildInfo) {
        return buildInfoService.addBindBuild(buildInfo);
    }


    @ApiOperation("用户房屋列表")
    @PostMapping("/list")
    public Result BuildList(@RequestBody String openid) {
        return buildInfoService.buildList(openid);
    }


    @ApiOperation("用户修改绑定房屋信息")
    @PostMapping("/update")
    public Result updateBindBuild(@RequestBody BuildInfo buildInfo) {
        return buildInfoService.updateBuild(buildInfo);
    }

    @ApiOperation("用户删除房屋信息")
    @GetMapping("/remove")
    public Result removeBindBuild(@RequestParam int id) {
        return buildInfoService.removeBuild(id);
    }



}
