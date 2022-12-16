package com.tbxx.wpct.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.tbxx.wpct.dto.Result;
import com.tbxx.wpct.entity.PooledFee;
import com.tbxx.wpct.mapper.ConsumptionMapper;
import com.tbxx.wpct.mapper.PooledFeeMapper;
import com.tbxx.wpct.service.PooledFeeService;
import com.tbxx.wpct.util.UserList;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * @Author ZXX
 * @ClassName PooledFeeServiceImpl
 * @Description TODO
 * @DATE 2022/10/10 19:32
 */

@Slf4j
@Service
public class PooledFeeServiceImpl extends ServiceImpl<PooledFeeMapper, PooledFee> implements PooledFeeService {

    @Resource
    private ConsumptionMapper consumptionMapper;

    @Resource
    private PooledFeeMapper pooledFeeMapper;


    /**
     * 新增公摊费
     */
    @Override
    public Result addpooled(PooledFee pooledFee, String control) {
        Integer Fee;
        String villageName = pooledFee.getVillageName();

        //查询该小区在本月是否已经有增加公摊费
        villageName = consumptionMapper.selectVexist(villageName);

        if (villageName == null) {
            Result.fail("该小区本月已有公摊费记录 请找到原处修改");
        }

        QueryWrapper<PooledFee> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("village_name", villageName);

        if (control.equals("lift_fee")) {
            Fee = pooledFee.getLiftFee();
        } else if (control.equals("water_fee")) {
            Fee = pooledFee.getWaterFee();
        } else {
            Fee = pooledFee.getElectricityFee(); //公电费
        }

        if (pooledFee.getCreateTime() == null) {
            baseMapper.insert(pooledFee);
        } else {
            baseMapper.update(pooledFee, queryWrapper);
        }


        consumptionMapper.addPooledFee(Fee, control, villageName);

        return Result.ok("新增成功");
    }

    /**
     * 公摊列表
     */
    @Override
    public Result pooledList(int pageNum) {
        QueryWrapper<PooledFee> queryWrapper = new QueryWrapper<>();
        queryWrapper.orderByDesc("create_time");

        PageHelper.startPage(pageNum, 5);
        List<PooledFee> pooledFees = baseMapper.selectList(queryWrapper);
        PageInfo<PooledFee> pageInfo = new PageInfo<>(pooledFees, 5);

        return Result.ok(pageInfo);
    }


    /**
     * 删除列表
     */
    @Override
    public Result removePooled(int id) {
        //删除公摊费表
        QueryWrapper<PooledFee> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("id", id);
        baseMapper.delete(queryWrapper);

        //删除后tb_consumption里面字段设置0;
        consumptionMapper.updatePooledToZero();

        return Result.ok("删除成功！");
    }

    /**
     * 更新列表
     */
    @Override
    public Result updatepooled(PooledFee pooledFee, String control) {
        //修改公摊费表
        int id = pooledFee.getId();
        UpdateWrapper<PooledFee> updateWrapper = new UpdateWrapper<>();
        updateWrapper.eq("id", id);
        baseMapper.update(pooledFee, updateWrapper);

        //修改关联表
        String villageName = pooledFee.getVillageName();
        Integer Fee;
        if (control.equals("lift_fee")) {
            Fee = pooledFee.getLiftFee();
        } else if (control.equals("water_fee")) {
            Fee = pooledFee.getWaterFee();
        } else {
            Fee = pooledFee.getElectricityFee(); //公电费
        }

        consumptionMapper.updateToNew(Fee, control, villageName);

        return Result.ok("更新成功");
    }


    /**
     * 查询单个公摊费
     */
    @Override
    public Result singlepooled(String villageName, String control) {
        if(control.equals("total")) {
            PooledFee pooledFee = query().eq("village_name", villageName).one();
            return Result.ok(pooledFee);
        }
        else {
            List<PooledFee> pooledFees = consumptionMapper.singleToNew(control, villageName);
            return Result.ok(pooledFees);
        }


    }
}
