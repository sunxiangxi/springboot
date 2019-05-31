package com.ahgj.community.canal.mapper;

import com.ahgj.community.canal.bean.dao.DataSynchronizationRelDao;
import org.apache.ibatis.annotations.Param;
import tk.mybatis.mapper.common.Mapper;


public interface DataSynchronizationRelMapper extends Mapper<DataSynchronizationRelDao> {
    /**
     * 根据端基础信息id查询数据库连接信息
     * @param portBaseId
     * @return
     */
    DataSynchronizationRelDao selectDataConnectByPortBaseId(@Param("portBaseId") String portBaseId);
}
