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

    @ApiOperation(value = "获得所有帖子")
    @GetMapping("/posts")
    @ResponseBody
    public List<Post> getAllPost(){
        return postService.getAllPosts();
    }

    @ApiOperation(value = "发评论")
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

    @ApiOperation(value = "用电话获取该博主所有帖子")
    @GetMapping("/post/{posterPhone}")
    @ResponseBody
    public List<Post> getPostByPosterPhone(@PathVariable String posterPhone){
        return postService.getPostByPosterPhone(posterPhone);
    }

    @ApiOperation(value = "用postID获取评论内容")
    @GetMapping("/comment/{postID}")
    @ResponseBody
    public List<Comment> getCommentByPostID(@PathVariable String postID){
        return postService.getCommentByPostID(postID);
    }

    @ApiOperation(value = "发帖子")
    @PutMapping("/post")
    @ResponseBody
    public Optional<Post> putPost( String postContent,String postPicture,UserItem userItem){
        return postService.putPost(postContent,postPicture,userItem);
    }

    @ApiOperation(value = "多图片发帖子")
    @PutMapping("/postByPictures")
    @ResponseBody
    public Optional<Post> putPost( String postContent,List<String> postPictures,UserItem userItem){
        return postService.putPostByPictures(postContent,postPictures,userItem);
    }

}
