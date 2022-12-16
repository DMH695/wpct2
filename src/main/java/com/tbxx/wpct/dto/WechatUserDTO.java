package com.tbxx.wpct.dto;

import lombok.Data;

/**
 * @ClassName WechatUserDTO
 * @Description TODO
 * @Author ZXX
 * @DATE 2022/10/14 14:08
 */

@Data
public class WechatUserDTO {

    /**
     * openID
     */
    private String openid;

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
}
