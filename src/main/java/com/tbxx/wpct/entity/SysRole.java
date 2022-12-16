package com.tbxx.wpct.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;

/**
 * @ClassName SysRole * @Description
 * @Author ZQB
 * @Date 20:24 2022/9/29
 * @Version 1.0
 **/
@Data
@TableName("sys_role")
public class SysRole implements Serializable{
    /**
     * 主键
     */
    @TableId(value = "id", type = IdType.AUTO)
    private int  roleId;

    private String roleName;
    private LocalDateTime createTime;
    private LocalDateTime updateTime;


    /**
     * 权限名集合
     */
    @TableField(exist = false)
    private List<String> permsList;
    /**
     * 权限名ID集合
     */
    @TableField(exist = false)
    private List<Integer> permsIDList;

}
