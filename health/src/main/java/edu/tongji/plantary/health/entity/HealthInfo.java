package edu.tongji.plantary.health.entity;


import lombok.Data;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@Document("HealthInfo")
public class HealthInfo {
    String date;
    String userPhone;
    Integer exerciseDuration;
    String exerciseIntensity;
    Double foodHeat;
}
