package com.tbxx.wpct.service.impl;

import com.alibaba.excel.EasyExcel;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
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
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.List;

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
    private CheckServiceImpl checkService;


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
            Consumption consumption = Consumption.builder()
                    .aFee(houseInfoImport.getAFee()).area(houseInfoImport.getArea())
                    .bFee(houseInfoImport.getBFee()).carFee(houseInfoImport.getCarFee())
                    .cFee(houseInfoImport.getCFee()).deposit(houseInfoImport.getDeposit())
                    .dFee(houseInfoImport.getDFee()).discount(houseInfoImport.getDiscount())
                    .electricity(houseInfoImport.getElectricity())
                    .gasFee(houseInfoImport.getGasFee()).limitArea(houseInfoImport.getLimitArea())
                    .overareaFee(houseInfoImport.getOverareaFee())
                    .property(houseInfoImport.getProperty()).propertyFee(houseInfoImport.getPropertyFee())
                    .waterFee(houseInfoImport.getWaterFee()).build();
            PayInfo payInfo = PayInfo.builder()
                    .buildNo(houseInfoImport.getBuildNo()).car(houseInfoImport.getCarType())
                    .conditionNumber(houseInfoImport.getConditionNumber())
                    .name(houseInfoImport.getName()).number(houseInfoImport.getNumber())
                    .personNumber(houseInfoImport.getPersonNumber()).relation(houseInfoImport.getRelation())
                    .remarks(houseInfoImport.getRemarks()).resident(houseInfoImport.getResident())
                    .roomNo(houseInfoImport.getRoomNo()).villageName(houseInfoImport.getVillageName())
                    .consumption(consumption).build();
            checkService.addCheck(payInfo);
            num++;
        }
        return Result.ok(num);
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
    };
}
