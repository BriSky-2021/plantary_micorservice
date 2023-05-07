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
        if(task.isPresent()){
            return task.get();
        }else{
            return null;
        }
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

    @ApiOperation(value = "新增事件")
    @PostMapping("/addTask")
    @ResponseBody
    public Task addTask(Task task){
        if(task.isDayLong()){ //全日日程不记录时刻数据
            task.setStartTime(null);
            task.setEndTime(null);
        }

        return(taskService.addTask(task));
    }

    @ApiOperation(value = "测试")
    @GetMapping("/test")
    @ResponseBody
    public String test(){
        return "ok";
    }
}
