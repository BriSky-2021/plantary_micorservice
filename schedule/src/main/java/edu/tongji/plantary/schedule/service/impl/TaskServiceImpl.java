package edu.tongji.plantary.schedule.service.impl;

import edu.tongji.plantary.schedule.dao.TaskDao;
import edu.tongji.plantary.schedule.entity.Task;
import edu.tongji.plantary.schedule.service.TaskService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.aggregation.AggregationResults;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
public class TaskServiceImpl implements TaskService {

    @Autowired
    private MongoTemplate mongoTemplate;

    @Autowired
    TaskDao taskDao;

    @Override
    public Optional<Task> getUserInfoByTitle(String title) {
        return taskDao.findByTitle(title);
    }
    @Override
    public void deleteTaskById(String _id) {
        taskDao.deleteById(_id);
    }

    @Override
    public Task update(Task task) {
        return taskDao.save(task);
    }

    @Override
    public List<Task> getTasks() {
        return taskDao.findAll();
    }

    @Override
    public List<Task> getTasksByUserId(String userId) {
        List<Task> tasks = null;
        //对javaList排序一定很占用内存，还是用数据query好一点
        //tasks = taskDao.findTasksByUserId(userId);
        //tasks.sort(Comparator.comparing(Task::getDate));

        // https://blog.csdn.net/lck_csdn/article/details/119668704
        Criteria criteria = Criteria.where("userId").is(userId);//查询条件
        Query query = new Query(criteria);
        query.limit(1000);//指定的单词查询数据条数
        query.with(Sort.by(Sort.Direction.ASC,"date","startTime"));//指定排序字段
        tasks=mongoTemplate.find(query, Task.class, "Task");//查询语句、保存的实体类、查询的collection

        return tasks;
    }

    @Override
    public List<Task> getTasksByUserIdAndDate(String userId, LocalDate date) {
        List<Task> tasks = null;
        //只能模糊查询：大于等于当天，小于下一天，否则没有结果
        LocalDate nextDay = date.plusDays(1); // 获取下一天的日期
        Criteria criteria = Criteria.where("userId").is(userId).and("date")
                .gte(date)
                .lt(nextDay);
        Query query = new Query(criteria);
        query.limit(1000);//指定的单词查询数据条数
        query.with(Sort.by(Sort.Direction.ASC,"date","startTime"));//指定排序字段
        tasks=mongoTemplate.find(query, Task.class, "Task");//查询语句、保存的实体类、查询的collection

        return tasks;
    }

    @Override
    public Task addTask(Task task) {
        mongoTemplate.insert(task);

        return task;
    }

}
