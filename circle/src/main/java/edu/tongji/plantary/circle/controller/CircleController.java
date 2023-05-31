package edu.tongji.plantary.circle.controller;

import edu.tongji.plantary.circle.dao.ThemeDao;
import edu.tongji.plantary.circle.entity.Comment;
import edu.tongji.plantary.circle.entity.Post;
import edu.tongji.plantary.circle.entity.Theme;
import edu.tongji.plantary.circle.entity.UserItem;
import edu.tongji.plantary.circle.service.PostService;
import edu.tongji.plantary.circle.service.ThemeService;
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

    @Autowired
    ThemeService themeService;


    @ApiOperation(value = "获得所有帖子")
    @GetMapping("/posts")
    @ResponseBody
    public List<Post> getAllPost(){
        return postService.getAllPosts();
    }


    @ApiOperation(value = "获得某主题圈的所有帖子")
    @GetMapping("/postByThemeName")
    @ResponseBody
    public List<Post> getPostsByThemeName(String themeName) {
        return postService.getPostsByThemeName(themeName);
    }

    @ApiOperation(value = "发评论")
    @PutMapping("/post/{postID}/comment")
    @ResponseBody
    public Comment addComment(@PathVariable String postID, UserItem userItem,String content){

        //获取时间
        Date date = new Date();
        SimpleDateFormat dateFormat= new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        //构造
        Comment comment=new Comment();
        comment.setReleaseTime(dateFormat.format(date));
        comment.setUserItem(userItem);
        comment.setContent(content);
        //调用服务
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

    @ApiOperation(value = "在主题圈发帖子")
    @PutMapping("/postByThemeName")
    @ResponseBody
    public Optional<Post> putPostByThemeName(String themeName, String postContent,String postPicture,UserItem userItem){
        return postService.putPostByThemeName(themeName,postContent,postPicture,userItem);
    }

    @ApiOperation(value = "多图片发帖子")
    @PutMapping("/postByPictures")
    @ResponseBody
    public Optional<Post> putPost( String postContent,List<String> postPictures,UserItem userItem){
        return postService.putPostByPictures(postContent,postPictures,userItem);
    }


    //*****************
    //以下为ThemeService
    //*****************

    @ApiOperation(value = "获取所有圈子")
    @GetMapping("/themes")
    @ResponseBody
    public List<Theme> getThemes(){
        return themeService.getThemeList();
    }


    @ApiOperation(value = "通过名字获取圈子")
    @GetMapping("/theme/{themeName}")
    @ResponseBody
    public Optional<Theme> getThemeByName(@PathVariable String themeName){
        return themeService.findByName(themeName);
    }


    @ApiOperation(value = "更新主题圈状态")
    @PostMapping("/theme/{themeName}")
    @ResponseBody
    public void updateThemeStateByName(@PathVariable String themeName){
        themeService.updateThemeStateByName(themeName);
    }


    @ApiOperation(value = "添加主题圈")
    @PutMapping("/theme/{themeName}")
    @ResponseBody
    Optional<Theme> addTheme(@PathVariable String themeName,String themePicture){
        return themeService.addTheme(themeName,themePicture);
    }


}
