package edu.tongji.plantary.user.service.impl;

import edu.tongji.plantary.user.dao.UserDao;
import edu.tongji.plantary.user.entity.User;
import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import io.qameta.allure.Story;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Collections;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@Epic("用户Service层测试")
@Feature("用户Service层测试")
class UserServiceImplTest {
    @Mock
    private UserDao userDao;

    @InjectMocks
    private UserServiceImpl userService;

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this); // 初始化 Mockito
        User user = new User();
        user.setName("test");
        user.setAge(21);
        user.setPasswd("test");
        user.setPhone("17830827328");
        user.setSex("男");
        // 设置userDao的stub行为
        // 这里主要设置validatePasswd方法的stub行为
        // passwd的长度范围是4~50
        // phone是11位数字, 需要用正则表达式检查是不是11位数字
        // 输入不在范围内的passwd和phone，抛出IllegalArgumentException
        // 输入正确的passwd和phone，返回true
        // 输入错误的passwd和phone，返回false

        // 设计测试用例
        // 1. passwd的取值可能: 正确/错误/空字符串/null/长度超过50/长度小于4
        // 2. phone的取值可能: 正确/错误/不符合正则表达式/空字符串/null
        // 使用doReturn().when()的方式设置stub的返回
        // 使用doThrow().when()的方式设置stub的抛出异常

        // 使用弱健壮等价类划分法，划分等价类，每次选一个变量取所有的无效值
        // 1. passwd和phone都取有效值
        // 1.1 passwd和phone都正确
        doReturn(Optional.of(user)).when(userDao).validatePasswd("17830827328", "test");
        // 1.2 passwd正确，phone错误
        doReturn(Optional.empty()).when(userDao).validatePasswd("17830827329", "test");
        // 1.3 passwd错误，phone正确
        doReturn(Optional.empty()).when(userDao).validatePasswd("17830827328", "test1");
        // 1.4 passwd和phone都错误
        doReturn(Optional.empty()).when(userDao).validatePasswd("17830827329", "test1");
        // 2. passwd取有效值，phone取无效值
        // 2.1 passwd正确，phone为空字符串
        // doThrow(IllegalArgumentException.class).when(userDao).validatePasswd("", "test");
        // 2.2 passwd正确，phone为null
        // doThrow(IllegalArgumentException.class).when(userDao).validatePasswd(null, "test");
        // 2.3 passwd正确，phone不符合正则表达式
        // doThrow(IllegalArgumentException.class).when(userDao).validatePasswd("1783082732a", "test");
        // doThrow(IllegalArgumentException.class).when(userDao).validatePasswd("178308273281", "test");
        // doThrow(IllegalArgumentException.class).when(userDao).validatePasswd("1783082732", "test");
        // 3. passwd取无效值，phone取有效值
        // 3.1 passwd为空字符串，phone正确
        // doThrow(IllegalArgumentException.class).when(userDao).validatePasswd("17830827328", "");
        // 3.2 passwd为null，phone正确
        // doThrow(IllegalArgumentException.class).when(userDao).validatePasswd("17830827328", null);
        // 3.3 passwd长度小于4，phone正确
        // doThrow(IllegalArgumentException.class).when(userDao).validatePasswd("17830827328", "tes");
        // 3.4 passwd长度大于50，phone正确
        // doThrow(IllegalArgumentException.class).when(userDao).validatePasswd("17830827328", Collections.nCopies(51, "a").stream().reduce("", String::concat));


    }

    @Nested
    @DisplayName("Login Stub Test")
    @Story("把UserDao的Stub行为注入到UserService中，测试UserService的login方法")
    class LoginStubTest {
        @Test
        @Story("测试正确的电话和密码")
        @DisplayName("1.1 Login Success Test")
        void login_with_correct_phone_and_password() {
            Optional<User> user = userService.login("17830827328", "test");
            assertTrue(user.isPresent());
            assertEquals("test", user.get().getName());
        }

        @Test
        @Story("测试正确的电话和错误的密码")
        @DisplayName("1.2 Login Fail Test")
        void login_with_correct_phone_and_wrong_password() {
            Optional<User> user = userService.login("17830827328", "test1");
            assertFalse(user.isPresent());
        }

        @Test
        @Story("测试错误的电话和正确的密码")
        @DisplayName("1.3 Login Fail Test")
        void login_with_wrong_phone_and_correct_password() {
            Optional<User> user = userService.login("17830827329", "test");
            assertFalse(user.isPresent());
        }

        @Test
        @Story("测试错误的电话和密码")
        @DisplayName("1.4 Login Fail Test")
        void login_with_wrong_phone_and_wrong_password() {
            Optional<User> user = userService.login("17830827329", "test1");
            assertFalse(user.isPresent());
        }

        @Test
        @Story("测试电话为空字符串")
        @DisplayName("2.1 Login Fail Test")
        void login_with_empty_phone() {
            assertThrows(IllegalArgumentException.class, () -> {
                userService.login("", "test");
            });
        }

        @Test
        @Story("测试电话为null")
        @DisplayName("2.2 Login Fail Test")
        void login_with_null_phone() {
            assertThrows(IllegalArgumentException.class, () -> {
                userService.login(null, "test");
            });
        }

        @Test
        @Story("测试电话不符合正则表达式")
        @DisplayName("2.3 Login Fail Test")
        void login_with_wrong_pattern_phone() {
            assertThrows(IllegalArgumentException.class, () -> {
                userService.login("1783082732a", "test");
            });
            assertThrows(IllegalArgumentException.class, () -> {
                userService.login("178308273281", "test");
            });
            assertThrows(IllegalArgumentException.class, () -> {
                userService.login("1783082732", "test");
            });
        }

        @Test
        @Story("测试密码为空字符串")
        @DisplayName("3.1 Login Fail Test")
        void login_with_empty_password() {
            assertThrows(IllegalArgumentException.class, () -> {
                userService.login("17830827328", "");
            });
        }

        @Test
        @Story("测试密码为null")
        @DisplayName("3.2 Login Fail Test")
        void login_with_null_password() {
            assertThrows(IllegalArgumentException.class, () -> {
                userService.login("17830827328", null);
            });
        }

        @Test
        @Story("测试密码长度小于4")
        @DisplayName("3.3 Login Fail Test")
        void login_with_password_length_less_than_4() {
            assertThrows(IllegalArgumentException.class, () -> {
                userService.login("17830827328", "tes");
            });
        }

        @Test
        @Story("测试密码长度大于50")
        @DisplayName("3.4 Login Fail Test")
        void login_with_password_length_more_than_50() {
            assertThrows(IllegalArgumentException.class, () -> {
                userService.login("17830827328", Collections.nCopies(51, "a").stream().reduce("", String::concat));
            });
        }

    }

}