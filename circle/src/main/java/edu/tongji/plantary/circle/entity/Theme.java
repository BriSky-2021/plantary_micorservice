package edu.tongji.plantary.circle.entity;

import lombok.Data;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

@Data
@Document("Theme")
public class Theme {
    String themeName;
    String themePicture;
    Integer postsCount;
    Integer likesCount;
    List<String> postsIds;
}
