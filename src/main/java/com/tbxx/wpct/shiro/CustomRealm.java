package com.tbxx.wpct.shiro;

import com.tbxx.wpct.dto.UserDTO;
import com.tbxx.wpct.entity.SysUser;
import com.tbxx.wpct.mapper.SysUserMapper;
import com.tbxx.wpct.service.SysUserService;
import com.tbxx.wpct.service.impl.SysUserServiceImpl;
import com.tbxx.wpct.util.UserHolder;

import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.apache.shiro.util.SimpleByteSource;
import org.springframework.beans.factory.annotation.Autowired;

import javax.annotation.Resource;
import java.util.Set;


@Slf4j
public class CustomRealm extends AuthorizingRealm {
    @Autowired
    private SysUserServiceImpl sysUserService;

    @Resource
    private SysUserMapper sysUserMapper;

    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principals) {
        log.debug("======doAuthorizationInfo授权=======");

//        UserDTO user = UserHolder.getUser();
//        Integer roleId = user.getRoleId();

//       Set<String> permissions = sysUserMapper.findPermsListByRoleId(roleId.toString());
        Set<String> permissions = sysUserMapper.findPermsListByRoleId(String.valueOf(1));

        log.debug("获得权限===>{}",permissions.iterator().toString());

        SimpleAuthorizationInfo authorizationInfo = new SimpleAuthorizationInfo();
        authorizationInfo.setStringPermissions(permissions);
        return authorizationInfo;

    }

    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken token) throws AuthenticationException {
        log.debug("======doGetAuthenticationInfo认证=======");

        //从传过来的token获取到的用户名
        String username = (String) token.getPrincipal();
        //根据用户名从数据库获得sysUser对象
        SysUser sysUser = sysUserService.QueryUser(username);


        log.debug("====认证的sysUser===="+sysUser);


            SimpleAuthenticationInfo simpleAuthenticationInfo = new SimpleAuthenticationInfo(
                    username,
                    sysUser.getPassword(),
                    new SimpleByteSource(sysUser.getSalt()),
                    this.getName());

//            Session session = SecurityUtils.getSubject().getSession();
//            session.setAttribute("USER_SESSION",sysUser);

            return simpleAuthenticationInfo;



    }

}
