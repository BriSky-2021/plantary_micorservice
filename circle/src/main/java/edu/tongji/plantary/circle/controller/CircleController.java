package edu.tongji.plantary.circle.controller;

import edu.tongji.plantary.circle.entity.Comment;
import edu.tongji.plantary.circle.entity.Post;
import edu.tongji.plantary.circle.entity.UserItem;
import edu.tongji.plantary.circle.service.PostService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Api(tags = "用户接口")
@RestController
@RequestMapping("/CC")
public class CircleController {

    @Autowired
    PostService postService;

    @ApiOperation(value = "测试")
    @GetMapping("/test")
    @ResponseBody
    public String test(){
        return "ok";
    }

    @ApiOperation(value = "圈子用户接口")
    @GetMapping("/posts")
    @ResponseBody
    public List<Post> getAllPost(){
        return postService.getAllPosts();
    }

    @ApiOperation(value = "圈子用户接口")
    @PutMapping("/post/{postID}/comment")
    @ResponseBody
    public Comment addComment(@PathVariable String postID, UserItem userItem,String content){


        Date date = new Date();
        SimpleDateFormat dateFormat= new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");

        Comment comment=new Comment();
        comment.setReleaseTime(dateFormat.format(date));
        comment.setUserItem(userItem);
        comment.setContent(content);

        Optional<Comment> comment1= postService.addComment(postID,comment);
        if (comment1.isPresent()){
            return comment1.get();
        }else{
            return null;
        }

    }

    @ApiOperation(value = "圈子用户接口")
    @GetMapping("/post/{posterPhone}")
    @ResponseBody
    public List<Post> getPostByPosterPhone(@PathVariable String posterPhone){
        return postService.getPostByPosterPhone(posterPhone);
    }

}
