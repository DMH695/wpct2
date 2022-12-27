package com.tbxx.wpct.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

@Data
@TableName(value = "tb_build")
public class Build {

    @TableId(value = "id", type = IdType.AUTO)
    private int id;

    /**
     * 所属小区id
     */
    @TableField(value = "village_id")
    private int villageId;

    /**
     * 楼号
     */
    private String name;

}
