package com.ahgj.community.canal.service.impl;

import com.ahgj.community.canal.bean.dao.DataSynchronizationRelDao;
import com.ahgj.community.canal.bean.dao.PortBaseDao;
import com.ahgj.community.canal.bean.dao.RestSynchronizationRelDao;
import com.ahgj.community.canal.mapper.DataSynchronizationRelMapper;
import com.ahgj.community.canal.mapper.PortBaseInfoMapper;
import com.ahgj.community.canal.mapper.RestSynchronizationRelMapper;
import com.ahgj.community.canal.service.ConfigPortBaseService;
import com.ahgj.community.canal.utils.SystemConstant;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.UUID;

/**
 * 多端源配置信息
 *
 * @author Hohn
 */
@Service
public class ConfigPortBaseServiceImpl implements ConfigPortBaseService {
    @Autowired
    private PortBaseInfoMapper portBaseInfoMapper;
    @Autowired
    private DataSynchronizationRelMapper dataConnectPortRelMapper;
    @Autowired
    private RestSynchronizationRelMapper restSynchronizationRelMapper;

    @Override
    public List<PortBaseDao> queryConnectInfoAll(String deleteFlag) {
        return portBaseInfoMapper.selectConnectInfoAll(deleteFlag);
    }

    @Override
    public DataSynchronizationRelDao queryDataConnectPortRel(String portBaseId) {
        return dataConnectPortRelMapper.selectDataConnectByPortBaseId(portBaseId);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int addPortInfo(Map<String, Object> requestMap, PortBaseDao baseDao) {
        //获取端连接类型 1: oracle 2: mysql 3: Rest 4: webSocket协议
        Integer connectionType = baseDao.getSynchronizeWay();
        //端基本信息插入,返回端信息id
        String randomUuid = insertBaseInfo(baseDao);
        if (connectionType == 1 || connectionType == 2) {
            return insertDataBaseRel(requestMap, randomUuid);
        }
        if (connectionType == 3 || connectionType == 4) {
            return insertRestRel(requestMap, randomUuid);
        }
        return 0;
    }

    /**
     * 插入Rest连接配置对象
     *
     * @param requestMap
     * @param randomUuid
     * @return
     */
    private int insertRestRel(Map<String, Object> requestMap, String randomUuid) {
        //设置Rest连接配置对象
        RestSynchronizationRelDao restSynchronizationRelDao = RestSynchronizationRelDao.builder()
                .id(UUID.randomUUID().toString().replace("-", ""))
                .portId(randomUuid)
                .httpUrl((String) requestMap.get("httpUrl"))
                .contentType((String) requestMap.get("contentType"))
                .requestMethod((String) requestMap.get("requestMethod"))
                .deleteFlag(SystemConstant.NOT_DELETE_STATUS_0).build();
        //设置url返回状态码字段
        Object httpStatusFiled = requestMap.get("httpStatusFiled");
        if (httpStatusFiled != null) {
            if (StringUtils.isNotBlank((String) httpStatusFiled)) {
                restSynchronizationRelDao.setHttpStatusFiled((String) httpStatusFiled);
            }
        }
        //插入同步结果集信息
        int insertStatus = restSynchronizationRelMapper.insertSelective(restSynchronizationRelDao);
        return insertStatus;
    }

    /**
     * 插入数据库连接配置对象
     *
     * @param requestMap
     * @param randomUuid
     * @return
     */
    private int insertDataBaseRel(Map<String, Object> requestMap, String randomUuid) {
        //设置数据库连接配置对象
        DataSynchronizationRelDao daoBuilder = DataSynchronizationRelDao.builder()
                .id(UUID.randomUUID().toString().replace("-", ""))
                .portId(randomUuid)
                .driverClassName((String) requestMap.get("driverClassName"))
                .datasourceUrl((String) requestMap.get("datasourceUrl"))
                .datasourceUsername((String) requestMap.get("datasourceUsername"))
                .datasourcePassword((String) requestMap.get("datasourcePassword"))
                .queryParams((String) requestMap.get("queryParams"))
                .deleteFlag(SystemConstant.NOT_DELETE_STATUS_0).build();
        int insertStatus = dataConnectPortRelMapper.insertSelective(daoBuilder);
        return insertStatus;
    }

    /**
     * 端基本信息插入,返回端信息id
     *
     * @param baseDao
     * @return
     */
    private String insertBaseInfo(PortBaseDao baseDao) {
        //插入数据源端基础信息
        String randomUuid = UUID.randomUUID().toString().replace("-", "");
        baseDao.setId(randomUuid);
        baseDao.setDeleteFlag(SystemConstant.NOT_DELETE_STATUS_0);
        portBaseInfoMapper.insert(baseDao);
        return randomUuid;
    }
}
