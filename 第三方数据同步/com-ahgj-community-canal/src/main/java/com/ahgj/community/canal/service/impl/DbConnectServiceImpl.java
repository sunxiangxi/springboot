package com.ahgj.community.canal.service.impl;

import com.ahgj.community.canal.bean.dao.DataSynchronizationRelDao;
import com.ahgj.community.canal.bean.dao.PortBaseDao;
import com.ahgj.community.canal.utils.DatabaseUtil;
import com.ahgj.community.canal.utils.ExceptionInformationUtil;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 执行连接数据库
 * @author Hohn
 */
@Service
public class DbConnectServiceImpl {
    private static final Logger logger = LoggerFactory.getLogger(DbConnectServiceImpl.class);

    /**
     * 获取数据库连接对象
     *
     * @param relDao
     * @return
     */
    public Connection getDataConnection(DataSynchronizationRelDao relDao) throws ClassNotFoundException, SQLException {

            Class.forName(relDao.getDriverClassName());
            String url = relDao.getDatasourceUrl();
            // 用户名,系统默认的账户名
            String user = relDao.getDatasourceUsername();
            // 密码
            String password = relDao.getDatasourcePassword();
            //创建一个数据库连接
            return DriverManager.getConnection(url, user, password);
    }


    public List<Object[]> OracleConnect4Array(List<String> failureReasonsList, DataSynchronizationRelDao relDao, PortBaseDao portBaseDao) {
        try {
            Connection dataConnection = getDataConnection(relDao);
            logger.info("连接数据库成功！");
            // 预编译语句，“？”代表参数
            String sql = relDao.getQueryScript();
            //获取配置参数
            String queryParams = relDao.getQueryParams();
            //查询参数转换list
            List<Object> paramList = Arrays.asList(queryParams.split(",")).stream().map(s -> (s.trim())).collect(Collectors.toList());
            return DatabaseUtil.executeQuery4ParamsReturnArray(dataConnection, sql, paramList);
        } catch (Exception e) {
            e.printStackTrace();
            String exceptionInformation = ExceptionInformationUtil.getExceptionAllInformation(e);
            failureReasonsList.add(exceptionInformation);
        }
        return null;
    }


    /**
     * <br>🌹author Hohn
     * <br>👇请求地址:
     * <br>🌹:
     * <br>🌹Desc
     * <br>🌹param
     * <br>🌹param
     * <br>🌹param
     * <br>🌹return
     */
    public List<Object> OracleConnect4List(List<String> failureReasonsList, DataSynchronizationRelDao relDao, PortBaseDao portBaseDao) {
        try {
            Connection dataConnection = getDataConnection(relDao);
            logger.info("连接数据库成功！");
            // 预编译语句，“？”代表参数
            String sql = relDao.getQueryScript();
            //获取配置参数
            String queryParams = relDao.getQueryParams();
            //查询参数转换list
            List<Object> paramList = Arrays.asList(queryParams.split(",")).stream().map(s -> (s.trim())).collect(Collectors.toList());
            return DatabaseUtil.executeQuery4ParamsReturnList(dataConnection, sql, paramList);
        } catch (Exception e) {
            e.printStackTrace();
            //String exceptionInformation = ExceptionInformationUtil.getExceptionAllInformation(e);
            failureReasonsList.add("第三方数据库连接异常");
        }
        return null;
    }


    public List<Map<String, Object>> OracleConnect4Map(List<String> failureReasonsList, DataSynchronizationRelDao relDao, PortBaseDao portBaseDao) {
        try {
            Connection dataConnection = getDataConnection(relDao);
            logger.info("连接数据库成功！");
            // 预编译语句，“？”代表参数
            String sql = relDao.getQueryScript();
            //获取配置参数
            String queryParams = relDao.getQueryParams();
            //查询参数转换list
            List<Object> paramList=null;
            if (StringUtils.isNotBlank(queryParams)) {
                 paramList = Arrays.asList(queryParams.split(",")).stream().map(s -> (s.trim())).collect(Collectors.toList());
            }

            return DatabaseUtil.executeQuery4MapByParams(dataConnection, sql, paramList);
        } catch (Exception e) {
            e.printStackTrace();
            //String exceptionInformation = ExceptionInformationUtil.getExceptionAllInformation(e);
            failureReasonsList.add("第三方数据库连接异常");
        }
        return null;
    }
}