package edu.tongji.plantary.circle.service;


import edu.tongji.plantary.circle.entity.Post;
import edu.tongji.plantary.circle.entity.Theme;

import java.util.List;
import java.util.Optional;

public interface ThemeService {

    List<Theme> getThemeList();

    Optional<Theme> findByName(String themeName);

    void updateThemeStateByName(String themeName);

    Optional<Theme> addTheme(String themeName,String themePicture);


}
