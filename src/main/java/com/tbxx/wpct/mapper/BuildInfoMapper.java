package com.tbxx.wpct.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.tbxx.wpct.entity.BuildInfo;

import java.util.List;

/**
 * @Author ZXX
 * @InterfaceName BuildInfoMapper
 * @Description TODO
 * @DATE 2022/10/8 17:24
 */

public interface BuildInfoMapper extends BaseMapper<BuildInfo> {
    void insertBuildInfos(List<BuildInfo> buildInfoList);

    BuildInfo allBuildInfo(String openId);
}
