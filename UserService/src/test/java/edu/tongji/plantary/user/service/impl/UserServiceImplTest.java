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
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.stubbing.Answer;
import org.springframework.data.mongodb.core.MongoTemplate;

import java.util.Collections;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@Epic("用户Service层测试")
@Feature("用户Service层测试")
class UserServiceImplTest {
    @Mock
    private UserDao userDao;    // mock userDao，不执行真正的数据库操作

    @Mock
    private MongoTemplate mongoTemplate;    // mock mongoTemplate，不执行真正的save操作
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
        // 1. 测试登录
        // 1.1 passwd和phone都正确
        doReturn(Optional.of(user)).when(userDao).validatePasswd("17830827328", "test");
        // 1.2 passwd正确，phone错误
        doReturn(Optional.empty()).when(userDao).validatePasswd("17830827329", "test");
        // 1.3 passwd错误，phone正确
        doReturn(Optional.empty()).when(userDao).validatePasswd("17830827328", "test1");
        // 1.4 passwd和phone都错误
        doReturn(Optional.empty()).when(userDao).validatePasswd("17830827329", "test1");

        // 2. 测试注册 - 状态迁移树
        // 使用 doAnswer() 方法模拟 findByPhone() 方法的行为
        doAnswer((Answer<Optional<User>>) invocation -> {
            String phone = invocation.getArgument(0);
            if (phone.equals(user.getPhone())) {
                return Optional.of(user);  // 已注册的号码，返回 User 对象
            } else {
                return Optional.empty();  // 未注册的号码，返回 Optional.empty()
            }
        }).when(userDao).findByPhone(ArgumentMatchers.anyString());
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
            assertThrows(NullPointerException.class, () -> {
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
            assertThrows(NullPointerException.class, () -> {
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

    @Nested
    @DisplayName("Register Stub Test")
    @Story("使用状态迁移树的方法，测试UserService的register方法")
    class RegisterStubTest {
        @Test
        @DisplayName("测试用例1 - sex为null")
        @Story("测试sex为null")
        void register_with_null_sex() {
            String name = "Emily Davis";
            String phone = "17830827330";
            String passwd = "Password123";

            // 构建测试时期望的 User 对象
            User expectedUser = new User();
            expectedUser.setName(name);
            expectedUser.setPhone(phone);
            expectedUser.setPasswd(passwd);

            // 使用 doAnswer() 方法模拟 insert() 方法的行为
            doAnswer(invocation -> {
                User savedUser = invocation.getArgument(0);
                return expectedUser;  // 返回构建的测试 User 对象
            }).when(mongoTemplate).insert(any(User.class));

            Optional<User> registerUser = userService.register(name, phone, passwd, null);
            assertTrue(registerUser.isPresent());
            assertEquals(name, registerUser.get().getName());
            assertEquals(phone, registerUser.get().getPhone());
            assertEquals(passwd, registerUser.get().getPasswd());
        }

        @Test
        @DisplayName("测试用例2 - name为最小长度")
        @Story("测试name为最小长度")
        void register_with_min_length_name() {
            String name = "A";
            String phone = "17830827330";
            String passwd = "Password123";
            String sex = "男";

            // 构建测试时期望的 User 对象
            User expectedUser = new User();
            expectedUser.setName(name);
            expectedUser.setPhone(phone);
            expectedUser.setPasswd(passwd);
            expectedUser.setSex(sex);

            // 使用 doAnswer() 方法模拟 insert() 方法的行为
            doAnswer(invocation -> {
                User savedUser = invocation.getArgument(0);
                return expectedUser;  // 返回构建的测试 User 对象
            }).when(mongoTemplate).insert(any(User.class));

            Optional<User> registerUser = userService.register(name, phone, passwd, sex);
            assertTrue(registerUser.isPresent());
            assertEquals(name, registerUser.get().getName());
            assertEquals(phone, registerUser.get().getPhone());
            assertEquals(passwd, registerUser.get().getPasswd());
            assertEquals(sex, registerUser.get().getSex());
        }

        @Test
        @DisplayName("测试用例3 - name超过最大长度")
        @Story("测试name超过最大长度")
        void register_with_max_length_name() {
            String name = "This is a long name exceeding the maximum length";
            String phone = "17830827330";
            String passwd = "Password123";
            String sex = "男";

            // 抛出IllegalArgumentException异常
            assertThrows(IllegalArgumentException.class, () -> {
                userService.register(name, phone, passwd, sex);
            });
        }

        @Test
        @DisplayName("测试用例4 - phone为最小长度")
        @Story("测试phone为最小长度")
        void register_with_min_length_phone() {
            String name = "Emily Davis";
            String phone = "12345678901";
            String passwd = "Password123";
            String sex = "男";

            // 构建测试时期望的 User 对象
            User expectedUser = new User();
            expectedUser.setName(name);
            expectedUser.setPhone(phone);
            expectedUser.setPasswd(passwd);
            expectedUser.setSex(sex);

            // 使用 doAnswer() 方法模拟 insert() 方法的行为
            doAnswer(invocation -> {
                User savedUser = invocation.getArgument(0);
                return expectedUser;  // 返回构建的测试 User 对象
            }).when(mongoTemplate).insert(any(User.class));

            Optional<User> registerUser = userService.register(name, phone, passwd, sex);
            assertTrue(registerUser.isPresent());
            assertEquals(name, registerUser.get().getName());
            assertEquals(phone, registerUser.get().getPhone());
            assertEquals(passwd, registerUser.get().getPasswd());
            assertEquals(sex, registerUser.get().getSex());
        }

        @Test
        @DisplayName("测试用例5 - phone超过最大长度")
        @Story("测试phone超过最大长度")
        void register_with_max_length_phone() {
            String name = "Emily Davis";
            String phone = "123456789012";
            String passwd = "Password123";
            String sex = "男";

            // 抛出IllegalArgumentException异常
            assertThrows(IllegalArgumentException.class, () -> {
                userService.register(name, phone, passwd, sex);
            });
        }

        @Test
        @DisplayName("测试用例6 - phone不是数字")
        @Story("测试phone不是数字")
        void register_with_non_numeric_phone() {
            String name = "Emily Davis";
            String phone = "abcdefghijk";
            String passwd = "Password123";
            String sex = "男";

            // 抛出IllegalArgumentException异常
            assertThrows(IllegalArgumentException.class, () -> {
                userService.register(name, phone, passwd, sex);
            });
        }

        @Test
        @DisplayName("测试用例7 - passwd为最小长度")
        @Story("测试passwd为最小长度")
        void register_with_min_length_passwd() {
            String name = "Emily Davis";
            String phone = "17830827330";
            String passwd = "Pas";
            String sex = "男";

            // 抛出IllegalArgumentException异常
            assertThrows(IllegalArgumentException.class, () -> {
                userService.register(name, phone, passwd, sex);
            });
        }

        @Test
        @DisplayName("测试用例8 - passwd超过最大长度")
        @Story("测试passwd超过最大长度")
        void register_with_max_length_passwd() {
            String name = "Emily Davis";
            String phone = "17830827330";
            String passwd = "Password123456789012345678901234567890123456789012345";
            String sex = "男";

            // 抛出IllegalArgumentException异常
            assertThrows(IllegalArgumentException.class, () -> {
                userService.register(name, phone, passwd, sex);
            });
        }

        @Test
        @DisplayName("测试用例9 - name为null")
        @Story("测试name为null")
        void register_with_null_name() {
            String name = null;
            String phone = "17830827330";
            String passwd = "Password123";
            String sex = "男";

            // 抛出NullPointerException异常
            assertThrows(NullPointerException.class, () -> {
                userService.register(name, phone, passwd, sex);
            });
        }

        @Test
        @DisplayName("测试用例10 - phone为null")
        @Story("测试phone为null")
        void register_with_null_phone() {
            String name = "Emily Davis";
            String phone = null;
            String passwd = "Password123";
            String sex = "男";

            // 抛出NullPointerException异常
            assertThrows(NullPointerException.class, () -> {
                userService.register(name, phone, passwd, sex);
            });
        }

        @Test
        @DisplayName("测试用例11 - passwd为null")
        @Story("测试passwd为null")
        void register_with_null_passwd() {
            String name = "Emily Davis";
            String phone = "17830827330";
            String passwd = null;
            String sex = "男";

            // 抛出NullPointerException异常
            assertThrows(NullPointerException.class, () -> {
                userService.register(name, phone, passwd, sex);
            });
        }

        @Test
        @DisplayName("测试用例12 - 已注册的phone")
        @Story("测试已注册的phone")
        void register_with_existing_phone() {
            String name = "Emily Davis";
            String phone = "17830827328";
            String passwd = "Password123";
            String sex = "男";

            // 使用 doReturn() 方法模拟 findByPhone() 方法的行为，返回已注册的 User 对象
            doReturn(Optional.of(new User())).when(userDao).findByPhone(phone);

            Optional<User> registerUser = userService.register(name, phone, passwd, sex);
            assertFalse(registerUser.isPresent());
        }

    }
}