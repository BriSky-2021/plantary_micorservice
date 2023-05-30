package edu.tongji.plantary.schedule.controller;

import edu.tongji.plantary.schedule.entity.Task;
import edu.tongji.plantary.schedule.service.TaskService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@Api(tags = "日程接口")
@RestController
@RequestMapping("/SC")
public class ScheduleController {

    @Autowired
    TaskService taskService;

    @ApiOperation(value = "以日期获取事件")
    @GetMapping("/task/{date}")
    @ResponseBody
    public Task getTaskByDate(@PathVariable String date){

        Optional<Task> task=taskService.getUserInfoByTitle(date);
        return task.orElse(null);
    }

    @ApiOperation(value = "获取所有事件")
    @GetMapping("/tasks")
    @ResponseBody
    public List<Task> getUserInfos(){

        List<Task> task=taskService.getTasks();
        if(task.size()!=0){
            return task;
        }else{
            return null;
        }
    }

    @ApiOperation(value = "删除task")
    @DeleteMapping("/task/{_id}")
    @ResponseBody
    public boolean deleteTask(@PathVariable String _id){
        taskService.deleteTaskById(_id);
        return true;
    }

    @ApiOperation(value = "更新task")
    @PutMapping("task")
    @ResponseBody
    public Task updateTask(@RequestBody Task task){
        if(task.isDayLong()){ //全日日程不记录时刻数据
            task.setStartTime(null);
            task.setEndTime(null);
        }
        return taskService.update(task);
    }

    @ApiOperation(value = "新增事件")
    @PostMapping("/addTask")
    @ResponseBody
    public Task addTask(@RequestBody Task task){
        if(task.isDayLong()){ //全日日程不记录时刻数据
            task.setStartTime(null);
            task.setEndTime(null);
        }
        task.set_id(null);  //需要由数据库生成
        return(taskService.addTask(task));
    }

    @ApiOperation(value = "测试")
    @GetMapping("/test")
    @ResponseBody
    public String test(){
        return "ok";
    }
}
