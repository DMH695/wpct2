package com.tbxx.wpct.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.tbxx.wpct.dto.PayInfoVo;
import com.tbxx.wpct.entity.PayInfo;
import com.tbxx.wpct.entity.PayInfo2;
import com.tbxx.wpct.mapper.PayInfoMapper;
import com.tbxx.wpct.service.PayInfoService;
import com.tbxx.wpct.util.page.PageRequest;
import com.tbxx.wpct.util.page.PageResult;
import com.tbxx.wpct.util.page.PageUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
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

    public static Page page;

    @Override
    public PageResult splitpage(PayInfoVo vo) {
        PageRequest pageRequest = new PageRequest(vo.getPageNum(), vo.getPageSize());
        return PageUtil.getPageResult(getPageInfo(pageRequest,vo),page);
    }

    private PageInfo<?> getPageInfo(PageRequest pageRequest, PayInfoVo vo) {
        int pageNum = pageRequest.getPageNum();
        int pageSize = pageRequest.getPageSize();
        //设置分页数据
        page = PageHelper.startPage(pageNum,pageSize);
        List<PayInfo> res = payInfoMapper.selectCondition(vo);
        return new PageInfo<>(res);
    }
}
