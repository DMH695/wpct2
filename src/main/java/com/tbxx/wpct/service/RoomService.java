package com.tbxx.wpct.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.tbxx.wpct.entity.Room;

import java.util.List;

public interface RoomService extends IService<Room> {


    int insert(Room room);

    List<Room> listByBuild(int buildId);

}
