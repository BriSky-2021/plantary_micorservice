package edu.tongji.plantary.schedule.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalTime;
import java.util.Date;
import java.util.List;

/**
 * TODO：跨日日程没有考虑
 * TODO: 为了间接初步功能实现，没有考虑重复日程
 * TODO: 重复日程的数据结构重构 https://stackoverflow.com/questions/5183630/calendar-recurring-repeating-tasks-best-storage-method
 */
@Data
@Document("Task")
public class Task {
    private String user; //电话号码
    private String title;
    private String desc;
    private boolean isCompleted=false;
    private boolean isDayLong; //全天活动，则没有时分秒，两个日期最好相同
    private boolean isRepeatable=false; //可重复，初步只考虑每日，不考虑截至日期
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
    @JsonFormat(locale = "zh", timezone = "Asia/Shanghai", pattern = "HH:mm")  // 时区，传给前端需要设置东八区 https://blog.csdn.net/zhangjian8641/article/details/110141412
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm")  //前端传参时，string转date格式化，不需要秒 https://zhuanlan.zhihu.com/p/147271819
    private Date startTime;
    @JsonFormat(locale = "zh", timezone = "Asia/Shanghai", pattern = "HH:mm")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm")
    private Date endTime;

    /**
     * 日期
     * 若不可重复，则两个日期之间事件连续
     * TODO:检查日期合法
     * 若可重复，则暂定每日重复
     */
    @JsonFormat(locale = "zh", timezone = "Asia/Shanghai", pattern = "yyyy-MM-dd")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date startDay;
    @JsonFormat(locale = "zh", timezone = "Asia/Shanghai", pattern = "yyyy-MM-dd")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date endDay;

//    @JsonFormat(locale = "zh", timezone = "Asia/Shanghai", pattern = "HH:mm")
//    @DateTimeFormat(pattern = "HH:mm")
//    private LocalTime test;
}

