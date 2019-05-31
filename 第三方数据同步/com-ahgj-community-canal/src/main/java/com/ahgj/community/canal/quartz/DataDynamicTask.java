package com.ahgj.community.canal.quartz;

import com.ahgj.community.canal.bean.dao.DataSynchronizationRelDao;
import com.ahgj.community.canal.bean.dao.PortBaseDao;
import com.ahgj.community.canal.bean.dao.SynchronizationResultDao;
import com.ahgj.community.canal.service.impl.BatchInsertDataServiceImpl;
import com.ahgj.community.canal.service.impl.ConfigPortBaseServiceImpl;
import com.ahgj.community.canal.service.impl.DbConnectServiceImpl;
import com.ahgj.community.canal.service.impl.SynchronizationResultServiceImpl;
import com.ahgj.community.canal.utils.DateUtil;
import com.ahgj.community.canal.utils.SpringUtils;
import org.apache.commons.collections4.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * 数据库同步执行任务
 * <br>🌹 注意:
 * <br>🌹    使用@Autowired @Resource控制反转原理获取注入对象会为null,
 * <br>🌹    因为在容器启动开始定时任务时未完成实例化而且线程定时任务的根据反射机制动态生成的,
 * <br>🌹    需要动态获取,要使用项目中SpringUtils.getBean方法
 *
 * @author Hohn
 * <br>🌹model:
 */
public class DataDynamicTask implements Runnable {
    private static final Logger logger = LoggerFactory.getLogger(DataDynamicTask.class);

    /**
     * 多源端基本信息
     */
    private PortBaseDao portBaseDao;

    public DataDynamicTask(PortBaseDao portBaseDao) {
        this.portBaseDao = portBaseDao;
    }


    @Override
    public void run() {
        //定义同步结果信息
        SynchronizationResultDao synchronizationResultDao = new SynchronizationResultDao();
        //定义异常信息
        List<String> failureReasonsList = new ArrayList<>();

        String startTime = DateUtil.getCurrentFormatDateTime();
        logger.info("============Rest同步任务开始时间：" + startTime);
        //执行数据库同步业务,并设置同步结果信息
        executeDBSynchronization(synchronizationResultDao, failureReasonsList);
        SynchronizationResultServiceImpl synchronizationResultServiceImpl = SpringUtils.getBean(SynchronizationResultServiceImpl.class);
        //插入同步结果信息
        synchronizationResultServiceImpl.setSynchronizationResult(synchronizationResultDao, failureReasonsList, startTime);
    }


    /**
     * 执行数据库同步
     *
     * @return
     */
    private int executeDBSynchronization(SynchronizationResultDao synchronizationResultDao, List<String> failureReasonsList) {
        int piecesNumber = 0;
        ConfigPortBaseServiceImpl connectConfigService = SpringUtils.getBean(ConfigPortBaseServiceImpl.class);
        //查询数据库连接配置信息
        DataSynchronizationRelDao dataConnectPortRelDao = connectConfigService.queryDataConnectPortRel(this.portBaseDao.getId());
        if (dataConnectPortRelDao != null) {
            DbConnectServiceImpl oracleConnectService = SpringUtils.getBean(DbConnectServiceImpl.class);
            //根据配置执行sql读取第三方数据
            List<Map<String, Object>> metaDataList = oracleConnectService.OracleConnect4Map(failureReasonsList, dataConnectPortRelDao, this.portBaseDao);
            if (CollectionUtils.isNotEmpty(metaDataList)) {

                //获取同步类型 1: 全量同步 2: 增量同步
                Integer synchronizeType = this.portBaseDao.getSynchronizeType();
                BatchInsertDataServiceImpl batchInsertDataService = SpringUtils.getBean(BatchInsertDataServiceImpl.class);
                //全量--数据分批批量插入
                if (synchronizeType == 1) {
                    //插入数据全量插入,并设置异常信息和同步结果集信息
                    piecesNumber = batchInsertDataService.executeFullInsert(metaDataList, synchronizationResultDao, failureReasonsList, portBaseDao);
                    SynchronizationResultServiceImpl synchronizationResultServiceImpl = SpringUtils.getBean(SynchronizationResultServiceImpl.class);
                    //设置同步结果集信息  同步数据量/成功条数/失败条数/成功率
                    synchronizationResultServiceImpl.setSynchronizationResult(synchronizationResultDao, piecesNumber, metaDataList.size());

                }
                //分批修改数据
                if (synchronizeType == 2) {
                    //todo 增量 同步
                    batchInsertDataService.executeIncrement(metaDataList, synchronizationResultDao, failureReasonsList,portBaseDao);
                }
            } else {
                //设置同步结果集异常信息
                failureReasonsList.add("根据数据库配置获取第三方数据为空");
            }
            return piecesNumber;
        }
        return 0;
    }


}