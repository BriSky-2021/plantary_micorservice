package edu.tongji.plantary.user.service.impl;

import com.mongodb.client.result.UpdateResult;
import edu.tongji.plantary.user.dao.UserDao;
import edu.tongji.plantary.user.entity.User;
import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import io.qameta.allure.Story;
import org.bson.BsonValue;
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
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.UpdateDefinition;

import java.util.Collections;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@Epic("用户Service层测试")
@Feature("用户Service层测试")
class UserServiceImplTest {
    @Mock
    private UserDao userDao; // mock userDao，不执行真正的数据库操作

    @Mock
    private MongoTemplate mongoTemplate; // mock mongoTemplate，不执行真正的insert操作
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
                return Optional.of(user); // 已注册的号码，返回 User 对象
            } else {
                return Optional.empty(); // 未注册的号码，返回 Optional.empty()
            }
        }).when(userDao).findByPhone(ArgumentMatchers.anyString());

        // 3. 测试修改用户信息
        // 使用 doAnswer() 方法模拟 updateFirst() 方法的行为
        doAnswer((Answer<UpdateResult>) invocation -> {
            // do nothing but return an UpdateResult
            return new UpdateResult() {
                @Override
                public boolean wasAcknowledged() {
                    return false;
                }

                @Override
                public long getMatchedCount() {
                    return 0;
                }

                @Override
                public long getModifiedCount() {
                    return 0;
                }

                @Override
                public BsonValue getUpsertedId() {
                    return null;
                }
            };
        }).when(mongoTemplate).updateFirst((Query) ArgumentMatchers.any(), (UpdateDefinition) ArgumentMatchers.any(),
                (Class<?>) ArgumentMatchers.any());
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
            assertThrows(IllegalArgumentException.class, () -> userService.login("", "test"));
        }

        @Test
        @Story("测试电话为null")
        @DisplayName("2.2 Login Fail Test")
        void login_with_null_phone() {
            assertThrows(NullPointerException.class, () -> userService.login(null, "test"));
        }

        @Test
        @Story("测试电话不符合正则表达式")
        @DisplayName("2.3 Login Fail Test")
        void login_with_wrong_pattern_phone() {
            assertThrows(IllegalArgumentException.class, () -> userService.login("1783082732a", "test"));
            assertThrows(IllegalArgumentException.class, () -> userService.login("178308273281", "test"));
            assertThrows(IllegalArgumentException.class, () -> userService.login("1783082732", "test"));
        }

        @Test
        @Story("测试密码为空字符串")
        @DisplayName("3.1 Login Fail Test")
        void login_with_empty_password() {
            assertThrows(IllegalArgumentException.class, () -> userService.login("17830827328", ""));
        }

        @Test
        @Story("测试密码为null")
        @DisplayName("3.2 Login Fail Test")
        void login_with_null_password() {
            assertThrows(NullPointerException.class, () -> userService.login("17830827328", null));
        }

        @Test
        @Story("测试密码长度小于4")
        @DisplayName("3.3 Login Fail Test")
        void login_with_password_length_less_than_4() {
            assertThrows(IllegalArgumentException.class, () -> userService.login("17830827328", "tes"));
        }

        @Test
        @Story("测试密码长度大于50")
        @DisplayName("3.4 Login Fail Test")
        void login_with_password_length_more_than_50() {
            assertThrows(IllegalArgumentException.class, () -> userService.login("17830827328",
                    Collections.nCopies(51, "a").stream().reduce("", String::concat)));
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
                return expectedUser; // 返回构建的测试 User 对象
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
                return expectedUser; // 返回构建的测试 User 对象
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
            assertThrows(IllegalArgumentException.class, () -> userService.register(name, phone, passwd, sex));
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
                return expectedUser; // 返回构建的测试 User 对象
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
            assertThrows(IllegalArgumentException.class, () -> userService.register(name, phone, passwd, sex));
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
            assertThrows(IllegalArgumentException.class, () -> userService.register(name, phone, passwd, sex));
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
            assertThrows(IllegalArgumentException.class, () -> userService.register(name, phone, passwd, sex));
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
            assertThrows(IllegalArgumentException.class, () -> userService.register(name, phone, passwd, sex));
        }

        @Test
        @DisplayName("测试用例9 - name为null")
        @Story("测试name为null")
        void register_with_null_name() {
            String phone = "17830827330";
            String passwd = "Password123";
            String sex = "男";

            // 抛出NullPointerException异常
            assertThrows(NullPointerException.class, () -> userService.register(null, phone, passwd, sex));
        }

        @Test
        @DisplayName("测试用例10 - phone为null")
        @Story("测试phone为null")
        void register_with_null_phone() {
            String name = "Emily Davis";
            String passwd = "Password123";
            String sex = "男";

            // 抛出NullPointerException异常
            assertThrows(NullPointerException.class, () -> userService.register(name, null, passwd, sex));
        }

        @Test
        @DisplayName("测试用例11 - passwd为null")
        @Story("测试passwd为null")
        void register_with_null_passwd() {
            String name = "Emily Davis";
            String phone = "17830827330";
            String sex = "男";

            // 抛出NullPointerException异常
            assertThrows(NullPointerException.class, () -> userService.register(name, phone, null, sex));
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

    @Nested
    @DisplayName("ModifyUserInfo测试")
    @Story("使用正交表法对ModifyUserInfo方法进行测试")
    class ModifyUserInfoStubTest {

        @Test
        @DisplayName("测试用例 - 1")
        @Story("测试用例 - 1")
        void test_case_1() {
            // null null null null "1" "男" null 171
            User user = new User();
            user.setName(null);
            user.setAge(null);
            user.setPasswd(null);
            user.setPhone(null);
            user.setBracelet("1");
            user.setSex("男");
            user.setAvatar(null);
            user.setHeight(171.0);
            assertThrows(NullPointerException.class, () -> userService.modifyUserInfo(user));
        }

        @Test
        @DisplayName("测试用例 - 2")
        @Story("测试用例 - 2")
        void test_case_2() {
            // "new name" null "new passwd" null "1" null
            // "https://s2.loli.net/2023/05/21/i3MvyxqobgmreKn.png" 171
            User user = new User();
            user.setName("new name");
            user.setAge(null);
            user.setPasswd("new passwd");
            user.setPhone(null);
            user.setBracelet("1");
            user.setSex(null);
            user.setAvatar("https://s2.loli.net/2023/05/21/i3MvyxqobgmreKn.png");
            user.setHeight(171.0);
            assertThrows(NullPointerException.class, () -> userService.modifyUserInfo(user));
        }

        @Test
        @DisplayName("测试用例 - 3")
        @Story("测试用例 - 3")
        void test_case_3() {
            // null 21 "new passwd" "17830827328" "1" "男" null 171
            User user = new User();
            user.setName(null);
            user.setAge(21);
            user.setPasswd("new passwd");
            user.setPhone("17830827328");
            user.setBracelet("1");
            user.setSex("男");
            user.setAvatar(null);
            user.setHeight(171.0);
            // 检查new_user是否存在
            Optional<User> new_user = userService.modifyUserInfo(user);
            assertTrue(new_user.isPresent());
            // 检查用户字段是否匹配
            assertEquals(user.getAge(), new_user.get().getAge());
            assertEquals(user.getPasswd(), new_user.get().getPasswd());
            assertEquals(user.getPhone(), new_user.get().getPhone());
            assertEquals(user.getBracelet(), new_user.get().getBracelet());
            assertEquals(user.getSex(), new_user.get().getSex());
            assertEquals(user.getHeight(), new_user.get().getHeight());
        }

        @Test
        @DisplayName("测试用例 - 4")
        @Story("测试用例 - 4")
        void test_case_4() {
            // "new name" 21 null null "1" "男" "https://s2.loli.net/2023/05/21/i3MvyxqobgmreKn.png" 171
            User user = new User();
            user.setName("new name");
            user.setAge(21);
            user.setPasswd(null);
            user.setPhone(null);
            user.setBracelet("1");
            user.setSex("男");
            user.setAvatar("https://s2.loli.net/2023/05/21/i3MvyxqobgmreKn.png");
            user.setHeight(171.0);
            assertThrows(NullPointerException.class, () -> userService.modifyUserInfo(user));
        }

        @Test
        @DisplayName("测试用例 - 5")
        @Story("测试用例 - 5")
        void test_case_5() {
            // null null "new passwd" "17830827328" null "男" "https://s2.loli.net/2023/05/21/i3MvyxqobgmreKn.png" 171
            User user = new User();
            user.setName(null);
            user.setAge(null);
            user.setPasswd("new passwd");
            user.setPhone("17830827328");
            user.setBracelet(null);
            user.setSex("男");
            user.setAvatar("https://s2.loli.net/2023/05/21/i3MvyxqobgmreKn.png");
            user.setHeight(171.0);

            // 修改用户信息并检查返回的新用户是否存在
            Optional<User> new_user = userService.modifyUserInfo(user);
            assertTrue(new_user.isPresent());

            // 检查用户字段是否匹配
            assertNull(new_user.get().getName());
            assertNull(new_user.get().getAge());
            assertEquals(user.getPasswd(), new_user.get().getPasswd());
            assertEquals(user.getPhone(), new_user.get().getPhone());
            assertNull(new_user.get().getBracelet());
            assertEquals(user.getSex(), new_user.get().getSex());
            assertEquals(user.getAvatar(), new_user.get().getAvatar());
            assertEquals(user.getHeight(), new_user.get().getHeight());
        }



        @Test
        @DisplayName("测试用例 - 6")
        @Story("测试用例 - 6")
        void test_case_6() {
            // "new name" null "new passwd" null null "男" "https://s2.loli.net/2023/05/21/i3MvyxqobgmreKn.png" null
            User user = new User();
            user.setName("new name");
            user.setAge(null);
            user.setPasswd("new passwd");
            user.setPhone(null);
            user.setBracelet(null);
            user.setSex("男");
            user.setAvatar("https://s2.loli.net/2023/05/21/i3MvyxqobgmreKn.png");
            user.setHeight(null);
            assertThrows(NullPointerException.class, () -> userService.modifyUserInfo(user));
        }

        @Test
        @DisplayName("测试用例 - 7")
        @Story("测试用例 - 7")
        void test_case_7() {
            // null 21 null null null null "https://s2.loli.net/2023/05/21/i3MvyxqobgmreKn.png" 171
            User user = new User();
            user.setName(null);
            user.setAge(21);
            user.setPasswd(null);
            user.setPhone(null);
            user.setBracelet(null);
            user.setSex(null);
            user.setAvatar("https://s2.loli.net/2023/05/21/i3MvyxqobgmreKn.png");
            user.setHeight(171.0);
            assertThrows(NullPointerException.class, () -> userService.modifyUserInfo(user));
        }

        @Test
        @DisplayName("测试用例 - 8")
        @Story("测试用例 - 8")
        void test_case_8() {
            // "new name" 21 null "17830827328" null "男" null null
            User user = new User();
            user.setName("new name");
            user.setAge(21);
            user.setPasswd(null);
            user.setPhone("17830827328");
            user.setBracelet(null);
            user.setSex("男");
            user.setAvatar(null);
            user.setHeight(null);
            // 检查new_user是否存在
            Optional<User> new_user = userService.modifyUserInfo(user);
            assertTrue(new_user.isPresent());
            // 检查用户字段是否匹配
            assertEquals(user.getName(), new_user.get().getName());
            assertEquals(user.getAge(), new_user.get().getAge());
            assertEquals(user.getPhone(), new_user.get().getPhone());
            assertEquals(user.getSex(), new_user.get().getSex());
        }

        @Test
        @DisplayName("测试用例 - 9")
        @Story("测试用例 - 9")
        void test_case_9() {
            // null null null null "1" "男" null null
            User user = new User();
            user.setName(null);
            user.setAge(null);
            user.setPasswd(null);
            user.setPhone(null);
            user.setBracelet("1");
            user.setSex("男");
            user.setAvatar(null);
            user.setHeight(null);
            assertThrows(NullPointerException.class, () -> userService.modifyUserInfo(user));
        }

        @Test
        @DisplayName("测试用例 - 10")
        @Story("测试用例 - 10")
        void test_case_10() {
            // "new name" null null "17830827328" null "男" null 171
            User user = new User();
            user.setName("new name");
            user.setAge(null);
            user.setPasswd(null);
            user.setPhone("17830827328");
            user.setBracelet(null);
            user.setSex("男");
            user.setAvatar(null);
            user.setHeight(171.0);
            // 检查new_user是否存在
            Optional<User> new_user = userService.modifyUserInfo(user);
            assertTrue(new_user.isPresent());
            // 检查用户字段是否匹配
            assertEquals(user.getName(), new_user.get().getName());
            assertEquals(user.getPhone(), new_user.get().getPhone());
            assertEquals(user.getSex(), new_user.get().getSex());
            assertEquals(user.getHeight(), new_user.get().getHeight());
        }

        @Test
        @DisplayName("测试用例 - 11")
        @Story("测试用例 - 11")
        void test_case_11() {
            // null 21 "new passwd" null null null null null
            User user = new User();
            user.setName(null);
            user.setAge(21);
            user.setPasswd("new passwd");
            user.setPhone(null);
            user.setBracelet(null);
            user.setSex(null);
            user.setAvatar(null);
            user.setHeight(null);
            assertThrows(NullPointerException.class, () -> userService.modifyUserInfo(user));
        }

        @Test
        @DisplayName("测试用例 - 12")
        @Story("测试用例 - 12")
        void test_case_12() {
            // "new name" 21 "new passwd" "17830827328" "1" null "https://s2.loli.net/2023/05/21/i3MvyxqobgmreKn.png" null
            User user = new User();
            user.setName("new name");
            user.setAge(21);
            user.setPasswd("new passwd");
            user.setPhone("17830827328");
            user.setBracelet("1");
            user.setSex(null);
            user.setAvatar("https://s2.loli.net/2023/05/21/i3MvyxqobgmreKn.png");
            user.setHeight(null);
            // 检查new_user是否存在
            Optional<User> new_user = userService.modifyUserInfo(user);
            assertTrue(new_user.isPresent());
            // 检查用户字段是否匹配
            assertEquals(user.getName(), new_user.get().getName());
            assertEquals(user.getAge(), new_user.get().getAge());
            assertEquals(user.getPasswd(), new_user.get().getPasswd());
            assertEquals(user.getPhone(), new_user.get().getPhone());
            assertEquals(user.getBracelet(), new_user.get().getBracelet());
            assertEquals(user.getAvatar(), new_user.get().getAvatar());
        }


    }
}