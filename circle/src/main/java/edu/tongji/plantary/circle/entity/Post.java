package edu.tongji.plantary.circle.entity;

import lombok.Data;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

@Data
@Document("Post")
public class Post {
    String _id;//自动生成，不必赋值
    UserItem poster;
    String content;
    List<String> pics;
    List<UserItem> userLikedList;
    List<Comment> userCommentList;
}
