package com.tbxx.wpct.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.github.pagehelper.PageInfo;
import com.tbxx.wpct.dto.PayInfoVo;
import com.tbxx.wpct.entity.PayInfo;

import java.util.List;

/**
 * @Author ZXX
 * @InterfaceName PayInfoService
 * @Description TODO
 * @DATE 2022/10/9 19:19
 */

public interface PayInfoService extends IService<PayInfo> {
    PageInfo splitpage(int pageNum, int pageSize,PayInfoVo vo);

}
