package com.tbxx.wpct.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.tbxx.wpct.entity.Room;
import com.tbxx.wpct.mapper.RoomMapper;
import com.tbxx.wpct.service.RoomService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
public class RoomServiceImpl extends ServiceImpl<RoomMapper, Room> implements RoomService {


    @Override
    public int insert(Room room) {
        return getBaseMapper().insert(room);
    }

    @Override
    public List<Room> listByBuild(int buildId) {
        QueryWrapper<Room> wrapper = new QueryWrapper<>();
        wrapper.eq("build_id", buildId);
        return baseMapper.selectList(wrapper);
    }
}
