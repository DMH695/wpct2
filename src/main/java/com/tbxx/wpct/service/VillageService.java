package com.tbxx.wpct.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.tbxx.wpct.dto.Result;
import com.tbxx.wpct.entity.Village;
import org.springframework.transaction.annotation.Transactional;

public interface VillageService extends IService<Village> {

    @Transactional
    Result getTree(int pageSize, int pageNum);

}
