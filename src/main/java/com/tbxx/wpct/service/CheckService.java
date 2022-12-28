package com.tbxx.wpct.service;


import com.baomidou.mybatisplus.extension.service.IService;
import com.tbxx.wpct.dto.Result;
import com.tbxx.wpct.entity.PayInfo;
import com.tbxx.wpct.util.page.PageResult;
import org.springframework.transaction.annotation.Transactional;

/**
 * @Author ZXX
 * @InterfaceName CheckServiceription TODO
 * @DATE 2022/10/9 18:39
 */

public interface CheckService extends IService<PayInfo> {

    @Transactional
    Result addCheck(PayInfo payinfo);


    @Transactional
    Result checklist(String openid);

    @Transactional
    PageResult checksList(int pageNum, int pageSize, String month);

    @Transactional
    Result deleteCheck(String checkid,String orderId);

    Result checkUpdate(PayInfo payinfo);

    Result checkFee(String checkId);
}
