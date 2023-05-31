package edu.tongji.plantary.circle;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;
import springfox.documentation.oas.annotations.EnableOpenApi;

@SpringBootApplication
@EnableOpenApi
@EnableScheduling
public class CircleApplication {
    public static void main(String[] args) {
        SpringApplication.run(CircleApplication.class, args);
    }
}