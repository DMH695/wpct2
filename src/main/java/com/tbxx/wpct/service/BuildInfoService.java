package com.tbxx.wpct.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.tbxx.wpct.dto.Result;
import com.tbxx.wpct.entity.BuildInfo;

/**
 * @Author ZXX
 * @InterfaceName BuildInfoService
 * @Description TODO
 * @DATE 2022/10/8 17:25
 */

public interface BuildInfoService extends IService<BuildInfo> {


    Result removeBuild(int buildid);

    Result updateBuild(BuildInfo buildid);

    Result buildList(String openid);

    Result addBindBuild(BuildInfo buildInfo);
}
