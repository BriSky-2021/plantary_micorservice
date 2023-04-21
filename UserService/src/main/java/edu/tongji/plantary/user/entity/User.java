package edu.tongji.plantary.user.entity;

import lombok.Data;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

@Data
@Document("User")
public class User {

//    @Id
//    private Integer id;
    private String name;
    private Integer age;
    private String passwd;
//    @Id
    private String phone;
    private String bracelet;
    private String sex;
    private String avatar;
    private Double weight;
    private Double height;
    private List<UserSleepInfo> sleepInfoList;
    private List<UserExerciseInfo> exerciseInfoList;
}

