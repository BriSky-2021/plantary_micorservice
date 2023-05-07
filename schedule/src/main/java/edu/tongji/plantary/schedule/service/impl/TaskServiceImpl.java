package edu.tongji.plantary.schedule.service.impl;

import edu.tongji.plantary.schedule.dao.TaskDao;
import edu.tongji.plantary.schedule.entity.Task;
import edu.tongji.plantary.schedule.service.TaskService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class TaskServiceImpl implements TaskService {

    @Autowired
    private MongoTemplate mongoTemplate;

    @Autowired
    TaskDao taskDao;

    @Override
    public Optional<Task> getUserInfoByTitle(String title) {
        Optional<Task> task= taskDao.findByTitle(title);
        return task;
    }

    @Override
    public List<Task> getTasks() {
        return taskDao.findAll();
    }

    @Override
    public Task addTask(Task task) {
        mongoTemplate.insert(task);

        return task;
    }

}
