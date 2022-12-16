package com.tbxx.wpct.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.tbxx.wpct.dto.Result;
import com.tbxx.wpct.dto.WechatUserDTO;
import com.tbxx.wpct.entity.BuildInfo;
import com.tbxx.wpct.entity.Consumption;
import com.tbxx.wpct.entity.PayInfo;

import java.util.List;

/**
 * @Author ZXX
 * @InterfaceName CheckMapper
 * @Description TODO
 * @DATE 2022/10/10 14:02
 */

public interface CheckMapper extends BaseMapper<PayInfo> {

    List<PayInfo> checksList(String month);

    //查询未缴费房屋 对应的openid
    List<WechatUserDTO> sendMsg();

    List<BuildInfo> selectOrderNo();


}
