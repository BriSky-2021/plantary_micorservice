import edu.tongji.plantary.circle.entity.Comment;
import edu.tongji.plantary.circle.entity.Post;
import edu.tongji.plantary.circle.entity.UserItem;
import edu.tongji.plantary.circle.service.PostService;
import edu.tongji.plantary.circle.service.impl.PostServiceImpl;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;


import java.text.SimpleDateFormat;
import java.util.*;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = {edu.tongji.plantary.circle.CircleApplication.class})
public class PostTest {

    @Autowired
    private PostService postService;

    @Test
    public void addPostTest(){



        Post post=new Post();
        post.setContent("健身100天第一天");
        post.setPics(Arrays.asList("http://img.daimg.com/uploads/allimg/220803/3-220P31644440-L.jpg"));

        UserItem user1=new UserItem();
        user1.setName("张三");
        user1.setPicture("http://img.daimg.com/uploads/allimg/220803/3-220P31520290-L.jpg");
        user1.setPhone("1234321");

        UserItem user2=new UserItem();
        user2.setName("李四");
        user2.setPicture("http://img.daimg.com/uploads/allimg/220803/3-220P31520290-L.jpg");
        user2.setPhone("123454321");

        UserItem user3=new UserItem();
        user3.setName("王五");
        user3.setPicture("http://img.daimg.com/uploads/allimg/220803/3-220P31520290-L.jpg");
        user3.setPhone("12345678910");

        post.setPoster(user2);
        post.setUserLikedList(Arrays.asList(user1,user2,user3));

        Comment comment1=new Comment();
        comment1.setContent("不错子");
        comment1.setReleaseTime("2001-02-03 16:43:23");
        comment1.setUserItem(user2);

        Comment comment2=new Comment();
        comment2.setContent("加油");
        comment2.setReleaseTime("2011-02-03 18:49:43");
        comment2.setUserItem(user3);

        post.setUserCommentList(Arrays.asList(comment1,comment2));

        for (Comment c:post.getUserCommentList()
             ) {
            System.out.println(c.getContent());
        }

        Optional<Post> postRet= postService.addPost(post);

        if(!postRet.isPresent()){
            System.out.println("添加失败");
        }

    }


    @Test
    public void getPosts(){
        List<Post> comments=postService.getAllPosts();
        if(comments.size()!=0){
            System.out.println(comments.get(0));
        }
    }

    @Test
    public void addComment(){

        UserItem user2=new UserItem();
        user2.setName("李四");
        user2.setPicture("http://img.daimg.com/uploads/allimg/220803/3-220P31520290-L.jpg");
        user2.setPhone("123454321");

        Date date = new Date();
        SimpleDateFormat dateFormat= new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");

        Comment comment=new Comment();
        comment.setUserItem(user2);
        comment.setContent("真棒！");
        comment.setReleaseTime(dateFormat.format(date));

        List<Post> posts=postService.getAllPosts();
        String postID=posts.get(0).get_id();

        postService.addComment(postID,comment);

    }

}
