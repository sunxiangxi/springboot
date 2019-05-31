package com.ahgj.community.canal.mapper;

import com.ahgj.community.canal.bean.dao.RestSynchronizationRelDao;
import io.lettuce.core.dynamic.annotation.Param;
import tk.mybatis.mapper.common.Mapper;


public interface RestSynchronizationRelMapper extends Mapper<RestSynchronizationRelDao> {
    /**
     * 根据端基础信息id查询数据库连接信息
     * @param portBaseId
     * @return
     */
    RestSynchronizationRelDao selectRestConnectByPortBaseId(@Param("portBaseId") String portBaseId);

}
