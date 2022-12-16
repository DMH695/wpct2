package com.tbxx.wpct.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.tbxx.wpct.dto.Result;
import com.tbxx.wpct.entity.BuildInfo;
import com.tbxx.wpct.entity.Deposit;

/**
 * @ClassName DepositService
 * @Description TODO
 * @Author ZXX
 * @DATE 2022/10/14 14:54
 */

public interface DepositService extends IService<Deposit>  {
    Result jsapiPay(BuildInfo buildInfo, Integer money, String openid) throws Exception;

    Result DepositList(int pageNum);

    Result setMoney(Integer money,BuildInfo buildInfo);
}

