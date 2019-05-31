package com.ahgj.community.canal.service;

import com.ahgj.community.canal.bean.dao.PortBaseDao;

/**
 * 定时任务
 * @author Hohn
 */
public interface TaskSchedulerService {

    /**
     * 执行同步任务
     * @return
     */
    void executeThreadPoolTaskScheduler();
    /**
     * 执行数据库同步任务
     * @param baseDao
     * @return
     */
    int executeDataScheduler(PortBaseDao baseDao);
    /**
     * 执行http协议同步任务
     * @param baseDao
     * @return
     */
    int executeRestScheduler(PortBaseDao baseDao);

    /**
     * 关闭定时任务
     * @param future
     * @return
     */
    boolean shutThreadPoolTaskScheduler(String future);
}
