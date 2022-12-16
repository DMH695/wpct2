package com.tbxx.wpct.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.tbxx.wpct.dto.Result;
import com.tbxx.wpct.entity.Examine;

/**
 * @Author ZXX
 * @InterfaceName ExamineService
 * @Description TODO
 * @DATE 2022/10/10 17:03
 */

public interface ExamineService extends IService<Examine> {
    Result addExamine(Examine examine);
    
    Result userExamineList(String openid);

    Result listExamine();


    Result soluExamine(Integer id, String openid, String resolveMsg);
}
