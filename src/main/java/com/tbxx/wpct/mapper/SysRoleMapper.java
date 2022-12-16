package com.tbxx.wpct.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.tbxx.wpct.dto.Result;
import com.tbxx.wpct.entity.SysRole;
import com.tbxx.wpct.util.OneToMore;

import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public interface SysRoleMapper extends BaseMapper<SysRole> {

    public void addRoleAndPerm(SysRole sysRole);

    public void deleteRoleAndPerm(Integer ID);


    /**
     * 角色列表
     */
    List<OneToMore> listRole();

    /**
     * 角色对应权限ID集合
     */
    HashSet<Integer> RolePerm(int roleId);
    /**
     * 批量增加 角色有用权限
     */
    void  batchAddRolePerm(int roleId,HashSet<Integer> perms);

    /**
     * 批量删除 角色无用权限
     */
    void batchdeleteRolePerm(int roleId, HashSet<Integer> perms);



}
