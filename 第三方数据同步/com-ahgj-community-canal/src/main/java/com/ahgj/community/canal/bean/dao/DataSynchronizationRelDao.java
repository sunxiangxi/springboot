package com.ahgj.community.canal.bean.dao;

import lombok.Builder;
import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Table;
import java.io.Serializable;

/**
 *
 * 数据库同步方式配置
 * @author Hohn
 */
@Data
@Builder
@Table(name = "C_DATA_SYNCHRONIZATION_REL")
public class DataSynchronizationRelDao implements Serializable {
    @Column(name = "ID")
    private String id;
    /**
     * 关联端基本信息id
     */
    @Column(name = "PORT_ID")
    private String portId;

    /**
     * 数据库连接驱动
     */
    @Column(name = "DRIVER_CLASS_NAME")
    private String driverClassName;

    /**
     * 数据库连接url
     */
    @Column(name = "DATASOURCE_URL")
    private String datasourceUrl;

    /**
     * 数据库连接用户名
     */
    @Column(name = "DATASOURCE_USERNAME")
    private String datasourceUsername;

    /**
     * 数据库连接密码
     */
    @Column(name = "DATASOURCE_PASSWORD")
    private String datasourcePassword;

    /**
     * 查询sql语句
     */
    @Column(name = "QUERY_SCRIPT")
    private String queryScript;

    /**
     * 查询参数 逗号隔开,按sql语句参数顺序拼接
     */
    @Column(name = "QUERY_PARAMS")
    private String queryParams;

    /**
     * 删除状态（0未删除、1已删除，默认0")
     */
    @Column(name = "DELETE_FLAG")
    private String deleteFlag;


}
