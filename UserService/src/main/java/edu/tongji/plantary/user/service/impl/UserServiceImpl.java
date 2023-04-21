package edu.tongji.plantary.user.service.impl;

import edu.tongji.plantary.user.dao.UserDao;
import edu.tongji.plantary.user.entity.User;
import edu.tongji.plantary.user.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private MongoTemplate mongoTemplate;

    @Autowired
    UserDao userDao;

    @Override
    public Optional<User> login(String phone, String passwd) {

        Optional<User> user=userDao.validatePasswd(phone,passwd);

        return user;
    }

    @Override
    public Optional<User> register(String name, String phone, String passwd ,String sex) {

        //先查找phone有没有被注册
        Optional<User> users=userDao.findByPhone(phone);
        if(users.isPresent()){
            System.out.println(users.get());
            System.out.println("电话号码已被注册");
            return Optional.empty();
        }

        //再进行注册
        User user = new User();
        //user.setId(123);
        user.setSex(sex);
        user.setPhone(phone);
        user.setName(name);
        user.setPasswd(passwd);
        User user1 = mongoTemplate.insert(user);

        return Optional.of(user1);
    }
}
