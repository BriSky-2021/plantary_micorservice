package edu.tongji.plantary.circle.entity;

import lombok.Data;

@Data
public class Comment {
    String releaseTime;
    String content;
    UserItem userItem;
}
