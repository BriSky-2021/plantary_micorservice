package edu.tongji.plantary.user.service;


import edu.tongji.plantary.user.entity.User;

import java.util.Optional;

public interface UserService {

    Optional<User> login(String phone,String passwd);
    Optional<User> register(String name,String phone,String passwd, String sex);

}
