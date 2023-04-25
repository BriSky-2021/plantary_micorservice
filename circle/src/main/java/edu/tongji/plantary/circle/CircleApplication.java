package edu.tongji.plantary.circle;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import springfox.documentation.oas.annotations.EnableOpenApi;

@SpringBootApplication
@EnableOpenApi
public class CircleApplication {
    public static void main(String[] args) {
        SpringApplication.run(CircleApplication.class, args);
    }
}