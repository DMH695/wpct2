package com.tbxx.wpct.service.impl;

import com.alibaba.excel.EasyExcel;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.conditions.query.QueryChainWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.tbxx.wpct.dto.PayInfoVo;
import com.tbxx.wpct.dto.Result;
import com.tbxx.wpct.dto.model.HouseInfoImport;
import com.tbxx.wpct.entity.BuildInfo;
import com.tbxx.wpct.entity.Consumption;
import com.tbxx.wpct.entity.PayInfo;
import com.tbxx.wpct.entity.PooledFee;
import com.tbxx.wpct.mapper.BuildInfoMapper;
import com.tbxx.wpct.service.BuildInfoService;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @Author ZXX
 * @ClassName BuildInfoServiceImpl
 * @Description TODO
 * @DATE 2022/10/8 17:26
 */

@Slf4j
@Service
public class BuildInfoServiceImpl extends ServiceImpl<BuildInfoMapper, BuildInfo> implements BuildInfoService {

    @Autowired
    @Lazy
    private CheckServiceImpl checkService;

    @Autowired
    @Lazy
    private ConsumptionServiceImpl consumptionService;


    /**
     * 增加绑定
     */
    @Override
    public Result addBindBuild(BuildInfo buildInfo) {
        baseMapper.insert(buildInfo);
        return Result.ok("添加成功！");
    }

    @SneakyThrows
    @Override
    public void getTemplate(HttpServletResponse response) {
        List<HouseInfoImport> houseInfoImports = new ArrayList<>();
        HouseInfoImport houseInfoImport = HouseInfoImport.builder().villageName("XX小区").buildNo("1-1")
                .roomNo("201").name("小王").number("10000").resident("XX地区").houseType("商品房")
                        .carType("汽车").relation("户主").build();
        houseInfoImports.add(houseInfoImport);
        //设置头属性  设置文件名称
        response.setHeader("Content-Disposition", "attachment;filename=template.xlsx");
        EasyExcel.write(response.getOutputStream())
                .head(HouseInfoImport.class)
                .sheet("模板")
                .doWrite(houseInfoImports);
    }

    @SneakyThrows
    @Override
    @Transactional
    public Result importData(MultipartFile file) {
        List<HouseInfoImport> houseInfoImports = EasyExcel.read(file.getInputStream()).head(HouseInfoImport.class).sheet().doReadSync();
        int num = 0;
        for (HouseInfoImport houseInfoImport : houseInfoImports) {
            checkService.addCheck(houseInfoImport2PayInfo(houseInfoImport));
            num++;
        }
        return Result.ok(num);
    }

    // TODO 日期筛选
    @SneakyThrows
    @Override
    @Transactional
    public void export2Excel(PayInfoVo vo, HttpServletResponse response) {
        String villageName = vo.getVillageName();
        String buildNo = vo.getBuildNo();
        String payStatus = vo.getPayStatus();
        String name = vo.getName();
        List<PayInfo> payInfos = checkService.query()
                .like(villageName!=null && !villageName.equals(""),"village_name", villageName)
                .like(buildNo!=null && !buildNo.equals(""),"build_no", buildNo)
                .eq(payStatus!= null && !payStatus.equals(""),"pay_status", payStatus).list();
        response.setHeader("Content-Disposition", "attachment;filename=template.xlsx");
        List<HouseInfoImport> houseInfoImports = new ArrayList<>();
        List<String> checkIds = new ArrayList<>();
        for (PayInfo payInfo : payInfos) {
            checkIds.add(payInfo.getPayinfoId());
        }
        List<Consumption> consumptions = consumptionService.query().in("build_id", checkIds).list();
        Map<String,Consumption> map = new ConcurrentHashMap<>();
        for (Consumption consumption : consumptions) {
            map.put(consumption.getBuildId(),consumption);
        }
        for (PayInfo payInfo : payInfos) {
            payInfo.setConsumption(map.get(payInfo.getPayinfoId()));
            houseInfoImports.add(payInfo2HouseInfoImport(payInfo));
        }
        EasyExcel.write(response.getOutputStream())
                .head(HouseInfoImport.class)
                .sheet(villageName + "#" + buildNo + "#" + payStatus + "#" + name)
                .doWrite(houseInfoImports);
    }

