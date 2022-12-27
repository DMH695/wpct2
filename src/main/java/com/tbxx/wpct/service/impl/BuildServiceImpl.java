package com.tbxx.wpct.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.tbxx.wpct.entity.Build;
import com.tbxx.wpct.mapper.BuildMapper;
import com.tbxx.wpct.service.BuildService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;


@Slf4j
@Service
public class BuildServiceImpl extends ServiceImpl<BuildMapper, Build> implements BuildService {
    @Override
    public List<Build> listByVillage(int villageId) {
        QueryWrapper<Build> wrapper = new QueryWrapper<>();
        wrapper.eq("village_id", villageId);
        return baseMapper.selectList(wrapper);
    }
}
