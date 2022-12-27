package com.tbxx.wpct.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

@Data
@TableName(value = "tb_room")
public class Room {

    @TableId(value = "id", type = IdType.AUTO)
    private int id;

    /**
     * 所属楼号id
     */
    @TableField(value = "build_id")
    private int buildId;

    /**
     * 房号
     */
    private String name;

}
