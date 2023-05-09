package edu.tongji.plantary.health.controller;

import edu.tongji.plantary.health.dao.HealthDao;
import edu.tongji.plantary.health.entity.HealthInfo;
import edu.tongji.plantary.health.service.HealthService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@Api(tags = "用户接口")
@RestController
@RequestMapping("/HC")
public class HealthController {

    @Autowired
    private HealthService healthService;


    @ApiOperation(value = "通过电话号码获取信息")
    @GetMapping("/HealthInfo/{phone}")
    @ResponseBody
    List<HealthInfo> getHealthInfoByPhone(@PathVariable String phone){
        List<HealthInfo> list= healthService.getHealthInfoByPhone(phone);
        if(list.size()==0){
            return null;
        }
        else{
            return list;
        }
    }

    @ApiOperation(value = "获取信息")
    @GetMapping("/HealthInfos")
    @ResponseBody
    List<HealthInfo> getHealthInfos(){
        return healthService.getHealthInfos();
    }

    @ApiOperation(value = "上传信息")
    @PutMapping("/HealthInfo/{phone}/{date}")
    @ResponseBody
    HealthInfo uploadHealthInfo(@PathVariable String phone,@PathVariable String date,String exerciseIntensity,Double foodHeat,Integer exerciseDuration){

        Optional<HealthInfo> healthInfo=healthService.uploadDailyInfo(phone,date,exerciseIntensity,foodHeat,exerciseDuration);
        if(healthInfo.isPresent()){
            return healthInfo.get();
        }else{
            return null;
        }

    }

}
