package com.tbxx.wpct.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.tbxx.wpct.entity.Build;

import java.util.List;

public interface BuildService extends IService<Build> {

    List<Build> listByVillage(int villageId);

}
