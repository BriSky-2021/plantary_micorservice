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
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@SpringBootTest
@ExtendWith(AllureJunit5.class)
@Epic("User DAO Tests Epic")
@Feature("User DAO Feature")
public class UserDaoTest {

    @Mock
    private UserDao userDao;

    private final List<User> users = new ArrayList<>(2);

    @BeforeEach
    void setup(){
        // 模拟生成一个UserDao对象
        userDao = mock(UserDao.class);
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

    @Test
    @DisplayName("Find by Name Test")
    @Description("Test the findByName method in UserDao")
    @Story("User DAO")
    void testFindByName() {
        // Arrange
        String name = "test";
        when(userDao.findByName(name)).thenReturn(users);

        // Act
        List<User> result = userDao.findByName(name);

        // Assert
        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals("test", result.get(0).getName());
        assertEquals("test2", result.get(1).getName());

        // Verify the interaction with the userDao mock
        verify(userDao, times(1)).findByName(name);
    }

    @Test
    @DisplayName("Validate Password Test")
    @Description("Test the validatePasswd method in UserDao")
    @Story("User DAO")
    void testValidatePasswd() {
        // Arrange
        String phone = "123456789";
        String passwd = "test";
        User user = new User();
        user.setPhone(phone);
        user.setPasswd(passwd);
        Optional<User> optionalUser = Optional.of(user);
        when(userDao.validatePasswd(phone, passwd)).thenReturn(optionalUser);

        // Act
        Optional<User> result = userDao.validatePasswd(phone, passwd);

        // Assert
        assertTrue(result.isPresent());
        assertEquals(phone, result.get().getPhone());
        assertEquals(passwd, result.get().getPasswd());

        // Verify the interaction with the userDao mock
        verify(userDao, times(1)).validatePasswd(phone, passwd);
    }

    @Test
    @DisplayName("Find by Phone Test")
    @Description("Test the findByPhone method in UserDao")
    @Story("User DAO")
    void testFindByPhone() {
        // Arrange
        String phone = "123456789";
        User user = new User();
        user.setPhone(phone);
        Optional<User> optionalUser = Optional.of(user);
        when(userDao.findByPhone(phone)).thenReturn(optionalUser);

        // Act
        Optional<User> result = userDao.findByPhone(phone);

        // Assert
        assertTrue(result.isPresent());
        assertEquals(phone, result.get().getPhone());

        // Verify the interaction with the userDao mock
        verify(userDao, times(1)).findByPhone(phone);
    }

    @Test
    @DisplayName("Find by Password Test")
    @Description("Test the findByPasswd method in UserDao")
    @Story("User DAO")
    void testFindByPasswd() {
        // Arrange
        String passwd = "test";
        when(userDao.findByPasswd(passwd)).thenReturn(users);

        // Act
        List<User> result = userDao.findByPasswd(passwd);

        // Assert
        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals("test", result.get(0).getPasswd());
        assertEquals("test2", result.get(1).getPasswd());

        // Verify the interaction with the userDao mock
        verify(userDao, times(1)).findByPasswd(passwd);
    }

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
}
