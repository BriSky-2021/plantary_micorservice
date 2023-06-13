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

import java.util.Arrays;
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
        if (phone == null || phone.isEmpty()) {
            throw new IllegalArgumentException("Phone number cannot be null or empty.");
        }

        if (passwd == null || passwd.isEmpty()) {
            throw new IllegalArgumentException("Password cannot be null or empty.");
        }

        if (!phone.matches("\\d{11}")) {
            throw new IllegalArgumentException("Phone number must be 11 digits.");
        }

        if (passwd.length() < 4 || passwd.length() > 50) {
            throw new IllegalArgumentException("Password length must be between 4 and 50.");
        }

        return userDao.validatePasswd(phone,passwd);
    }

    // TODO: 测试这个函数
    @Override
    public Optional<User> register(String name, String phone, String passwd ,String sex) {

        //先对关键属性进行检查
        if(name==null || phone==null || passwd ==null ){
            return Optional.empty();
        }

        //查找phone有没有被注册
        Optional<User> users=userDao.findByPhone(phone);
        if(users.isPresent()){//电话号码已经被注册，则不予注册
            System.out.println(users.get());
            System.out.println("电话号码已被注册");
            return Optional.empty();
        }

        //再进行注册
        User user = new User();

        //检测性别是不是三种之一
        List<String> avalible_sexs= Arrays.asList("男","女","非二元性别");

        //如果不选择性别或者性别并非给定的三种之一
        if(sex==null || !avalible_sexs.contains(sex)){
            user.setSex("非二元性别");
        }else{
            user.setSex(sex);
        }
        //设置属性
        user.setPhone(phone);
        user.setName(name);
        user.setPasswd(passwd);
        //设置头像默认值
        user.setAvatar("https://img.ddtouxiang.com/upload/images/20230529/2023052908353959421.jpg");
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

    // TODO: 测试这个函数 正交法
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
