package edu.tongji.plantary.circle.service.impl;

import edu.tongji.plantary.circle.dao.PostDao;
import edu.tongji.plantary.circle.dao.ThemeDao;
import edu.tongji.plantary.circle.entity.Post;
import edu.tongji.plantary.circle.entity.Theme;
import edu.tongji.plantary.circle.service.ThemeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class ThemeServiceImpl implements ThemeService {

    @Autowired
    ThemeDao themeDao;

    //TODO:可能引起问题
    @Autowired
    PostDao postDao;

    @Autowired
    private MongoTemplate mongoTemplate;

    @Override
    public List<Theme> getThemeList() {
        return themeDao.findAll();
    }

    @Override
    public Optional<Theme> findByName(String themeName) {
        return themeDao.findByName(themeName);
    }

    @Override
    public void updateThemeStateByName(String themeName) {


        Optional<Theme> theme=themeDao.findByName(themeName);
        if(!theme.isPresent()){
            return;
        }

        List<Post> postList=postDao.findByThemeName(themeName);
        Integer postCount=postList.size();
        Integer likesCount=0;
        for (Post post:
             postList) {
            if(post.getUserLikedList()!=null){
                likesCount+=post.getUserLikedList().size();
            }
        }

        Query query = new Query(Criteria.where("themeName").is(themeName));
        Update update = new Update();
        update.set("postsCount",postCount);
        update.set("likesCount",likesCount);
        //TODO postIDS没有赋值
        mongoTemplate.updateFirst(query,update,Theme.class);


    }

    @Override
    public Optional<Theme> addTheme(String themeName, String themePicture) {

        //存在同名的就不添加了
        Optional<Theme> themeRet=themeDao.findByName(themeName);
        if(themeRet.isPresent()){
            return Optional.empty();
        }

        Theme theme=new Theme();
        theme.setThemeName(themeName);
        theme.setThemePicture(themePicture);
        theme.setLikesCount(0);
        theme.setPostsCount(0);
        theme.setPostsIds(new ArrayList<>());

        Theme result= themeDao.save(theme);

        return Optional.of(result);
    }
}
