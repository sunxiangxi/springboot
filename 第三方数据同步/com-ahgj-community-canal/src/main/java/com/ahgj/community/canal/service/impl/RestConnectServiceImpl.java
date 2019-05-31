package com.ahgj.community.canal.service.impl;

import com.ahgj.community.canal.bean.dao.PortBaseDao;
import com.ahgj.community.canal.bean.dao.RestSynchronizationRelDao;
import com.ahgj.community.canal.mapper.RestSynchronizationRelMapper;
import com.ahgj.community.canal.service.RestConnectService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * 执行连接Oracle数据库
 * @author Hohn
 */
@Service
public class RestConnectServiceImpl implements RestConnectService {
    private static final Logger logger = LoggerFactory.getLogger(RestConnectServiceImpl.class);
@Autowired
private RestSynchronizationRelMapper restSynchronizationRelMapper;

    @Override
    public RestSynchronizationRelDao queryRestConnectRel(String portBaseId) {
        RestSynchronizationRelDao restSynchronizationRelDao = restSynchronizationRelMapper.selectRestConnectByPortBaseId(portBaseId);
        return restSynchronizationRelDao;
    }


    @Override
    public List<PortBaseDao> queryConnectInfoAll(String deleteFlag) {
        return null;
    }

    @Override
    public int addPortBaseInfo(Map<String, Object> requestMap, PortBaseDao baseDao) {
        return 0;
    }
}