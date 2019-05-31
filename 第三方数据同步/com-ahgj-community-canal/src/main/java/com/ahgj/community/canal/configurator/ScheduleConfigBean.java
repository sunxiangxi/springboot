package com.ahgj.community.canal.configurator;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.scheduling.Trigger;
import org.springframework.scheduling.TriggerContext;
import org.springframework.scheduling.annotation.SchedulingConfigurer;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;
import org.springframework.scheduling.config.ScheduledTaskRegistrar;
import org.springframework.scheduling.support.CronTrigger;

import java.util.Date;
/**
 * 配置线程池任务，任务时间可配置化
 * <br>🌹
 * @author Hohn
 * <br>🌹model:
 */
@Configuration
public class ScheduleConfigBean implements SchedulingConfigurer {


    /**
     * 添加定时任务到线程池
     * @param scheduledTaskRegistrar
     */
    @Override
    public void configureTasks(ScheduledTaskRegistrar scheduledTaskRegistrar) {
        scheduledTaskRegistrar.setTaskScheduler(getScheduler());
    }

    /**
     * 线程池配置类
     * @return
     */
    @Primary
    @Bean(name = "canalSchedulerTask",destroyMethod = "shutdown")
    public ThreadPoolTaskScheduler getScheduler(){
        ThreadPoolTaskScheduler taskScheduler=new ThreadPoolTaskScheduler();
        taskScheduler.setPoolSize(100);
        //设置线程池中任务的等待时间,超过这个时候还没有销毁就强制销毁,确保应用最后能够被关闭，而不是阻塞住
        taskScheduler.setAwaitTerminationSeconds(60);
        //设置线程任务可以方便我们定位处理任务所在的线程池
        taskScheduler.setThreadNamePrefix("community-canal-task → ");
        //设置线程池关闭的时候等待所有任务都完成再继续销毁其他的Bean
        taskScheduler.setWaitForTasksToCompleteOnShutdown(true);
        return taskScheduler;
    }

    /**
     * 设置定时触发器,动态配置触发任务时间
     * threadPoolTaskScheduler.schedule(new DataDynamicTask(baseDao), new CronTrigger(baseDao.getCron()))
     * @return
     */
    private Trigger getTrigger(String cron){
        return new Trigger() {
            @Override
            public Date nextExecutionTime(TriggerContext triggerContext) {
                CronTrigger trigger=new CronTrigger(cron);
                return trigger.nextExecutionTime(triggerContext);
            }
        };
    }
}