    @Override
    public Result removeBuild(int buildid) {
        removeById(buildid);
        return Result.ok("解除绑定");
    }

    @Override
    public Result updateBuild(BuildInfo buildInfo) {
        QueryWrapper<BuildInfo> queryWrapper=new QueryWrapper<>();
        queryWrapper.eq("id",buildInfo.getId());
        update(buildInfo,queryWrapper);
        return Result.ok("修改成功");
    }


    // TODO 分页
    @Override
    public Result buildList(String openid){
        List<BuildInfo> buildInfoList = query().eq("openid", openid).list();
        if(buildInfoList == null){
            Result.ok("你的账号未绑定房屋信息");
        }

        return Result.ok(buildInfoList);
    }

    HouseInfoImport payInfo2HouseInfoImport(PayInfo payInfo){
        Consumption consumption = payInfo.getConsumption() == null ? new Consumption() : payInfo.getConsumption();

        return HouseInfoImport.builder()
                .villageName(payInfo.getVillageName()).buildNo(payInfo.getBuildNo())
                .roomNo(payInfo.getRoomNo()).name(payInfo.getName()).number(payInfo.getNumber())
                .resident(payInfo.getResident()).houseType(payInfo.getHouseType())
                .carType(payInfo.getCar()).relation(payInfo.getRelation())
                .conditionNumber(payInfo.getConditionNumber())
                .personNumber(payInfo.getPersonNumber()).rent(consumption.getRent())
                .area(consumption.getArea()).limitArea(consumption.getLimitArea())
                .overArea(consumption.getOverArea()).overareaFee(consumption.getOverareaFee())
                .property(consumption.getProperty()).propertyFee(consumption.getPropertyFee())
                .deposit(consumption.getDeposit()).waterFee(consumption.getWaterFee())
                .electricity(consumption.getElectricity()).gasFee(consumption.getGasFee())
                .carFee(consumption.getCarFee()).aFee(consumption.getAFee())
                .bFee(consumption.getBFee()).cFee(consumption.getCFee())
                .dFee(consumption.getDFee()).discount(consumption.getDiscount())
                .remarks(payInfo.getRemarks()).otherFee(consumption.getOtherFee())
                .carNumber(payInfo.getCarNumber()).build();
    }

    PayInfo houseInfoImport2PayInfo(HouseInfoImport houseInfoImport){
        Consumption consumption = Consumption.builder()
                .aFee(houseInfoImport.getAFee()).area(houseInfoImport.getArea())
                .bFee(houseInfoImport.getBFee()).carFee(houseInfoImport.getCarFee())
                .cFee(houseInfoImport.getCFee()).deposit(houseInfoImport.getDeposit())
                .dFee(houseInfoImport.getDFee()).discount(houseInfoImport.getDiscount())
                .electricity(houseInfoImport.getElectricity())
                .gasFee(houseInfoImport.getGasFee()).limitArea(houseInfoImport.getLimitArea())
                .overareaFee(houseInfoImport.getOverareaFee())
                .property(houseInfoImport.getProperty()).propertyFee(houseInfoImport.getPropertyFee())
                .waterFee(houseInfoImport.getWaterFee()).otherFee(houseInfoImport.getOtherFee()).build();
        return PayInfo.builder()
                .buildNo(houseInfoImport.getBuildNo()).car(houseInfoImport.getCarType())
                .conditionNumber(houseInfoImport.getConditionNumber())
                .name(houseInfoImport.getName()).number(houseInfoImport.getNumber())
                .personNumber(houseInfoImport.getPersonNumber()).relation(houseInfoImport.getRelation())
                .remarks(houseInfoImport.getRemarks()).resident(houseInfoImport.getResident())
                .roomNo(houseInfoImport.getRoomNo()).villageName(houseInfoImport.getVillageName())
                .consumption(consumption).carNumber(houseInfoImport.getCarNumber()).build();
    }

}
