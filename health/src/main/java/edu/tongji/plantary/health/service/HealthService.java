package edu.tongji.plantary.health.service;

import edu.tongji.plantary.health.entity.HealthInfo;

import java.util.List;
import java.util.Optional;

public interface HealthService {

    List<HealthInfo> getHealthInfoByPhone(String phone);

    List<HealthInfo> getHealthInfos();

    List<HealthInfo> getHealthInfoByPhoneAndDate(String phone,String startDate,String endDate);

    Optional<HealthInfo> uploadDailyInfo(String phone,String date,String exerciseIntensity,Double foodHeat,Integer exerciseDuration);

}
