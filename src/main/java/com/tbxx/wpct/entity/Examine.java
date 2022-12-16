package com.tbxx.wpct.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;


@Data
@TableName("tb_examine")
public class Examine implements Serializable {

    /**
     * 主键
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;
    /**
     * openid
     */
    private String openid;
    /**
     * 处理信息
     */
    private String examineMsg;
    /**
     * 处理信息
     */
    private String resolveMsg;
    /**
     * 处理状态
     */
    private String examineStatus;
    /**
     * 提交时间
     */
    private LocalDateTime commitTime;
    /**
     * 处理时间
     */
    private LocalDateTime examineTime;

    /**
     * 微信用户具体信息
     */
    @TableField(exist = false)
    private WechatUser wechatUser;

    /**
     * 处理列表需要小区名 楼号 房号
     */
    @TableField(exist = false)
    private List<BuildInfo> buildInfo;
}
