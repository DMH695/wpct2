package com.tbxx.wpct.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.tbxx.wpct.dto.Result;
import com.tbxx.wpct.entity.Room;
import com.tbxx.wpct.service.RoomService;

import com.tbxx.wpct.service.impl.RoomServiceImpl;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@CrossOrigin
@Api(tags = "房屋管理模块")
@RestController
@RequestMapping("/room")
public class RoomController {

    @Autowired
    RoomServiceImpl roomService;

    @ApiOperation("插入新的房屋")
    @PostMapping("/insert")
    public Result insert(@RequestBody Room room){
        return Result.ok(roomService.insert(room));
    }

    @ApiOperation("删除房屋")
    @DeleteMapping("/remove")
    public Result remove(@RequestParam Integer id){
        QueryWrapper<Room> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("id", id);
        return Result.ok(roomService.remove(queryWrapper));
    }

    @ApiOperation("更新房屋信息")
    @PostMapping("/update")
    public Result update(@RequestBody Room room){
        return Result.ok(roomService.updateById(room));
    }
}
