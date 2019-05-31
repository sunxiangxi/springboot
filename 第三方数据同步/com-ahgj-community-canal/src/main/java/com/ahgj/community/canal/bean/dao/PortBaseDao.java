package com.ahgj.community.canal.bean.dao;

import lombok.Builder;
import lombok.Data;
import org.hibernate.validator.constraints.Range;

import javax.persistence.Column;
import javax.persistence.Table;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * 多源端基础配置信息
 * @author Hohn
 */
@Data
@Builder
@Table(name = "C_PORT_BASE")
public class PortBaseDao implements Serializable {
    @Column(name = "ID")
    private String id;
    /**
     * 数据端名称
     */
    @NotBlank(message = "数据端名称不能为空")
    @Column(name = "PORT_NAME")
    private String portName;

    /**
     * 目标表名
     */
    @NotBlank(message = "目标表名不能为空")
    @Column(name = "TARGET_TABLE_NAME")
    private String targetTableName;

    /**
     * 目标表字段
     */
    @NotBlank(message = "目标表字段不能为空")
    @Column(name = "TARGET_TABLE_FIELD")
    private String targetTableField;
    /**
     * 增量更新字段
     */
    @NotBlank(message = "同步的表名不能为空")
    @Column(name = "UPDATE_FIELD")
    private String updateField;
    /**
     * 分批批量插入执行条数
     */
    @NotNull(message = "分批批量插入执行条数不能为空")
    @Column(name = "EXECUTIONS_NUMBER")
    private Integer executionsNumber;

    /**
     * 同步类型 1: 全量同步 2: 增量同步
     */
    @Column(name = "SYNCHRONIZE_TYPE")
    private Integer synchronizeType;
    /**
     * 连接类型(同步方式) 1: oracle 2: mysql 3: http/https协议 4: webSocket协议
     */
    @Range(min = 1, max = 5, message = "请选择正确的同步方式")
    @Column(name = "SYNCHRONIZE_WAY")
    private Integer synchronizeWay;

    /**
     * 定时任务时间
     */
    @Column(name = "CRON")
    private String cron;

    /**
     * 删除状态（0未删除、1已删除，默认0")
     */
    @Column(name = "DELETE_FLAG")
    private String deleteFlag;

}
