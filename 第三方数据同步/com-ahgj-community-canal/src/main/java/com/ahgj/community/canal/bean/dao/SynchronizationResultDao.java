package com.ahgj.community.canal.bean.dao;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Table;
import java.io.Serializable;

/**
 * 同步结果信息对象
 * @author Hohn
 */
@Data
@Table(name = "C_SYNCHRONISED_RESULT")
public class SynchronizationResultDao implements Serializable {
    @Column(name = "ID")
    private String id;

    /**
     * 同步数据量
     */
    @Column(name = "SYNCHRONIZED_DATA_VOLUME")
    private Integer SynchronizedDataVolume;
    /**
     * 成功数量
     */
    @Column(name = "SUCCESS_NUMBER")
    private Integer successNumber;

    /**
     * 失败数量
     */
    @Column(name = "FAILED_NUMBER")
    private Integer failedNumber;
    /**
     * 开始时间
     */
    @Column(name = "START_TIME")
    private String startTime;
    /**
     * 结束时间
     */
    @Column(name = "END_TIME")
    private String endTime;
    /**
     * 耗时 单位秒
     */
    @Column(name = "ELAPSED_TIME")
    private Long elapsedTime;
    /**
     * 成功率
     */
    @Column(name = "SUCCESS_RATE")
    private Double successRate;

    /**
     * 失败原因
     */
    @Column(name = "FAILURE_REASONS")
    private String failureReasons;
    /**
     * 同步状态(0成功 1失败 默认0)
     */
    @Column(name = "SYNCHRONIZED_STATUS")
    private Integer synchronizedStatus;
    /**
     * 删除状态（0未删除、1已删除，默认0")
     */
    @Column(name = "DELETE_FLAG")
    private String deleteFlag;

}
