package com.tbxx.wpct.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.github.pagehelper.PageInfo;
import com.tbxx.wpct.dto.PayInfoVo;
import com.tbxx.wpct.entity.PayInfo;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * @Author ZXX
 * @InterfaceName PayInfoMapper
 * @Description TODO
 * @DATE 2022/10/9 19:18
 */
@Mapper
public interface PayInfoMapper extends BaseMapper<PayInfo> {
    //缴费多条件查询
    List<PayInfo> selectCondition(PayInfoVo vo);
}
