package edu.tongji.plantary.health.service.impl;

import edu.tongji.plantary.health.dao.HealthDao;
import edu.tongji.plantary.health.entity.HealthInfo;
import edu.tongji.plantary.health.service.HealthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class HealthServiceImpl implements HealthService {

    @Autowired
    private HealthDao healthDao;

    @Override
    public List<HealthInfo> getHealthInfoByPhone(String phone) {
        return healthDao.findByUserPhone(phone);
    }

    @Override
    public List<HealthInfo> getHealthInfos() {
        return healthDao.findAll();
    }

    @Override
    public List<HealthInfo> getHealthInfoByPhoneAndDate(String phone, String startDate, String endDate) {
        return null;
    }

    @Override
    public Optional<HealthInfo> uploadDailyInfo(String phone, String date, String exerciseIntensity, Double foodHeat, Integer exerciseDuration) {

        HealthInfo healthInfo=new HealthInfo();
        healthInfo.setDate(date);
        healthInfo.setFoodHeat(foodHeat);
        healthInfo.setExerciseDuration(exerciseDuration);
        healthInfo.setUserPhone(phone);
        healthInfo.setExerciseIntensity(exerciseIntensity);

        HealthInfo ret =healthDao.insert(healthInfo);
        if(ret==null){
            return Optional.empty();
        }else{
            return Optional.of(ret);
        }

    }
}
