package com.tbxx.wpct.task;

import com.tbxx.wpct.dto.WechatUserDTO;
import com.tbxx.wpct.mapper.CheckMapper;
import com.tbxx.wpct.service.impl.SendMsgServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.List;


@Slf4j
@Component
public class SendMsgTask {

    @Autowired
    private SendMsgServiceImpl sendMsgService;

    @Resource
    private CheckMapper checkMapper;

    //每天中午12点触发
    @Scheduled(cron = "0 0 12 * * ?")
    public void sendMsg() throws InterruptedException {
        List<WechatUserDTO> wechatUserDTOS = checkMapper.sendMsg();
        try {
            for (WechatUserDTO wechatUserDTOS1 : wechatUserDTOS) {
                sendMsgService.sendHasten(wechatUserDTOS1);
            }
        } catch (Exception e) {
            e.printStackTrace();
            log.error("提醒缴费消息推送异常");
        }
    }


}
