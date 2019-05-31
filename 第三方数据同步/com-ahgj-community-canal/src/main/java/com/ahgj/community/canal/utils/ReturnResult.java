package com.ahgj.community.canal.utils;


import lombok.Data;
import org.springframework.http.HttpStatus;

@Data
public class ReturnResult implements java.io.Serializable {
    private static final long serialVersionUID = 1L;
    /**
     * 返回的状态码
     */
    private  Integer status;
    /**
     * 返回的提示信息
     */
    private  String  message;
    /**
     * 返回的自定义数据 数字类型
     */
    private  Integer customValue;
    /**
     * 返回的自定义数据 字符串类型
     */
    private  String  customString;

    /**
     * 返回的分页数据
     */
    private  Object data;

    public ReturnResult(HttpStatus status, String  message, Object data){
        this.status=status.value();
        this.message=message;
        this.data=data;
    }

    public ReturnResult(HttpStatus status, String  message, Integer customValue, Object data){
        this.status=status.value();
        this.message=message;
        this.customValue=customValue;
        this.data=data;
    }


    public ReturnResult(HttpStatus status, String  message, String customString, Object data){
        this.status=status.value();
        this.message=message;
        this.customString=customString;
        this.data=data;
    }


    public ReturnResult(HttpStatus status, String  message, Integer customValue, String customString, Object data){
        this.status=status.value();
        this.message=message;
        this.customValue=customValue;
        this.customString=customString;
        this.data=data;
    }

    public ReturnResult(HttpStatus status, String  message){
        this.status=status.value();
        this.message=message;
    }
}
