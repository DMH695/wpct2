package com.tbxx.wpct.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PayInfo2 implements Serializable {
    /**
     * 缴费id
     */
    private String payinfoId;

    /**
     * 小区名称
     */
    private String villageName;

    /**
     * 楼号
     */
    private String buildNo;

    /**
     * 房号
     */
    private String roomNo;

    /**
     * 姓名
     */
    private String name;

    /**
     * 身份证
     */
    private String pid;

    /**
     * 电话
     */
    private String number;

    /**
     * 车类
     */
    private String car;

    /**
     * 车牌号
     */
    private String carNumber;

    /**
     * 户口
     */
    private String resident;

    /**
     * 符合条件人数
     */
    private String conditionNumber;

    /**
     * 其中低保人数
     */
    private String personNumber;

    /**
     * 缴费状况
     */
    private String payStatus;

    /**
     * 房屋类型
     */
    private String houseType;

    /**
     * 缴费开始时间
     */
    private String payBeginTime;

    /**
     * 缴费结束时间
     */
    private String payEndTime;

    /**
     * 后台人员修改时间
     */
    private String updateTime;

    /**
     * 备注
     */
    private String remarks;

    /**
     * 操作人员记录
     */
    private String updateUser;

    /**
     * 订单号
     */
    private String OrderNo;

    /**
     * 与房屋的关系
     */
    private String relation;



    //@TableField(exist = false)
    private WechatUser wechatUser;



    /**
     * 计算
     */
    //@TableField(exist = false)
    private Consumption consumption;
}
