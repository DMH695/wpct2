package com.tbxx.wpct.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.tbxx.wpct.entity.BuildInfo;
import com.tbxx.wpct.entity.WechatUser;
import org.apache.ibatis.annotations.Mapper;
import springfox.documentation.builders.BuilderDefaults;

import java.util.List;

/**
 * @Author ZXX
 * @InterfaceName WechatUserMapper
 * @Description
 * @DATE 2022/10/1 17:15
 */
@Mapper
public interface WechatUserMapper extends BaseMapper<WechatUser> {
    WechatUser getByOpenid(String openid);
}
