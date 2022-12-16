package com.tbxx.wpct.service;

import com.tbxx.wpct.dto.Result;
import com.tbxx.wpct.dto.WechatUserDTO;
import com.tbxx.wpct.entity.PayInfo;
import com.tbxx.wpct.entity.WechatUser;

import java.util.List;

/**
 * @InterfaceName SendMsgService
 * @Description TODO
 * @Author ZXX
 * @DATE 2022/10/12 16:23
 */

public interface SendMsgService {
   void sendHasten(WechatUserDTO wechatUserDTO) throws InterruptedException;
}
