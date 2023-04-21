package edu.tongji.plantary.schedule.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

@Api(tags = "用户接口")
@RestController
@RequestMapping("/SC")
public class ScheduleController {

    @ApiOperation(value = "测试")
    @GetMapping("/test")
    @ResponseBody
    public String test(){
        return "ok";
    }
}
