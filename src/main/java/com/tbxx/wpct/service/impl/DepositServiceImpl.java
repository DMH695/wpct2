package com.tbxx.wpct.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.tbxx.wpct.dto.Result;
import com.tbxx.wpct.entity.BuildInfo;
import com.tbxx.wpct.entity.Deposit;
import com.tbxx.wpct.entity.OrderInfo;
import com.tbxx.wpct.enums.OrderStatus;
import com.tbxx.wpct.mapper.BuildInfoMapper;
import com.tbxx.wpct.mapper.CheckMapper;
import com.tbxx.wpct.mapper.DepositMapper;
import com.tbxx.wpct.mapper.OrderInfoMapper;
import com.tbxx.wpct.service.DepositService;
import com.tbxx.wpct.service.impl.BuildInfoServiceImpl;
import com.tbxx.wpct.service.impl.WechatPayServiceImpl;
import com.tbxx.wpct.util.OrderNoUtils;
import com.tbxx.wpct.util.UserList;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.util.List;

/**
 * @ClassName DepositServiceImpl
 * @Description TODO
 * @Author ZXX
 * @DATE 2022/10/14 14:54
 */


@Slf4j
@Service
public class DepositServiceImpl extends ServiceImpl<DepositMapper, Deposit> implements DepositService {

    @Autowired
    private BuildInfoServiceImpl buildInfoService;

    @Resource
    private BuildInfoMapper buildInfoMapper;

    @Autowired
    private WechatPayServiceImpl wechatPayService;

    @Resource
    private CheckMapper checkMapper;

    @Resource
    private OrderInfoMapper orderInfoMapper;


    @Override
    public Result jsapiPay(BuildInfo buildInfo, Integer money, String openid) throws Exception {
        String orderNo = OrderNoUtils.getOrderDe();  //生成商家订单号

        QueryWrapper<BuildInfo> queryWrapper = new QueryWrapper<>();

        queryWrapper.eq("village_name", buildInfo.getVillageName())
                .eq("build_no", buildInfo.getBuildNo()).eq("room_no", buildInfo.getRoomNo())
                .last("limit 1");


        BuildInfo oneBuildInfo = buildInfoMapper.selectOne(queryWrapper);

        //生成订单
        OrderInfo orderInfo = new OrderInfo();
        orderInfo.setTitle("武平城投缴费业务（押金）");   //商品描述
        orderInfo.setOrderNo(orderNo);  //商家订单号
        orderInfo.setVillageName(buildInfo.getVillageName());  //小区名
        orderInfo.setBuildNo(buildInfo.getBuildNo());         //楼号
        orderInfo.setRoomNo(buildInfo.getRoomNo());           //房号
        orderInfo.setCreateTime(LocalDateTime.now());       //创建时间
        orderInfo.setTotalFee(money);  //押金
        orderInfo.setOrderStatus(OrderStatus.NOTPAY.getType());//默认 未付款
        orderInfoMapper.insert(orderInfo);

        if ("".equals(oneBuildInfo.getOrderNo())) {
            buildInfoService.update().eq("village_name", buildInfo.getVillageName())
                    .eq("build_no", buildInfo.getBuildNo()).eq("room_no", buildInfo.getRoomNo())
                    .set("order_no", orderNo).update();
        }
        String resultJson = wechatPayService.depositJsapiPay(orderInfo, openid);
        log.warn("押金下单的结果是====>{}",resultJson);
        return Result.ok(resultJson);
    }


    /**
     * 押金列表（后台）
     */
    @Override
    public Result DepositList(int pageNum) {
        Page<Object> page = PageHelper.startPage(pageNum, 5);

        List<BuildInfo> buildInfoList = checkMapper.selectOrderNo();
        PageInfo<BuildInfo> pageInfo = new PageInfo<>(buildInfoList, 5);

        return Result.ok(pageInfo);
    }

    @Override
    public Result setMoney(Integer money,BuildInfo buildInfo) {
        buildInfoService.update().eq("village_name", buildInfo.getVillageName())
                .eq("build_no", buildInfo.getBuildNo()).eq("room_no", buildInfo.getRoomNo())
                .set("deposit",buildInfo.getDeposit()-money).update();
        return Result.ok("扣除成功");
    }


}
