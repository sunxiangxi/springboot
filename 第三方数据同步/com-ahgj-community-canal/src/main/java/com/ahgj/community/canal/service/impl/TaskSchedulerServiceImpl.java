package com.ahgj.community.canal.service.impl;

import com.ahgj.community.canal.bean.dao.PortBaseDao;
import com.ahgj.community.canal.quartz.DataDynamicTask;
import com.ahgj.community.canal.quartz.RestDynamicTask;
import com.ahgj.community.canal.service.ConfigPortBaseService;
import com.ahgj.community.canal.service.TaskSchedulerService;
import com.ahgj.community.canal.utils.AssertUtil;
import com.ahgj.community.canal.utils.SystemConstant;
import org.apache.commons.collections4.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;
import org.springframework.scheduling.support.CronTrigger;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.concurrent.ScheduledFuture;

/**
 * 执行连接Oracle数据库
 *
 * @author Hohn
 */
@Service
public class TaskSchedulerServiceImpl implements TaskSchedulerService {
    private static final Logger logger = LoggerFactory.getLogger(TaskSchedulerServiceImpl.class);


    /**
     * webSocket长链接也用到了ThreadPoolTaskScheduler,本项目线程池同步要用到自定义注入的线程池任务 canalSchedulerTask
     * ScheduleConfigBean配置器中注入的
     */
    @Resource(name = "canalSchedulerTask")
    private ThreadPoolTaskScheduler threadPoolTaskScheduler;

    private ScheduledFuture<?> DataDynamicTaskFuture;

    private ScheduledFuture<?> RestDynamicTaskFuture;

    @Autowired
    private ConfigPortBaseService configPortBaseService;


    @Override
    public void executeThreadPoolTaskScheduler() {
        //获取同步端基本设置信息
        List<PortBaseDao> portBaseDaoList = configPortBaseService.queryConnectInfoAll(SystemConstant.NOT_DELETE_STATUS_0);
        if (CollectionUtils.isNotEmpty(portBaseDaoList)) {
            for (PortBaseDao baseDao : portBaseDaoList) {
                //获取多源端连接方式 1: oracle 2: mysql 3:REST  4: webSocket协议
                Integer synchronizeWay = baseDao.getSynchronizeWay();
                //数据库同步方式
                if (synchronizeWay == 1 || synchronizeWay == 2) {
                    logger.info(synchronizeWay + "synchronizeWay=================>执行数据库同步定时任务");

                    executeDataScheduler(baseDao);
                }
                //http协议同步,目前只支持json数据格式
                if (synchronizeWay == 3) {
                    logger.info(synchronizeWay + "synchronizeWay=================>执行http协议同步定时任务");
                    executeRestScheduler(baseDao);
                }
            }

        }
    }

    @Override
    public int executeDataScheduler(PortBaseDao baseDao) {
        DataDynamicTaskFuture = threadPoolTaskScheduler.schedule(new DataDynamicTask(baseDao), new CronTrigger(baseDao.getCron()));
        AssertUtil.isNull(DataDynamicTaskFuture, "执行数据库同步任务异常", HttpStatus.BAD_REQUEST.value());
        return 1;
    }

    @Override
    public int executeRestScheduler(PortBaseDao baseDao) {
        RestDynamicTaskFuture = threadPoolTaskScheduler.schedule(new RestDynamicTask(baseDao), new CronTrigger(baseDao.getCron()));
        AssertUtil.isNull(RestDynamicTaskFuture, "执行http协议同步任务异常", HttpStatus.BAD_REQUEST.value());
        return 1;
    }

    @Override
    public boolean shutThreadPoolTaskScheduler(String future) {
        boolean status = false;
        if (future.equals(SystemConstant.DATA_DYNAMIC_TASK)) {
            if (DataDynamicTaskFuture != null) {
                status = DataDynamicTaskFuture.cancel(true);
            }
        }
        if (future.equals(SystemConstant.HTTP_DYNAMIC_TASK)) {
            if (RestDynamicTaskFuture != null) {
                status = RestDynamicTaskFuture.cancel(true);
            }
        }
        return status;
    }

}
