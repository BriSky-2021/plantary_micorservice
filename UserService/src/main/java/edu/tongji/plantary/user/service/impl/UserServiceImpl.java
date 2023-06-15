package edu.tongji.plantary.user.service.impl;

import edu.tongji.plantary.user.dao.UserDao;
import edu.tongji.plantary.user.entity.User;
import edu.tongji.plantary.user.service.UserService;
import edu.tongji.plantary.user.service.UserValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private MongoTemplate mongoTemplate;

    @Autowired
    UserDao userDao;

    @Override
    public Optional<User> login(String phone, String passwd) {
        // 参数校验
        UserValidator.validatePhone(phone);
        UserValidator.validatePassword(passwd);

        return userDao.validatePasswd(phone,passwd);
    }

    // TODO: 测试这个函数
    @Override
    public Optional<User> register(String name, String phone, String passwd ,String sex) {
        // 参数校验
        UserValidator.validateName(name);
        UserValidator.validatePhone(phone);
        UserValidator.validatePassword(passwd);

        // 检查数据库中是否已经存在该用户
        Optional<User> existingUser = userDao.findByPhone(phone);
        if (existingUser.isPresent()) {
            System.out.println("Phone number already registered.");
            return Optional.empty();
        }

        // 创建新用户
        User user = new User();
        List<String> availableSexes = Arrays.asList("男", "女", "非二元性别");
        String userSex = (sex == null || !availableSexes.contains(sex)) ? "非二元性别" : sex;

        // 设置用户信息
        user.setPhone(phone);
        user.setName(name);
        user.setPasswd(passwd);
        user.setSex(userSex);
        user.setAvatar("https://img.ddtouxiang.com/upload/images/20230529/2023052908353959421.jpg");

        // 将用户信息插入数据库
        User registeredUser = mongoTemplate.insert(user);

        // 返回注册成功的用户信息
        return Optional.of(registeredUser);
    }

    @Override
    public Optional<User> getUserInfoByPhone(String phone) {
        Optional<User> user= userDao.findByPhone(phone);
        return user;
    }

    @Override
    public List<User> getUserInfos() {
        return userDao.findAll();
    }

    // TODO: 测试这个函数 正交法
    @Override
    public Optional<User> modifyUserInfo(User user) {
        // 检查参数对象和关键字段是否为null
        Objects.requireNonNull(user, "User object cannot be null.");
        Objects.requireNonNull(user.getPhone(), "Phone number cannot be null.");

        // 查询数据库中是否存在该用户
        Optional<User> existingUser = userDao.findByPhone(user.getPhone());
        if (!existingUser.isPresent()) {
            return Optional.empty();
        }

        // 参数校验
        if (user.getName() != null) {
            UserValidator.validateName(user.getName());
        }
        if (user.getAge() != null) {
            UserValidator.validateAge(user.getAge());
        }
        if (user.getPasswd() != null) {
            UserValidator.validatePassword(user.getPasswd());
        }
        if (user.getPhone() != null) {
            UserValidator.validatePhone(user.getPhone());
        }
        if (user.getBracelet() != null) {
            UserValidator.validateBracelet(user.getBracelet());
        }
        if (user.getSex() != null) {
            UserValidator.validateSex(user.getSex());
        }
        if (user.getAvatar() != null) {
            UserValidator.validateAvatar(user.getAvatar());
        }
        if (user.getWeight() != null) {
            UserValidator.validateWeight(user.getWeight());
        }
        if (user.getHeight() != null) {
            UserValidator.validateHeight(user.getHeight());
        }

        // 构建查询条件和更新对象
        Query query = new Query(Criteria.where("phone").is(user.getPhone()));
        Update update = new Update();
        // 如果字段存在且非空，则进行更新
        if (existingUser.get().getAge() == null || user.getAge() != null) {
            update.set("age", user.getAge());
        }
        if (existingUser.get().getName() == null || user.getName() != null) {
            update.set("name", user.getName());
        }
        if (existingUser.get().getPasswd() == null || user.getPasswd() != null) {
            update.set("passwd", user.getPasswd());
        }
        if (existingUser.get().getBracelet() == null || user.getBracelet() != null) {
            update.set("bracelet", user.getBracelet());
        }
        if (existingUser.get().getSex() == null || user.getSex() != null) {
            update.set("sex", user.getSex());
        }
        if (existingUser.get().getAvatar() == null || user.getAvatar() != null) {
            update.set("avatar", user.getAvatar());
        }
        if (existingUser.get().getWeight() == null || user.getWeight() != null) {
            update.set("weight", user.getWeight());
        }
        if (existingUser.get().getHeight() == null || user.getHeight() != null) {
            update.set("height", user.getHeight());
        }

        // 执行更新操作
        mongoTemplate.updateFirst(query, update, User.class);

        return Optional.of(user);
    }
}
