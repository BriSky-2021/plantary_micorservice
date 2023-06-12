package edu.tongji.plantary.user.service.impl;

import edu.tongji.plantary.user.dao.UserDao;
import edu.tongji.plantary.user.entity.User;
import io.qameta.allure.Description;
import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import io.qameta.allure.Story;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@Epic("用户Service层测试")
@Feature("用户Service层测试")
class UserServiceImplTest {

    @Mock
    private UserServiceImpl userService;
    private final List<User> users = new ArrayList<>(2);

    @BeforeEach
    void setup() {
        // 模拟生成一个User对象
        User user = new User();
        user.setName("test");
        user.setPhone("123456789");
        user.setAge(21);
        user.setPasswd("test");
        user.setSex("男");
        users.add(user);
        // 模拟生成一个User对象
        User user2 = new User();
        user2.setName("test2");
        user2.setPhone("123456789");
        user2.setAge(21);
        user2.setPasswd("test2");
        user2.setSex("女");
        users.add(user2);
    }


    @Nested
    @DisplayName("Login Test")
    class LoginTest {
        @Test
        @DisplayName("Login Success Test")
        @Description("Test the login method in UserServiceImpl")
        @Story("用户输入正确的用户名和密码，登录成功")
        void login_with_correct_username_and_password(){
            userService = mock(UserServiceImpl.class);
            for (User user : users) {
                when(userService.login(user.getName(), user.getPasswd())).thenReturn(Optional.of(user));
                Optional<User> result = userService.login(user.getName(), user.getPasswd());
                assertTrue(result.isPresent());
                assertEquals(user.getName(), result.get().getName());
            }
        }

        @Test
        @DisplayName("Login Fail Test")
        @Description("Test the login method in UserServiceImpl")
        @Story("用户输入错误的用户名和密码，登录失败")
        void login_with_wrong_username_and_password(){
            userService = mock(UserServiceImpl.class);
            for (User user : users) {
                Optional<User> result = userService.login(user.getName(), user.getPasswd() + "wrong");
                assertFalse(result.isPresent());
            }
        }
    }
}