package edu.tongji.plantary.circle.service.impl;

import edu.tongji.plantary.circle.dao.PostDao;
import edu.tongji.plantary.circle.entity.Comment;
import edu.tongji.plantary.circle.entity.Post;
import edu.tongji.plantary.circle.service.PostService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

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
            post1.getUserCommentList().add(comment);
            postDao.save(post1);

            return Optional.of(comment);
        }else{
            return Optional.empty();
        }
    }

    @Override
    public List<Post> getPostByPosterPhone(String posterPhone) {
        return postDao.findByPosterPhone(posterPhone);
    }
}
