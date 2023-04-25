package edu.tongji.plantary.circle.dao;


import edu.tongji.plantary.circle.entity.Post;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PostDao extends MongoRepository<Post,String> {

    @Query(value = "{'poster.phone':'?0'}")
    List<Post> findByPosterPhone(String phone);


}
