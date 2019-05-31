package com.ahgj.community.canal.controller;

import com.ahgj.community.canal.service.TaskSchedulerService;
import com.ahgj.community.canal.utils.ReturnResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.PostConstruct;

/**
 * 定时任务
 * 动态配置定时时间可参考 http://www.cnblogs.com/Mblood/p/9816152.html
 *
 * @author Hohn
 * <br>🌹model:
 */
@RestController
@RequestMapping(value = "/scheduleApi")
public class ScheduleController {
    private static final Logger logger = LoggerFactory.getLogger(ScheduleController.class);
    @Autowired
    private TaskSchedulerService taskSchedulerService;


    /**
     * 默认启动
     * 服务加载Servlet的时候运行，并且只会被服务器调用一次，类似于Servlet的inti()方法。
     */
    @PostConstruct
    private void autoRun() {
        runSchedulerTask();
    }

    /**
     * 运行定时任务
     * 查询系统的同步配置,启动相关定时任务
     * @return
     */
    @GetMapping(value = "/runSchedulerTask")
    public ReturnResult runSchedulerTask() {
        taskSchedulerService.executeThreadPoolTaskScheduler();
        return new ReturnResult(HttpStatus.OK, "定时任务执行成功");

    }

    /**
     * 关闭定时任务
     *
     * @param future DataDynamicTaskFuture: 数据库同步任务 RestDynamicTaskFuture: rest同步任务
     * @return
     */
    @GetMapping(value = "/stopDynamicTask/{future}")
    public ReturnResult stopDynamicTask(@PathVariable("future") String future) {
        boolean status = taskSchedulerService.shutThreadPoolTaskScheduler(future);
        if (status) {
            return new ReturnResult(HttpStatus.BAD_REQUEST, "定时任务关闭失败");
        }
        return new ReturnResult(HttpStatus.OK, "定时任务已关闭");
    }


    /**
     * 更改时间马上执行定时任务
     *
     * @param future  定时任务标识
     * @param cronTime
     * @param canalMark 多端标识
     * @return
     */
//    @GetMapping(value = "/updateDynamicTask/{future}/{cronTime}/{canalMark}")
//    public Object change(@PathVariable("future") String future, @PathVariable("cronTime") String cronTime, @PathVariable("canalMark") String canalMark) {
//        //https://www.cnblogs.com/xuange306/p/6250740.html
//        String cron = "*/" + cronTime + " * * * * *";
//        //根据同步方式标识获取对应的配置文件/配置数据
//        Object object = null;
//        stopDynamicTask(future);
//        if (future.equals("DynamicTask1Future")) {
//            //cronService.updateById(cron,1);
//            // DynamicTask1Future = threadPoolTaskScheduler.schedule(new DynamicTaskData(canalMark, object), new CronTrigger(cron));
//        }
//        if (future.equals("DynamicTask2Future")) {
//            //cronService.updateById(cron,2);
//            //DynamicTask2Future = threadPoolTaskScheduler.schedule(new DynamicTaskHttp(), new CronTrigger(cron));
//        }
//
//        return new ReturnResult(HttpStatus.OK, "");
//    }


}