package edu.tongji.plantary.user.service.impl;

import edu.tongji.plantary.user.dao.UserDao;
import edu.tongji.plantary.user.entity.User;
import edu.tongji.plantary.user.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Service;

import java.util.List;
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

    @Override
    public Optional<User> getUserInfoByPhone(String phone) {
        Optional<User> user= userDao.findByPhone(phone);
        return user;
    }

    @Override
    public List<User> getUserInfos() {
        return userDao.findAll();
    }

    @Override
    public Optional<User> modifyUserInfo(User user) {

        Optional<User> user1=userDao.findByPhone(user.getPhone());
        if(!user1.isPresent()){//不存在则不修改
            return Optional.empty();
        }

        Query query = new Query(Criteria.where("phone").is(user.getPhone()));
        Update update = new Update();
        //如果存在且非空则修改，不考虑列表信息
        if(user1.get().getAge()==null || user.getAge()!=null){
            update.set("age",user.getAge());
        }
        if(user1.get().getName()==null || user.getName()!=null){
            update.set("name",user.getName());
        }
        if(user1.get().getPasswd()==null || user.getPasswd()!=null){
            update.set("passwd",user.getPasswd());
        }
        if(user1.get().getBracelet()==null || user.getBracelet()!=null){
            update.set("bracelet",user.getBracelet());
        }
        if(user1.get().getSex()==null || user.getSex()!=null){
            update.set("sex",user.getSex());
        }
        if(user1.get().getAvatar()==null || user.getAvatar()!=null){
            update.set("avatar",user.getAvatar());
        }
        if(user1.get().getWeight()==null || user.getWeight()!=null){
            update.set("weight",user.getWeight());
        }
        if(user1.get().getHeight()==null || user.getHeight()!=null){
            update.set("height",user.getHeight());
        }

        mongoTemplate.updateFirst(query,update,User.class);

        return Optional.of(user);
    }
}
