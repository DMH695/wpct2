package com.tbxx.wpct.controller;

import com.tbxx.wpct.dto.Result;
import com.tbxx.wpct.entity.BuildInfo;
import com.tbxx.wpct.service.impl.BuildInfoServiceImpl;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.time.LocalDate;
import java.time.LocalDateTime;

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
@RequestMapping("/build/info")
public class BuildInfoController {


    @Autowired
    @Lazy
    private BuildInfoServiceImpl buildInfoService;


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

    @ApiOperation("获取房屋信息模板")
    @GetMapping("/get/template")
    public void getTemplate(HttpServletResponse response){
        buildInfoService.getTemplate(response);
    }

    @ApiOperation("导入房屋信息表格")
    @PostMapping("/import")
    public Result importData(MultipartFile file){
        if (file.isEmpty()){
            Result.fail("empty file");
        }
        return buildInfoService.importData(file);
    }

    @ApiOperation("按条件筛选导出excel")
    @GetMapping("/export")
    public void export2Excel(String villageName, String buildNo, String payStatus
            , String name, String beganDate,String endDate,HttpServletResponse response){
        buildInfoService.export2Excel(villageName, buildNo, payStatus, name, beganDate, endDate, response);
    }



}
