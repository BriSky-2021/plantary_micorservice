package edu.tongji.plantary.user.entity;

import lombok.Data;

@Data
public class UserExerciseInfo {
    private String date;
    private Integer total_time;
    private Integer steps;
    private String intensity;
}
