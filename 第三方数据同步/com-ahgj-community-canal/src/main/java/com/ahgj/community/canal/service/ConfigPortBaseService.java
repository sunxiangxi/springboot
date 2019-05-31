package com.ahgj.community.canal.service;

import com.ahgj.community.canal.bean.dao.DataSynchronizationRelDao;
import com.ahgj.community.canal.bean.dao.PortBaseDao;

import java.util.List;
import java.util.Map;

/**
 * 同步连接对象信息
 * @author Hohn
 */
public interface ConfigPortBaseService {
    /**
     * 查询数据库连接配置信息
     * @param portBaseId
     * @return
     */
    DataSynchronizationRelDao queryDataConnectPortRel(String portBaseId);
    /**
     * 获取多源端基础信息未删除禁用的
     * @return
     * @param deleteFlag
     */
    List<PortBaseDao> queryConnectInfoAll(String deleteFlag);

    /**
     * 根据多源端基本信息配置端信息
     * @param requestMap
     * @param baseDao
     * @return
     */
    int addPortInfo(Map<String, Object> requestMap,PortBaseDao baseDao);
}
