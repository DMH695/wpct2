package com.tbxx.wpct.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.tbxx.wpct.dto.PayInfoVo;
import com.tbxx.wpct.entity.PayInfo;
import com.tbxx.wpct.mapper.PayInfoMapper;
import com.tbxx.wpct.service.PayInfoService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

/**
 * @Author ZXX
 * @ClassName PayInfoServiceImpl
 * @Description TODO
 * @DATE 2022/10/9 19:19
 */

@Slf4j
@Service
public class PayInfoServiceImpl extends ServiceImpl<PayInfoMapper, PayInfo> implements PayInfoService {
    @Autowired
    PayInfoMapper payInfoMapper;

    @Override
    public PageInfo splitpage(int pageNum, int pageSize,PayInfoVo vo) {
        PageHelper.startPage(pageNum,pageSize);
        List list = payInfoMapper.selectCondition(vo);
        PageInfo<PayInfo> pageInfo = new PageInfo(list);
        return pageInfo;
    }
}
