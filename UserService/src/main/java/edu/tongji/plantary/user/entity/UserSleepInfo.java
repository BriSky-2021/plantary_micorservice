package edu.tongji.plantary.user.entity;

import lombok.Data;

@Data
public class UserSleepInfo {
    private String date;
    private Integer total_time;
    private String quality;
}
