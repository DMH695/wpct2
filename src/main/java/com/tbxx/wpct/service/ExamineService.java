package com.tbxx.wpct.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.tbxx.wpct.dto.Result;
import com.tbxx.wpct.entity.Examine;
import com.tbxx.wpct.util.page.PageResult;

/**
 * @Author ZXX
 * @InterfaceName ExamineService
 * @Description TODO
 * @DATE 2022/10/10 17:03
 */

public interface ExamineService extends IService<Examine> {
    Result addExamine(Examine examine);
    
    Result userExamineList(String openid);

    PageResult listExamine(int pageNum, int pageSize);


    Result soluExamine(Integer id, String openid, String resolveMsg);
}
