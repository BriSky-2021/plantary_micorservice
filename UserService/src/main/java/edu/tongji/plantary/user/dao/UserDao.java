package edu.tongji.plantary.user.dao;

import edu.tongji.plantary.user.entity.User;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserDao extends MongoRepository<User,String> {


    @Query(value = "{'phone':'?0','passwd':'?1'}")
    Optional<User> validatePasswd(String phone, String passwd);

    @Query(value = "{'name':'?0'}")
    List<User> findByName(String name);

    @Query(value = "{'phone':'?0'}")
    Optional<User> findByPhone(String phone);

    @Query(value = "{'passwd':'?0'}")
    List<User> findByPasswd(String passwd);

}
