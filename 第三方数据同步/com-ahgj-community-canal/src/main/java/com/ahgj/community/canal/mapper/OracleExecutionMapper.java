package com.ahgj.community.canal.mapper;

import org.apache.ibatis.annotations.Param;

import java.util.List;


public interface OracleExecutionMapper  {
    /**
     * 根据端基础信息id查询数据库连接信息
     * @param list
     * @param targetTableName
     * @return
     */
    int insertBatch(@Param("maps") List<Object> list, @Param("tableName")String targetTableName);
}
