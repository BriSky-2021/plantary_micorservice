package edu.tongji.plantary.circle.service.impl;

import edu.tongji.plantary.circle.dao.PostDao;
import edu.tongji.plantary.circle.entity.Comment;
import edu.tongji.plantary.circle.entity.Post;
import edu.tongji.plantary.circle.entity.UserItem;
import edu.tongji.plantary.circle.service.PostService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.*;

@Service
public class PostServiceImpl implements PostService {

    @Autowired
    private MongoTemplate mongoTemplate;

    @Autowired
    private PostDao postDao;

    @Override
    public List<Post> getAllPosts() {
        List<Post> posts= postDao.findAll();
        return posts;
    }

    @Override
    public Optional<Post> addPost(Post post) {

        Post ret=mongoTemplate.insert(post);

        if(ret==null)return Optional.empty();
        else return Optional.of(ret);
    }

    @Override
    public Optional<Post> deletePost(String postID) {
        return Optional.empty();
    }

    @Override
    public Optional<Comment> addComment(String postID, Comment comment) {

        Optional<Post> post= postDao.findById(postID);
        if(post.isPresent()){

            Post post1=post.get();

            if(post1.getUserCommentList()==null){
                post1.setUserCommentList(new ArrayList<>());
            }


            post1.getUserCommentList().add(comment);
            postDao.save(post1);

            return Optional.of(comment);
        }else{
            return Optional.empty();
        }
    }

    @Override
    public List<Comment> getCommentByPostID(String postID) {
        Optional<Post> post=postDao.findById(postID);
        List<Comment> comments=new ArrayList<>();
        if(post.isPresent()){
            return post.get().getUserCommentList();
        }else{
            return comments;
        }
    }

    @Override
    public List<Post> getPostByPosterPhone(String posterPhone) {
        return postDao.findByPosterPhone(posterPhone);
    }

    @Override
    public Optional<Post> putPost(String postContent, String postPicture, UserItem userItem) {

        Post post=new Post();
        post.setContent(postContent);
        post.setPics(Arrays.asList(postPicture));
        post.setPoster(userItem);
        Date date = new Date();
        SimpleDateFormat dateFormat= new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        post.setReleaseTime(dateFormat.format(date));
        Post ret=mongoTemplate.insert(post);

        if(ret==null){
            return Optional.empty();
        }else{
            return Optional.of(ret);
        }

    }

    @Override
    public Optional<Post> putPostByThemeName(String ThemeName, String postContent, String postPicture, UserItem userItem) {
        Post post=new Post();
        post.setContent(postContent);
        post.setPics(Arrays.asList(postPicture));
        post.setPoster(userItem);
        post.setThemeName(ThemeName);
        Date date = new Date();
        SimpleDateFormat dateFormat= new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        post.setReleaseTime(dateFormat.format(date));
        Post ret=mongoTemplate.insert(post);

        if(ret==null){
            return Optional.empty();
        }else{
            return Optional.of(ret);
        }
    }

    @Override
    public Optional<Post> putPostByPictures(String postContent, List<String> postPictures, UserItem userItem) {

        Post post=new Post();
        post.setContent(postContent);
        post.setPics(postPictures);
        post.setPoster(userItem);
        Date date = new Date();
        SimpleDateFormat dateFormat= new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        post.setReleaseTime(dateFormat.format(date));
        Post ret=mongoTemplate.insert(post);

        if(ret==null){
            return Optional.empty();
        }else{
            return Optional.of(ret);
        }

    }

    @Override
    public List<Post> getPostsByThemeName(String themeName) {
        return postDao.findByThemeName(themeName);
    }

}
