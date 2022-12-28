package com.tbxx.wpct.dto.model;

import com.alibaba.excel.annotation.ExcelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class HouseInfoImport {

    /**
     * 小区名
     */
    @ExcelProperty("小区")
    private String villageName;
    /**
     * 楼号
     */
    @ExcelProperty("楼号")
    private String buildNo;
    /**
     * 房号
     */
    @ExcelProperty("房号")
    private String roomNo;
    /**
     * 姓名
     */
    @ExcelProperty("姓名")
    private String name;
    /**
     * 电话
     */
    @ExcelProperty("联系电话")
    private String number;
    /**
     * 户口所在地
     */
    @ExcelProperty("户口所在地")
    private String resident;
    /**
     * 房屋类型
     */
    @ExcelProperty("房屋类型")
    private String houseType;
    /**
     * 车类
     */
    @ExcelProperty("车辆类型")
    private String carType;
    /**
     * 与房屋的关系
     */
    @ExcelProperty("与房屋的关系")
    private String relation;
    /**
     * 符合条件人数
     */
    @ExcelProperty("符合条件人数")
    private String conditionNumber;
    /**
     * 其中低保人数
     */
    @ExcelProperty("其中低保人数")
    private String personNumber;
    /**
     * 租金
     */
    @ExcelProperty("租金")
    private Integer rent;
    /**
     * 面积
     */
    @ExcelProperty("面积")
    private double area;
    /**
     * 核准面积
     */
    @ExcelProperty("核准面积")
    private double limitArea;
    /**
     * 超出面积
     */
    @ExcelProperty("超出面积")
    private double overArea;
    /**
     * 超出面积单价
     */
    @ExcelProperty("超出面积单价")
    private Integer overareaFee;
    /**
     * 物业单价
     */
    @ExcelProperty("物业单价")
    private Integer property;
    /**
     * 物业费
     */
    @ExcelProperty("物业费")
    private Integer propertyFee;
    /**
     * 押金
     */
    @ExcelProperty("押金")
    private Integer deposit;
    /**
     * 水费
     */
    @ExcelProperty("水费")
    private Integer waterFee;
    /**
     * 电费
     */
    @ExcelProperty("电费")
    private Integer electricity;
    /**
     * 气费
     */
    @ExcelProperty("气费")
    private Integer gasFee;
    /**
     * 停车费
     */
    @ExcelProperty("停车费")
    private Integer carFee;
    /**
     * 收回不符合条件疫情减免金额
     */
    @ExcelProperty("收回不符合条件疫情减免金额")
    private Integer aFee;
    /**
     * 应收不符合条件租金
     */
    @ExcelProperty("应收不符合条件租金")
    private Integer bFee;
    /**
     * 应收应退租金
     */
    @ExcelProperty("应收应退租金")
    private Integer cFee;
    /**
     * 应收应退物业费
     */
    @ExcelProperty("应收应退物业费")
    private Integer dFee;
    /**
     * 优惠
     */
    @ExcelProperty("优惠")
    private Integer discount;
    /**
     * 备注
     */
    @ExcelProperty("备注")
    private String remarks;
}
