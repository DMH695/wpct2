package com.tbxx.wpct.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.tbxx.wpct.entity.Consumption;
import com.tbxx.wpct.entity.PooledFee;
import org.mapstruct.Mapper;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @Author ZXX
 * @InterfaceName ConsumptionMapper
 * @Description TODO
 * @DATE 2022/10/8 21:12
 */
@Mapper
public interface ConsumptionMapper extends BaseMapper<Consumption> {

    @Transactional
    void updatePooledToZero();

    @Transactional
    void  addPooledFee(Integer Fee,String control,String villageName);

    @Transactional
    void updatePooledFee(Integer Fee,String control,String villageName);

    @Transactional
    void updateToNew(Integer Fee,String control,String villageName);

    @Transactional
    String selectVexist(String villageName);

    @Transactional
    List<Consumption> selectOrderStatus();

    @Transactional
    List<PooledFee> singleToNew(String control, String villageName);

}
