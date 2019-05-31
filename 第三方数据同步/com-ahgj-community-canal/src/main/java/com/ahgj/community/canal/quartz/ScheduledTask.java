package com.ahgj.community.canal.quartz;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.text.SimpleDateFormat;
import java.util.Date;

//@Component
public class ScheduledTask {
    private static final Logger logger = LoggerFactory.getLogger(ScheduledTask.class);
    private static final SimpleDateFormat dateFormat = new SimpleDateFormat("YYYY-MM-DD HH:mm:ss");

    /**
     * fixedRate 每隔5秒执行, 单位：ms。
     */
   //@Scheduled(fixedRate = 5000)
    public void fixRate() {
       //执行业务
       logger.info("我每隔5秒冒泡一次：" + dateFormat.format(new Date()));
    }

    /**
     * cron某个时刻触发执行定时任务
     *
     */
   // @Scheduled(cron = "0 0 1 * * ?")
    public void refreshCron() {
        //执行业务
        logger.info("每天凌晨1点冒泡一次：" + dateFormat.format(new Date()));

    }
}

