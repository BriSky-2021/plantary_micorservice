package edu.tongji.plantary.schedule.dao;

import edu.tongji.plantary.schedule.entity.Task;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface TaskDao extends MongoRepository<Task,String> {

    @Query(value = "{'title':'?0'}")
    Optional<Task> findByTitle(String title);

}
