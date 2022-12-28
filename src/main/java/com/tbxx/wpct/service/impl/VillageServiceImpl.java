package com.tbxx.wpct.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.tbxx.wpct.dto.Result;
import com.tbxx.wpct.entity.Village;
import com.tbxx.wpct.mapper.BuildMapper;
import com.tbxx.wpct.mapper.RoomMapper;
import com.tbxx.wpct.mapper.VillageMapper;
import com.tbxx.wpct.service.BuildService;
import com.tbxx.wpct.service.RoomService;
import com.tbxx.wpct.service.VillageService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
public class VillageServiceImpl extends ServiceImpl<VillageMapper, Village> implements VillageService {

    @Autowired
    private BuildServiceImpl buildService;

    @Autowired
    private RoomServiceImpl roomService;

    @Override
    public Result getTree(int pageSize, int pageNum) {
        Page<Village> page = PageHelper.startPage(pageNum,pageSize);
        List<Village> villages = baseMapper.selectList(null);
        PageInfo<Village> pageInfo = new PageInfo<>(villages,pageSize);
        JSONArray tree = JSONArray.parseArray(JSON.toJSONString(villages));
        for (Object o : tree) {
            JSONObject village = ((JSONObject) o);
            JSONArray builds = JSONArray.parseArray(
                    JSON.toJSONString(buildService.listByVillage(village.getInteger("id")))
            );
            for (Object o1 : builds) {
                JSONObject build = ((JSONObject) o1);
                JSONArray rooms = JSONArray.parseArray(
                        JSON.toJSONString(roomService.listByBuild(build.getInteger("id")))
                );
                build.put("rooms",rooms);
            }
            village.put("builds",builds);
        }
        JSONObject res = new JSONObject();
        res.put("tree",tree);
        res.put("pageInfo",pageInfo);
        return Result.ok(res);
    }
}
