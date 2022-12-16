package com.tbxx.wpct.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * @Author ZXX
 * @ClassName WechatUser
 * @Description
 * @DATE 2022/10/1 16:50
 */

@Data
@TableName("tb_wechat_user")
public class WechatUser implements Serializable {

    /**
     * 主键：用户id
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /**
     * openid
     */
    private String openid;

    /**
     * 微信用户昵称
     */
    private String nickname;


    //
    /**
     * 姓名
     */
    private  String name;

    /**
     * 手机号
     */
    private  String number;

    /**
     * 身份证
     */
    private String pid;

    /**
     * 用户的房屋信息 （多对多）
     */
    @TableField(exist = false)
    private List<BuildInfo> buildInfoList;


}
