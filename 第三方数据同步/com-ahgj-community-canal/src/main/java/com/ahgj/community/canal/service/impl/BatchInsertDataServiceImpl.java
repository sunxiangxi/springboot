package com.ahgj.community.canal.service.impl;

import com.ahgj.community.canal.bean.dao.PortBaseDao;
import com.ahgj.community.canal.bean.dao.SynchronizationResultDao;
import com.ahgj.community.canal.utils.DatabaseUtil;
import com.ahgj.community.canal.utils.ExceptionInformationUtil;
import com.ahgj.community.canal.utils.SpringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 数据分批插入结果
 *
 * @author Hohn
 */
@Service
public class BatchInsertDataServiceImpl {
    private static final Logger logger = LoggerFactory.getLogger(BatchInsertDataServiceImpl.class);

    /**
     * 全量更新插入
     *
     * @param metaDataList
     * @return
     */
    public int executeFullInsert(List<Map<String, Object>> metaDataList, SynchronizationResultDao synchronizationResultDao, List<String> failureReasonsList,PortBaseDao portBaseDao) {
        //定义插入数据成功数量
        int piecesNumber = 0;
        try {
            DataSource dataSource = SpringUtils.getBean(DataSource.class);
            Connection conn = dataSource.getConnection();
            //执行truncate删除操作
            DatabaseUtil.executeScript(conn, "TRUNCATE TABLE " + portBaseDao.getTargetTableName());
            //执行jdbcTemplate批量插入
            piecesNumber = batchInsertJdbc(metaDataList, failureReasonsList, conn,portBaseDao);

        } catch (Exception e) {
            //获取异常日志内容
            String exceptionInformation = ExceptionInformationUtil.getExceptionAllInformation(e);
            failureReasonsList.add(exceptionInformation);
            logger.info("===============" + exceptionInformation);
            e.printStackTrace();

        }
        return piecesNumber;
    }

    /**
     * 分批批量插入
     *
     * @param batchArgs
     */
    public int batchInsertJdbc(List<Map<String, Object>> batchArgs, List<String> failureReasonsList, Connection connection,PortBaseDao portBaseDao) {
        int piecesNumbers = 0;
        try {
            //配置项中分批执行条数
            Integer executionsNumber = portBaseDao.getExecutionsNumber();
            //插入数据list开始截取索引列/结束截取索引列
            int fromIndex = 0;
            int toIndex = executionsNumber;
            //结束截取索引列不等于数据长度时截取数据长度执行执行jdbcTemplate 插入
            while (fromIndex != batchArgs.size()) {
                if (toIndex > batchArgs.size()) {
                    toIndex = batchArgs.size();
                }

                //根据list开始和结束索引列截取list分批插入
                List<Map<String, Object>> subList = batchArgs.subList(fromIndex, toIndex);
                int piecesNumber = DatabaseUtil.insertAll(portBaseDao.getTargetTableName(), subList, connection);
                //重新赋值数据list开始截取索引列/结束截取索引列
                fromIndex = toIndex;
                toIndex += executionsNumber;
                //结束截取索引列大于数据长度时赋值数据list长度
                if (toIndex > batchArgs.size()) {
                    toIndex = batchArgs.size();
                }
                piecesNumbers += piecesNumber;
            }
        } catch (Exception e) {
            e.getMessage();
            //获取异常日志内容
            String exceptionInformation = ExceptionInformationUtil.getExceptionAllInformation(e);
            logger.info("执行插入异常=============" + exceptionInformation);
            failureReasonsList.add(exceptionInformation);
        } finally {
            if (connection != null) {
                try {
                    connection.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
        return piecesNumbers;
    }


    /**
     * 增量更新数据
     *
     * @param metaDataList
     * @return
     */
    public int executeIncrement(List<Map<String, Object>> metaDataList, SynchronizationResultDao synchronizationResultDao, List<String> failureReasonsLis,PortBaseDao portBaseDao) {
        //定义插入数据成功数量
        int piecesNumber = 0;
        try {
            DataSource dataSource = SpringUtils.getBean(DataSource.class);
            Connection connection = dataSource.getConnection();
            //todo 分批更新
             piecesNumber = DatabaseUtil.update(portBaseDao.getTargetTableName(), null,null, connection);

        } catch (Exception e) {
            e.printStackTrace();
            //获取异常日志内容
            String exceptionInformation = ExceptionInformationUtil.getExceptionAllInformation(e);
            logger.info("===============" + exceptionInformation);
            failureReasonsLis.add(exceptionInformation);
        }
        return piecesNumber;
    }


    /**
     * 分批批量插入   jdbcTemple插入 未知的列类型
     *
     * @param batchArgs
     */
    public void batchInsertJdbc2(List<Object[]> batchArgs, List<String> failureReasonsList,PortBaseDao portBaseDao) {
        try {
            //==========================
            // 拼接插入sql
            //==========================
            StringBuffer sqlBuffer = new StringBuffer();
            //1.1 获取目标表拼接
            sqlBuffer.append("INSERT INTO ").append(portBaseDao.getTargetTableName()).append("(").append(portBaseDao.getTargetTableField()).append(") ").append("VALUES (");
            //1.2 获取目标表字段拼接
            String targetTableField = portBaseDao.getTargetTableField();
            //1.3 根据目标表字段个数拼接?
            List<String> fieldList = Arrays.asList(targetTableField.split(",")).stream().map(s -> (s.trim())).collect(Collectors.toList());
            for (int i = 0; i < fieldList.size() - 1; i++) {
                sqlBuffer.append("?,");
            }
            sqlBuffer.append("?)");
            JdbcTemplate jdbcTemplate = SpringUtils.getBean(JdbcTemplate.class);

            //配置项中分批执行条数
            Integer executionsNumber = portBaseDao.getExecutionsNumber();
            //插入数据list开始截取索引列/结束截取索引列
            int fromIndex = 0;
            int toIndex = executionsNumber;
            //结束截取索引列不等于数据长度时截取数据长度执行执行jdbcTemplate 插入
            while (fromIndex != batchArgs.size()) {
                if (toIndex > batchArgs.size()) {
                    toIndex = batchArgs.size();
                }
                String toString = sqlBuffer.toString();
                logger.info("执行sql=============" + toString);
                //根据list开始和结束索引列截取list分批插入
                List<Object[]> subList = batchArgs.subList(fromIndex, toIndex);
                //执行jdbcTemplate 插入
                jdbcTemplate.batchUpdate(sqlBuffer.toString(), subList);

                //重新赋值数据list开始截取索引列/结束截取索引列
                fromIndex = toIndex;
                toIndex += executionsNumber;
                //结束截取索引列大于数据长度时赋值数据list长度
                if (toIndex > batchArgs.size()) {
                    toIndex = batchArgs.size();
                }
            }
        } catch (Exception e) {
            e.getMessage();
            //获取异常日志内容
            String exceptionInformation = ExceptionInformationUtil.getExceptionAllInformation(e);
            logger.info("执行插入异常=============" + exceptionInformation);
            failureReasonsList.add(exceptionInformation);
        }

    }
}
