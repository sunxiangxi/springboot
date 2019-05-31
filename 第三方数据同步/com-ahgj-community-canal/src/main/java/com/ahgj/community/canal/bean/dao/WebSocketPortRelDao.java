package com.ahgj.community.canal.bean.dao;

import lombok.Data;

import java.io.Serializable;

/**
 * webSocket同步方式配置
 * @author Hohn
 */
@Data
//@Table(name = "C_WEB_SOCKET_PROT_REL")
public class WebSocketPortRelDao implements Serializable {
    
    private String id;
    /**
     * 端基本信息id
     */
    private String portId;

    /**
     * 删除状态（0未删除、1已删除，默认0")
     */
    private String deleteFlag;


}
