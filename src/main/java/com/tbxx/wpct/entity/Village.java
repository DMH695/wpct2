package com.tbxx.wpct.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

@Data
@TableName("tb_village")
public class Village {

    /**
     * 主键
     */
    @TableId(type = IdType.AUTO)
    private int id;

    /**
     * 小区名
     */
    private String name;

}
