package com.ahgj.community.canal.mapper;

import com.ahgj.community.canal.bean.dao.PortBaseDao;
import io.lettuce.core.dynamic.annotation.Param;
import tk.mybatis.mapper.common.Mapper;

import java.util.List;

public interface PortBaseInfoMapper extends Mapper<PortBaseDao> {
    /**
     * 查询所有未删除的多源端信息
     * @param deleteFlag
     * @return
     */
    List<PortBaseDao> selectConnectInfoAll(@Param("deleteFlag") String deleteFlag);

    /**
     * 查询目标表名时候存在
     * @param targetTableName
     * @return
     */
    int isSynchronizationTableExist(@Param("targetTableName") String targetTableName);
}
