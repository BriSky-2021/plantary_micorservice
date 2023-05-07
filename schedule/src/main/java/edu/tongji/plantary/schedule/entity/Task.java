package edu.tongji.plantary.schedule.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.format.annotation.DateTimeFormat;

import java.sql.Time;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Date;
import java.util.List;

/**
 * TODO: 为了间接初步功能实现，没有考虑重复日程
 * TODO: 重复日程的数据结构重构 https://stackoverflow.com/questions/5183630/calendar-recurring-repeating-tasks-best-storage-method
 */
@Data
@Document("Task")
public class Task {
    private String userId; //电话号码
    private String title;
    private String desc;
    private boolean isCompleted=false;
    private boolean isDayLong; //全天活动，则没有时分秒，两个日期最好相同
//    private boolean isRepeatable=false; //TODO:可重复，初步只考虑每日，不考虑截至日期
    /**
     * 时刻
     * 若全天则为null
     * 若非全日则日期无用
     * TODO:非全日日期删不掉
     * --------------------------------------------
     * mongoDB存储的时间-8h，但是与后端交互会自动转换回来
     * 但是前端swagger get后显示又-8h,需要设置时区
     *
     * 插入按照DateTimeFormat的格式
     * 查询按照JsonFormat的格式
     */
//    @JsonFormat(locale = "zh", timezone = "Asia/Shanghai", pattern = "HH:mm")  //出参： 时区，传给前端需要设置东八区 https://blog.csdn.net/zhangjian8641/article/details/110141412
//    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm")  //入参： 前端传参时，string转date格式化，不需要秒 https://zhuanlan.zhihu.com/p/147271819
//    private Date startTime;
//    @JsonFormat(locale = "zh", timezone = "Asia/Shanghai", pattern = "HH:mm")
//    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm")
//    private Date endTime;


    @DateTimeFormat(pattern = "yyyy-MM-dd")  //入参 前端->后端 yyyy-MM-dd字符串
    private LocalDate date;

    /**
     * 时刻
     * TODO: swagger无法测试，只能apifox
     */
    @DateTimeFormat(pattern = "HH:mm")  //入参 前端->后端 HH:mm字符串
    private LocalTime startTime;
    @DateTimeFormat(pattern = "HH:mm")  //入参 前端->后端 HH:mm字符串
    private LocalTime endTime;
}

