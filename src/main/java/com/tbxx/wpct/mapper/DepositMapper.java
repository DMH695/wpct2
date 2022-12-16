package com.tbxx.wpct.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.tbxx.wpct.entity.Deposit;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.data.map.repository.config.EnableMapRepositories;

/**
 * @ClassName DepositMapper
 * @Description TODO
 * @Author ZXX
 * @DATE 2022/10/14 14:55
 */
@Mapper
public interface DepositMapper extends BaseMapper<Deposit> {


}
