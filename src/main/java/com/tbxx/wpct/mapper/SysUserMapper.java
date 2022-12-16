package com.tbxx.wpct.mapper;

import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.tbxx.wpct.dto.UserDTO;
import com.tbxx.wpct.entity.SysUser;
import com.tbxx.wpct.util.UserList;

import java.util.Set;

import java.util.List;
import java.util.Set;

/**
 * @Author ZXX
 * @InterfaceName SysUserMapper
 * @Description
 * @DATE 2022/9/29 18:37
 */


public interface SysUserMapper extends BaseMapper<SysUser> {

    Set<String> findPermsListByRoleId(String roleId);

    List<UserList> findUserList();

}
