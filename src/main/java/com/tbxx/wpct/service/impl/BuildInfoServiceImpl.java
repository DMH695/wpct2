package com.tbxx.wpct.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.tbxx.wpct.dto.Result;
import com.tbxx.wpct.entity.BuildInfo;
import com.tbxx.wpct.mapper.BuildInfoMapper;
import com.tbxx.wpct.service.BuildInfoService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @Author ZXX
 * @ClassName BuildInfoServiceImpl
 * @Description TODO
 * @DATE 2022/10/8 17:26
 */

@Slf4j
@Service
public class BuildInfoServiceImpl extends ServiceImpl<BuildInfoMapper, BuildInfo> implements BuildInfoService {

    /**
     * 增加绑定
     */
    @Override
    public Result addBindBuild(BuildInfo buildInfo) {
        baseMapper.insert(buildInfo);
        return Result.ok("添加成功！");
    }

    @Override
    public Result removeBuild(int buildid) {
        removeById(buildid);
        return Result.ok("解除绑定");
    }

    @Override
    public Result updateBuild(BuildInfo buildInfo) {
        QueryWrapper<BuildInfo> queryWrapper=new QueryWrapper<>();
        queryWrapper.eq("id",buildInfo.getId());
        update(buildInfo,queryWrapper);
        return Result.ok("修改成功");
    }


    // TODO 分页
    @Override
    public Result buildList(String openid){
        List<BuildInfo> buildInfoList = query().eq("openid", openid).list();
        if(buildInfoList == null){
            Result.ok("你的账号未绑定房屋信息");
        }

        return Result.ok(buildInfoList);
    };
}