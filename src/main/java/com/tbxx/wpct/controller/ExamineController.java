package com.tbxx.wpct.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.tbxx.wpct.dto.Result;
import com.tbxx.wpct.entity.Examine;
import com.tbxx.wpct.service.impl.ExamineServiceImpl;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * @Author ZXX
 * @ClassName ExamineController
 * @Description TODO
 * @DATE 2022/10/10 17:06
 */

@CrossOrigin
@Api(tags = "意见处理（审批）管理")
@Slf4j
@RestController
@RequestMapping("/examine")
public class ExamineController {

    @Autowired
    ExamineServiceImpl examineService;


    @ApiOperation("新增处理（审批）")
    @PostMapping("/add")
    public Result addExamine(@RequestBody Examine examine) {
        System.out.println(66666);
        return examineService.addExamine(examine);
    }

    //@RequiresPermissions("shenpi:remove")
    @ApiOperation("删除处理（审批）")
    @GetMapping("/remove")
    public Result removeExamine(@RequestParam Integer id) {
        QueryWrapper<Examine> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("id", id);
        examineService.remove(queryWrapper);
        return Result.ok("删除成功");
    }

    //@RequiresPermissions("shenpi:list")
    @ApiOperation("后台处理（审批）列表")
    @GetMapping("/list")
    public Result listExamine() {
        return examineService.listExamine();
    }

    @ApiOperation("微信用户历史 记录处理（审批）")
    @PostMapping("/wxlist")
    public Result userExamine(@RequestBody String openid) {
        return examineService.userExamineList(openid);
    }


    /**
     * 后台管理员处理用户审批
     */
    //@RequiresPermissions("shenpi:update")
    @ApiOperation("处理审批")
    @PostMapping("/solu")
    public Result soluExamine(@RequestParam Integer id,@RequestParam String openid,@RequestParam String resolveMsg) {
        return examineService.soluExamine(id, openid, resolveMsg);
    }

}
