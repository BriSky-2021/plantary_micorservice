package edu.tongji.plantary.schedule.service;

import edu.tongji.plantary.schedule.entity.Task;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public interface TaskService {

    Optional<Task> getUserInfoByTitle(String title);

    void deleteTaskById(String _id);

    Task update(Task task);

    List<Task> getTasks();

    List<Task> getTasksByUserId(String userId);

    List<Task> getTasksByUserIdAndDate(String userId, LocalDate date);

    Task addTask(Task task);
}
