package edu.tongji.plantary.circle.dao;

import edu.tongji.plantary.circle.entity.Post;
import edu.tongji.plantary.circle.entity.Theme;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ThemeDao extends MongoRepository<Theme,String> {

    @Query(value = "{'themeName':'?0'}")
    Optional<Theme> findByName(String themeName);

}
