package edu.tongji.plantary.health.dao;

import edu.tongji.plantary.health.entity.HealthInfo;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface HealthDao extends MongoRepository<HealthInfo,String> {

    @Query(value = "{'userPhone':'?0'}")
    List<HealthInfo> findByUserPhone(String phone);

}

