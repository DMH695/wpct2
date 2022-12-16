package com.tbxx.wpct.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.tbxx.wpct.dto.Result;
import com.tbxx.wpct.entity.Examine;

import java.util.List;

/**
 * @Author ZXX
 * @InterfaceName ExamineMapper
 * @Description TODO
 * @DATE 2022/10/10 17:03
 */

public interface ExamineMapper extends BaseMapper<Examine> {

    List<Examine>  listExamine();
}
