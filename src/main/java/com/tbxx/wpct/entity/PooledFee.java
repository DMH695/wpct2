package com.tbxx.wpct.entity;

import com.alibaba.excel.annotation.ExcelIgnore;
import com.alibaba.excel.annotation.ExcelProperty;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * @Author ZXX
 * @ClassName PooledFee
 * @Description TODO
 * @DATE 2022/10/10 19:29
 */

@Data
@TableName("tb_pooled_fee")
public class PooledFee implements Serializable {
    /**
     * 主键
     */
    @TableId(type = IdType.AUTO)
    private int id;
    /**
     * 小区号
     */
    @ExcelProperty("小区号")
    private String villageName;
    /**
     * 楼栋号
     */
    @ExcelProperty("楼栋号")
    private String buildName;
    /**
     * 房间号
     */
    @ExcelProperty("房间号")
    private String roomName;
    /**
     * 公电梯费
     */
    @ExcelProperty("公电梯费")
    private Integer liftFee;
    /**
     * 公水费
     */
    @ExcelProperty("公水费")
    private Integer waterFee;
    /**
     * 公电费
     */
    @ExcelProperty("公电费")
    private Integer electricityFee;
    /**
     * 创建时间
     */
    @ExcelProperty("创建时间")
    private LocalDateTime createTime;


}
