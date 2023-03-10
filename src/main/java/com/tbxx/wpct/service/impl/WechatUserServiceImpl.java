package com.tbxx.wpct.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.extension.conditions.query.QueryChainWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.tbxx.wpct.dto.Result;
import com.tbxx.wpct.dto.SR;
import com.tbxx.wpct.entity.BuildInfo;
import com.tbxx.wpct.entity.PayInfo;
import com.tbxx.wpct.entity.WechatUser;
import com.tbxx.wpct.mapper.BuildInfoMapper;
import com.tbxx.wpct.mapper.WechatUserMapper;
import com.tbxx.wpct.service.WechatUserService;
import com.tbxx.wpct.util.page.PageRequest;
import com.tbxx.wpct.util.page.PageResult;
import com.tbxx.wpct.util.page.PageUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import springfox.documentation.builders.BuilderDefaults;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.tbxx.wpct.util.constant.SysConstant.*;

/**
 * @Author ZXX
 * @ClassName WechatUserServiceImpl
 * @Description
 * @DATE 2022/10/1 17:13
 */

@Slf4j
@Service
public class WechatUserServiceImpl extends ServiceImpl<WechatUserMapper, WechatUser> implements WechatUserService {


    @Resource
    private BuildInfoMapper buildInfoMapper;

    @Resource
    private WechatUserMapper wechatUserMapper;

    public static Page page;



    /**
     * 注册
     */
    @Override
    public Result register(WechatUser wechatUser) {
        String phoneNumber = wechatUser.getNumber();
        //String pid = wechatUser.getPid();
        if (!phoneNumber.matches(PHONE_REGEX)) {
            return Result.fail("手机号格式有误，请重新输入~");
        }
        /*if (!(pid.matches(PID_REGEX18) || pid.matches(PID_REGEX15))) {
            return Result.fail("身份证格式有误，请重新输入~");
        }*/
        log.warn("手机身份证格式验证");

        //用户注册
        List<BuildInfo> buildInfoList = wechatUser.getBuildInfoList();
        buildInfoMapper.insertBuildInfos(buildInfoList);
        //wechatUser表插入
        UpdateWrapper<WechatUser> updateWrapper = new UpdateWrapper<>();
        updateWrapper.eq("openid", wechatUser.getOpenid());
        WechatUser user = new WechatUser();
        user.setNumber(phoneNumber);
        //user.setPid(pid);
        user.setName(wechatUser.getName());
        wechatUserMapper.update(user, updateWrapper);

        return Result.ok("1");
    }


    /**
     * 查询用户信息
     */
    @Override
    public Result getInfo(String openid) {
        QueryWrapper<WechatUser> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("openid", openid);
        WechatUser user = wechatUserMapper.selectOne(queryWrapper);
        String number = user.getNumber();
        String name = user.getName();
        Map map = new HashMap();
        map.put("name", name);
        map.put("number", number);
        return Result.ok(map);
    }

    /**
     * 后台展示微信用户信息
     * 小区名+房号+楼号+姓名+电话+openid（未注册只有openid和昵称 更没有房屋 排除此类用户）
     * 查出所有已经注册的用户（pid不为空） 用openid去查房屋表 用户和房屋是一对多的关系
     */
    @Override
    public PageResult splitpage(int pageNum, int pageSize) {
        PageRequest pageRequest = new PageRequest(pageNum, pageSize);
        return PageUtil.getPageResult(getPageInfo(pageRequest),page);
    }
    private PageInfo<?> getPageInfo(PageRequest pageRequest) {
        int pageNum = pageRequest.getPageNum();
        int pageSize = pageRequest.getPageSize();
        //设置分页数据
        page = PageHelper.startPage(pageNum,pageSize);
        List<WechatUser> wechatUsers = baseMapper.selectList(null);
        for(int i = 0;i<wechatUsers.size();i++){
            String openid = wechatUsers.get(i).getOpenid();
            QueryWrapper<BuildInfo> queryWrapper = new QueryWrapper<>();
            queryWrapper.eq("openid",openid);
            List<BuildInfo> buildInfoList = buildInfoMapper.selectList(queryWrapper);
            wechatUsers.get(i).setBuildInfoList(buildInfoList);
        }
        return new PageInfo<>(wechatUsers);
    }

    @Override
    public WechatUser getByOpenid(String openid) {
        return wechatUserMapper.getByOpenid(openid);
    }
    @Override
    public Result registerVerify(PayInfo payInfo) {
        WechatUser wechatUser = wechatUserMapper.getNameNumberOrderNo(payInfo);
        if (wechatUser == null){
            return Result.fail("无对应数据");
        }
        return Result.ok(wechatUser);
    }

    @Override
    public Result buildList(String openId) {
        BuildInfo buildInfo = buildInfoMapper.allBuildInfo(openId);
        return Result.ok(buildInfo);
    }
}
