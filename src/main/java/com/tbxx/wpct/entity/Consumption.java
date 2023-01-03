package com.tbxx.wpct.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * @ClassName ConsumptionService * @Description
 * @Author ZQB
 * @Date 20:43 2022/10/8
 * @Version 1.0
 **/

@Data
@TableName(value = "tb_consumption")
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Consumption implements Serializable {
    /**
     * 房屋id
     **/
    private String buildId;

    /**
     * 面积核准单价
     */
    private Integer areaFee;

    /**
     * 面积
     */
    private double area;

    /**
     * 核准面积
     */
    private double limitArea;

    /**
     * 超出面积
     */
    private double overArea;

    /**
     * 超出面积单价
     */
    private Integer overareaFee;

    /**
     * 物业单价
     */
    private Integer property;

    /**
     * 物业费
     */
    private Integer propertyFee;

    /**
     * 押金
     */
    private Integer deposit;


    /**
     * 水费
     */
    private Integer waterFee;

    /**
     * 电费
     */
    private Integer electricity;

    /**
     * 气费
     */
    private Integer gasFee;

    /**
     * 停车费
     */
    private Integer carFee;

    /**
     * 收回不符合条件疫情减免金额
     */
    private Integer aFee;

    /**
     * 应收不符合条件租金
     */
    private Integer bFee;

    /**
     * 应收应退租金
     */
    private Integer cFee;

    /**
     * 应收应退物业费
     */
    private Integer dFee;

    /**
     * 优惠
     */
    private Integer discount;

    /**
     * 公电梯费
     */
    private Integer liftFee;
    /**
     * 公水费
     */
    private Integer gwaterFee;
    /**
     * 公电费
     */
    private Integer electricityFee;

    /**
     * 月金额
     */
    private Integer monthCost;

    /**
     * 其它费用
     */
    private Integer otherFee;

    /**
     * 租金
     */
    private Integer rent;


}
