package com.tbxx.wpct.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.tbxx.wpct.config.WxMsgConfig;
import com.tbxx.wpct.dto.Result;
import com.tbxx.wpct.dto.WechatUserDTO;
import com.tbxx.wpct.entity.Consumption;
import com.tbxx.wpct.entity.PayInfo;
import com.tbxx.wpct.entity.WechatUser;
import com.tbxx.wpct.entity.wxpush.WxMsgTemplateHasten;
import com.tbxx.wpct.mapper.ConsumptionMapper;
import com.tbxx.wpct.mapper.PayInfoMapper;
import com.tbxx.wpct.mapper.WechatUserMapper;
import com.tbxx.wpct.service.SendMsgService;
import com.tbxx.wpct.util.wx.WxSendMsgUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @ClassName SendMsgServiceImpl
 * @Description TODO
 * @Author ZXX
 * @DATE 2022/10/12 16:23
 */


@Slf4j
@Service
public class SendMsgServiceImpl implements SendMsgService {

    @Resource
    private RestTemplate restTemplate;

    @Resource
    private WxSendMsgUtil wxSendMsgUtil;

    @Resource
    private PayInfoMapper payInfoMapper;
    @Resource
    private ConsumptionMapper consumptionMapper;

    @Resource
    private WechatUserMapper wechatUserMapper;


    /**
     * 参数拼接
     */
    public WxMsgConfig getMsgConfig(PayInfo payInfo, WechatUser wechatUser) {
        String payinfoId = payInfo.getPayinfoId();
        QueryWrapper<Consumption> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("build_id", payinfoId);
        Consumption consumption = consumptionMapper.selectOne(queryWrapper);


        WxMsgTemplateHasten wxMsgTemplateHasten = new WxMsgTemplateHasten();
        wxMsgTemplateHasten.setFirst(payInfo.getVillageName() + "-"
                + payInfo.getBuildNo() + "-"
                + payInfo.getRoomNo() +
                "缴费提醒");
        /*房屋号*/
        wxMsgTemplateHasten.setKeyword1(payInfo.getVillageName() + "-"
                + payInfo.getBuildNo() + "-"
                + payInfo.getRoomNo());
        /*缴费人*/
        wxMsgTemplateHasten.setKeyword2(wechatUser.getName());
        /*缴费类型*/
        wxMsgTemplateHasten.setKeyword3("总费用");
        /*缴费状态*/
        wxMsgTemplateHasten.setKeyword4("未支付");
        /*合计金额*/
        BigDecimal bigDecimal = new BigDecimal(consumption.getMonthCost());
        wxMsgTemplateHasten.setKeyword5(String.valueOf(bigDecimal.divide(new BigDecimal(100))));
        wxMsgTemplateHasten.setRemark("请及时缴交费用~");


        /*消息推送配置参数拼接*/
        WxMsgConfig wxMsgConfig = new WxMsgConfig();
        wxMsgConfig.setTouser(wechatUser.getOpenid());
        wxMsgConfig.setTemplate_id("QgLoZpp1KWNskam2jclpxXmmSu4nhVZkv8bPU9wEqS4");
        wxMsgConfig.setData(wxMsgTemplateHasten);
        return wxMsgConfig;
    }


    /**
     * 发送请求
     */
    public JSONObject postData(String url, WxMsgConfig param) {
        MediaType type = MediaType.parseMediaType("application/json; charset=UTF-8");
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(type);
        HttpEntity<WxMsgConfig> httpEntity = new HttpEntity<>(param, headers);
        JSONObject jsonResult = restTemplate.postForObject(url, httpEntity, JSONObject.class);
        return jsonResult;
    }


    /**
     * 催缴
     */
    @Override
    public void sendHasten(WechatUserDTO wechatUserDTO) throws InterruptedException {
        QueryWrapper<WechatUser> queryWrapper1 = new QueryWrapper<>();
        queryWrapper1.eq("openid", wechatUserDTO.getOpenid());
        WechatUser wechatUser = wechatUserMapper.selectOne(queryWrapper1);

        QueryWrapper<PayInfo> queryWrapper2 = new QueryWrapper<>();
        queryWrapper2.eq("village_name", wechatUserDTO.getVillageName()).eq("build_no", wechatUserDTO.getBuildNo())
                .eq("room_no", wechatUserDTO.getRoomNo());

        List<PayInfo> payInfos = payInfoMapper.selectList(queryWrapper2);

        for (PayInfo pList : payInfos) {
            WxMsgConfig requestData = this.getMsgConfig(pList, wechatUser);

            log.info("推送消息请求参数：{}", JSON.toJSONString(requestData));

            String url = "https://api.weixin.qq.com/cgi-bin/message/template/send?access_token=" + wxSendMsgUtil.getAccessToken();
            log.info("推送消息请求地址：{}", url);
            JSONObject responseData = postData(url, requestData);
            log.info("推送消息返回参数：{}", JSON.toJSONString(responseData));

            Integer errorCode = responseData.getInteger("errcode");
            String errorMessage = responseData.getString("errmsg");
            if (errorCode == 0) {
                log.info("推送消息发送成功");
            } else {
                log.info("推送消息发送失败,errcode：{},errorMessage：{}", errorCode, errorMessage);
            }
        }

    }


}
