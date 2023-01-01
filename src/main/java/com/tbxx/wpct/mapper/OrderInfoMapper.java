package com.tbxx.wpct.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.tbxx.wpct.entity.OrderInfo;
import org.apache.ibatis.annotations.Mapper;

/**
 * @Author ZXX
 * @InterfaceName OrderInfoMapper
 * @Description TODO
 * @DATE 2022/10/9 17:57
 */
@Mapper
public interface OrderInfoMapper extends BaseMapper<OrderInfo> {
    OrderInfo getById(String id);
}
