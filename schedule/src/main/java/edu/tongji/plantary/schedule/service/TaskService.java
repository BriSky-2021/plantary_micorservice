package edu.tongji.plantary.schedule.service;

import edu.tongji.plantary.schedule.entity.Task;

import java.util.List;
import java.util.Optional;

public interface TaskService {

    Optional<Task> getUserInfoByTitle(String title);

    void deleteTaskById(String _id);

    Task update(Task task);

    List<Task> getTasks();

    Task addTask(Task task);

//    Optional<Task> modifyUserInfo(User user);

}
