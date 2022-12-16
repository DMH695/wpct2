package com.tbxx.wpct.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

/**
 * @ClassName SysPerm * @Description
 * @Author ZQB
 * @Date 12:39 2022/9/30
 * @Version 1.0
 **/
@Data
@TableName("sys_permission")
public class SysPerm implements Serializable {

    @TableId(type = IdType.AUTO)
    private Integer Id;

    /**
     * 菜单模块类型
     */
    private String menuCode;
    /**
     * 该类型中功能名字
     */
    private String menuName;
    /**
     * 权限码
     */
    private String permissionCode;
    /**
     * 权限名字
     */
    private String permissionName;
    /**
     * 必选标识
     */
    private String requiredPermission;


    @TableField(exist = false)
    public List<Map<Object,Object>> permissions;

    @TableField(exist = false)
    public List<Map<Object, Object>> menus;

}
