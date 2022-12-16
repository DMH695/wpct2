package com.tbxx.wpct.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.tbxx.wpct.dto.Result;
import com.tbxx.wpct.entity.BuildInfo;
import com.tbxx.wpct.entity.Consumption;
import com.tbxx.wpct.entity.OrderInfo;
import com.tbxx.wpct.entity.PayInfo;
import com.tbxx.wpct.enums.OrderStatus;
import com.tbxx.wpct.mapper.*;
import com.tbxx.wpct.service.CheckService;
import com.tbxx.wpct.util.CheckUtil;
import com.tbxx.wpct.util.OrderNoUtils;
import com.tbxx.wpct.util.UserHolder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cglib.core.Local;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * @Author ZXX
 * @ClassName CheckServiceImpl
 * @Description TODO
 * @DATE 2022/10/9 18:40
 */
@Slf4j
@Service
public class CheckServiceImpl extends ServiceImpl<CheckMapper, PayInfo> implements CheckService {

    @Resource
    OrderInfoMapper orderInfoMapper;

    @Resource
    ConsumptionMapper consumptionMapper;

    @Resource
    PayInfoMapper payfoMapper;

    @Autowired
    BuildInfoServiceImpl buildInfoService;

    @Autowired
    ConsumptionServiceImpl consumptionService;


    /**
     * 新增缴费
     *
     * @param payinfo 缴费信息
     * @return ok
     */
    @Override
    public Result addCheck(PayInfo payinfo) {
        Consumption consumption = payinfo.getConsumption();
        OrderInfo orderInfo = new OrderInfo();
        String orderNo = OrderNoUtils.getOrderNo();  //生成商家订单号

        String checkid = CheckUtil.getCheckNo();  //生成consumption和payinfo 连接
        consumption.setBuildId(checkid);
        payinfo.setPayinfoId(checkid);
        payinfo.setOrderNo(orderNo);
        LocalDateTime time = LocalDateTime.now();
        payinfo.setPayBeginTime(time);
        payinfo.setPayEndTime(time.plus(30, ChronoUnit.DAYS));

        consumptionMapper.insert(consumption);
        payfoMapper.insert(payinfo);

        orderInfo.setTitle("武平城投缴费业务");   //商品描述
        orderInfo.setOrderNo(orderNo);  //商家订单号
        orderInfo.setVillageName(payinfo.getVillageName());  //小区名
        orderInfo.setBuildNo(payinfo.getBuildNo());         //楼号
        orderInfo.setRoomNo(payinfo.getRoomNo());           //房号
        orderInfo.setCreateTime(LocalDateTime.now());       //创建时间
        orderInfo.setTotalFee(consumption.getMonthCost());  //月缴费
        orderInfo.setCheckId(checkid);                      //连接co表 和 pay表
        if (consumption.getGwaterFee() != null && consumption.getLiftFee() != null && consumption.getElectricityFee() != null &&
                consumption.getGwaterFee() > 0 && consumption.getLiftFee() > 0 && consumption.getElectricityFee() > 0) {
            orderInfo.setStatus(1);
        }

        orderInfo.setOrderStatus(OrderStatus.NOTPAY.getType());//默认 未付款

        orderInfoMapper.insert(orderInfo);


        return Result.ok("添加成功");

    }

    /**
     * 后端缴费列表
     */
    @Override
    public Result checksList(int pageNum, String month) {
        PageHelper.startPage(pageNum, 5);
        List<PayInfo> payInfos = baseMapper.checksList(month);
        PageInfo<PayInfo> pageInfo = new PageInfo<>(payInfos, 5);
        return Result.ok(pageInfo);
    }


    /**
     * 前端微信用户缴费列表
     */
    @Override
    public Result checklist(String openid) {
        //openid 对应多个房屋信息
        List<BuildInfo> buildInfoList = buildInfoService.query().eq("openid", openid).list();

        //存放多个房子 多个时间段 多个订单集合
        List<OrderInfo> totalList = new ArrayList<>();

        //调用房屋集合 三个信息点 来查询 对应所有时间段订单
        for (BuildInfo buildInfo : buildInfoList) {
            QueryWrapper<OrderInfo> queryWrapper = new QueryWrapper<>();
            queryWrapper.eq("village_name", buildInfo.getVillageName()).eq("build_no", buildInfo.getBuildNo())
                    .eq("room_no", buildInfo.getRoomNo()).eq("status", 1);

            //一个房屋订单集合 加入总订单集合
            totalList.addAll(orderInfoMapper.selectList(queryWrapper));
        }
        Collections.sort(totalList);

//        totalList.forEach(
//                item -> item.setConsumption(consumptionService.query().eq("build_id", item.getCheckId()).one()));

//        List<Object> relist = new ArrayList<>();
//        for(OrderInfo orderInfo:totalList){
//            relist.add(orderInfo);
//            List<Consumption> con = consumptionService.query().eq("build_id", orderInfo.getCheckId()).list();
//            relist.add(con);
//        }


        return Result.ok(totalList);
    }


    @Override
    public Result checkFee(String checkId) {
        Consumption fee = consumptionService.query().eq("build_id", checkId).one();
        return Result.ok(fee);
    }

    /**
     * 修改缴费（同步）
     */
    @Override
    public Result checkUpdate(PayInfo payinfo) {
        //TODO 操作留底  和前端配合
        String userName = UserHolder.getUser().getUserName();
        log.warn("当前进行修改的用户是===>{}", userName);
        payinfo.setUpdateUser(userName);

        String payinfoId = payinfo.getPayinfoId();
        Consumption consumption = payinfo.getConsumption();
        String buildId = consumption.getBuildId();

        UpdateWrapper<PayInfo> updateWrapper1 = new UpdateWrapper<>();
        updateWrapper1.eq("payinfo_id", payinfoId);

        UpdateWrapper<Consumption> updateWrapper2 = new UpdateWrapper<>();
        updateWrapper2.eq("build_id", buildId);

        payfoMapper.update(payinfo, updateWrapper1);
        consumptionMapper.update(consumption, updateWrapper2);


        return Result.ok("更新成功!");
    }


    /**
     * (前后端) 删除缴费列表
     */
    @Override
    public Result deleteCheck(String checkid, String orderId) {
        //查询订单
        QueryWrapper<OrderInfo> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("order_no", orderId);
        orderInfoMapper.delete(queryWrapper);

        QueryWrapper<Consumption> cqueryWrapper = new QueryWrapper<>();
        cqueryWrapper.eq("build_id", checkid);

        QueryWrapper<PayInfo> pqueryWrapper = new QueryWrapper<>();
        pqueryWrapper.eq("payinfo_id", checkid);

        consumptionMapper.delete(cqueryWrapper);
        payfoMapper.delete(pqueryWrapper);

        return Result.ok("删除成功");
    }
}
