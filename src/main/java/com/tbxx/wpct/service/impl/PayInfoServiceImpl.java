package com.tbxx.wpct.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.tbxx.wpct.dto.PayInfoVo;
import com.tbxx.wpct.entity.Consumption;
import com.tbxx.wpct.entity.PayInfo;
import com.tbxx.wpct.mapper.ConsumptionMapper;
import com.tbxx.wpct.mapper.PayInfoMapper;
import com.tbxx.wpct.service.PayInfoService;
import com.tbxx.wpct.util.page.PageRequest;
import com.tbxx.wpct.util.page.PageResult;
import com.tbxx.wpct.util.page.PageUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
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
    @Resource
    ConsumptionMapper consumptionMapper;

    public static Page page;

    @Override
    public PageResult splitpage(int pageNum, int pageSize, PayInfoVo vo) {
        PageRequest pageRequest = new PageRequest(pageNum, pageSize);
        return PageUtil.getPageResult(getPageInfo(pageRequest,vo),page);
    }

    private PageInfo<?> getPageInfo(PageRequest pageRequest, PayInfoVo vo) {
        List<PayInfo> res;
        int pageNum = pageRequest.getPageNum();
        int pageSize = pageRequest.getPageSize();
        //设置分页数据
        page = PageHelper.startPage(pageNum, pageSize);
        if (vo == null) {
            return new PageInfo<>(query().list());
        } else {
             res = query()
                    .like(vo.getVillageName() != null && !vo.getVillageName().equals(""), "village_name", vo.getVillageName())
                    .eq(vo.getBuildNo() != null && !vo.getBuildNo().equals(""), "build_no", vo.getBuildNo())
                    .like(vo.getName() != null && !vo.getName().equals(""), "name", vo.getName())
                    .eq(vo.getPayStatus() != null && !vo.getPayStatus().equals("")&&vo.getPayStatus().equals("已缴"), "pay_status", vo.getPayStatus())
                     .ne(vo.getPayStatus() != null && !vo.getPayStatus().equals("")&&!vo.getPayStatus().equals("已缴"),"pay_status","已缴")
                    .ge(vo.getPayBeginTime() != null && vo.getPayEndTime() == null, "pay_begin_time", vo.getPayBeginTime())
                    .le(vo.getPayEndTime() != null&&vo.getPayBeginTime() == null, "pay_end_time", vo.getPayEndTime())
                     .between(vo.getPayBeginTime() != null && vo.getPayEndTime() != null,"pay_begin_time",vo.getPayBeginTime(),vo.getPayEndTime())
                     .between(vo.getPayBeginTime() != null && vo.getPayEndTime() != null,"pay_end_time",vo.getPayBeginTime(),vo.getPayEndTime()).list();
             }
        if(res != null){
            for(int i = 0;i<res.size();i++){
                PayInfo payInfo = res.get(i);
                QueryWrapper queryWrapper = new QueryWrapper<>();
                queryWrapper.in("build_id",payInfo.getPayinfoId());
                Consumption consumption = consumptionMapper.selectOne(queryWrapper);
                payInfo.setConsumption(consumption);
            }
        }return new PageInfo<>(res);
    }
}