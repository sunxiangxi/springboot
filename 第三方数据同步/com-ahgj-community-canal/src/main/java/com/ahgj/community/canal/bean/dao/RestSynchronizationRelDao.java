package com.ahgj.community.canal.bean.dao;

import lombok.Builder;
import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Table;
import java.io.Serializable;

@Data
@Builder
@Table(name = "C_REST_SYNCHRONIZATION_REL")
public class RestSynchronizationRelDao implements Serializable {
    /**
     * ID
     */
    @Column(name = "ID")
    private String id;
    /**
     * 关联端基本信息id
     */
    @Column(name = "PORT_ID")
    private String portId;
    /**
     * 协议url
     */
    @Column(name = "HTTP_URL")
    private String httpUrl;
    /**
     * url参数 json.toString形式
     */
    @Column(name = "STRING_ENTITY")
    private String stringEntity;

    /**
     * response中获取目标数据的key值
     */
    @Column(name = "DATA_KEY")
    private String dataKey;

    /**
     * 获取服务器资源成功状态字段
     */
    @Column(name = "HTTP_STATUS_FILED")
    private String httpStatusFiled;

    /**
     * 请求头上下文类型,默认application/x-www-form-urlencoded
     */
    @Column(name = "CONTENT_TYPE")
    private String contentType;

    /**
     * http/https协议与服务器交互方法 get/post
     */
    @Column(name = "REQUEST_METHOD")
    private String requestMethod;
    /**
     * 删除状态（0未删除、1已删除，默认0")
     */
    @Column(name = "DELETE_FLAG")
    private String deleteFlag;


}
