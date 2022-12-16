package com.tbxx.wpct.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.tbxx.wpct.dto.Result;
import com.tbxx.wpct.entity.Examine;
import com.tbxx.wpct.mapper.ExamineMapper;
import com.tbxx.wpct.service.ExamineService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

/**
 * @Author ZXX
 * @ClassName ExamineServiceImpl
 * @Description TODO
 * @DATE 2022/10/10 17:04
 */

@Slf4j
@Service
public class ExamineServiceImpl extends ServiceImpl<ExamineMapper, Examine> implements ExamineService {

    /**
     * 新增审批
     */
    @Override
    public Result addExamine(Examine examine) {
        baseMapper.insert(examine);
        return Result.ok("新增成功");
    }


    /**
     * 后台审批列表
     */
    @Override
    public Result listExamine() {
        List<Examine> examines = baseMapper.listExamine();
        return Result.ok(examines);
    }

    /**
     * 微信用户审批列表
     */
    @Override
    public Result userExamineList(String openid) {
        QueryWrapper<Examine> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("openid",openid);
        queryWrapper.orderByDesc("commit_time");
        List<Examine> examineList = baseMapper.selectList(queryWrapper);
        return Result.ok(examineList);
    }


    /**
     * 处理意见
     */
    @Override
    public Result soluExamine(Integer id, String openid, String resolveMsg) {
        UpdateWrapper<Examine> updateWrapper = new UpdateWrapper<>();
        //根据openid和id锁定处理的信息
        updateWrapper.eq("id",id).eq("openid",openid);
        Examine examine = new Examine();
        examine.setExamineTime(LocalDateTime.now());
        examine.setResolveMsg(resolveMsg);
        examine.setExamineStatus("已处理");
        baseMapper.update(examine,updateWrapper);
        return Result.ok("操作成功！");
    }
}
