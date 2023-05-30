package edu.tongji.plantary.circle.task;


import edu.tongji.plantary.circle.entity.Theme;
import edu.tongji.plantary.circle.service.ThemeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class ScheduledTask {
    @Autowired
    private ThemeService themeService;

    /**
     * 每20秒执行一次
     * 每过20s，刷新主题圈状态。包括帖子总数与喜欢总数。
     */
    @Scheduled(cron = "*/20 * * * * ?")
    private void printNowDate() {
        //固定执行定时任务
        long nowDateTime = System.currentTimeMillis();
        System.out.println("固定定时任务执行:刷新主题圈状态--->"+nowDateTime+"，此任务为每20秒执行一次");
        //获取主题圈列表
        List<Theme> themes=themeService.getThemeList();
        //对每一个主体，操作：
        for (Theme theme:
             themes) {
            themeService.updateThemeStateByName(theme.getThemeName());
        }

    }
}
