package com.ahgj.community.canal.service;

import com.ahgj.community.canal.bean.dao.PortBaseDao;
import com.ahgj.community.canal.bean.dao.RestSynchronizationRelDao;

import java.util.List;
import java.util.Map;

/**
 * http/https连接对象信息
 */
public interface RestConnectService {

    /**
     * 根据端id查询rest连接信息
     * @param portBaseId
     * @return
     */
    RestSynchronizationRelDao queryRestConnectRel(String portBaseId);
    /**
     * 获取多源端基础信息未删除禁用的
     * @return
     * @param deleteFlag
     */
    List<PortBaseDao> queryConnectInfoAll(String deleteFlag);

    /**
     * 根据多源端基本信息配置端信息
     * @param requestMap
     * @return
     */
    int addPortBaseInfo(Map<String, Object> requestMap, PortBaseDao baseDao);


}
