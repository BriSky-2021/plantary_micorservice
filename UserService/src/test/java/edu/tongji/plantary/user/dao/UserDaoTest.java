package edu.tongji.plantary.user.dao;

import edu.tongji.plantary.user.entity.User;
import io.qameta.allure.Description;
import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import io.qameta.allure.Story;
import io.qameta.allure.junit5.AllureJunit5;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(AllureJunit5.class)
@Epic("用户DAO层测试")
@Feature("用户DAO层测试")
@DataMongoTest
public class UserDaoTest {

    @Autowired
    private UserDao userDao;

    @Test
    @DisplayName("Sample Success Test")
    @Description("Test the sampleSuccess method in UserDao")
    @Story("Sample Success")
    void successSample(){
        int a = 1;
        int b = 2;
        assertEquals(3, a+b);
    }

    @Test
    @DisplayName("Sample Fail Test")
    @Description("Test the sampleFail method in UserDao")
    @Story("Sample Fail")
    void failSample(){
        int a = 1;
        int b = 2;
        assertEquals(4, a+b);
    }

    @Test
    @Disabled
    @DisplayName("Sample Skip Test")
    @Description("Test the sampleSkip method in UserDao")
    @Story("Sample Skip")
    void skipSample(){
        int a = 1;
        int b = 2;
        assertEquals(3, a+b);
    }

    @Test
    @DisplayName("Sample Exception Test")
    @Description("Test the sampleException method in UserDao")
    @Story("Sample Exception")
    void exceptionSample(){
        int a = 1;
        int b = 0;
        assertEquals(3, a/b);
    }

    @Test
    void checkTestProperties(){
        // 查询测试环境的数据库的数据
        System.out.println("Test Data: " + userDao.findAll());
    }
}
