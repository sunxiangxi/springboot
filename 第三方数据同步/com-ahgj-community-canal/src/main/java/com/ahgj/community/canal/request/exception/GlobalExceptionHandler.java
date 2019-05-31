package com.ahgj.community.canal.request.exception;

import org.springframework.beans.propertyeditors.StringTrimmerEditor;
import org.springframework.validation.BindException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 全局异常统一处理.
 * ControllerAdvice注解扫描基础包名,如后期引入其他包需要添加基础包名
 *
 * @author:Hohn
 */
@ControllerAdvice(basePackages = {"com.ahgj.*"})
public class GlobalExceptionHandler {

    @InitBinder
    public void initBinder(WebDataBinder binder) {
        StringTrimmerEditor stringTrimmerEditor = new StringTrimmerEditor(true);
        binder.registerCustomEditor(String.class, stringTrimmerEditor);
    }

    /**
     * BindException处理
     * org.springframework.validation.Validator、
     * org.hibernate.validator、javax.validation错误.
     * <p>
     * <br>?param: request 请求.
     * <br>?param: bindException bindException.
     * <br>?return: Map.
     *
     * @throws Exception
     */
    @ExceptionHandler(value = BindException.class)
    @ResponseBody
    public Map<String, Object> validatorErrorHandler(BindException bindException) throws Exception {
        Map<String, Object> map = new HashMap<String, Object>(2);
        List<FieldError> errors = bindException.getBindingResult().getFieldErrors();
        String message = null;
        for (int i = 0, size = errors.size(); i < size; i++) {
            FieldError fieldError = errors.get(i);
            message = fieldError.getDefaultMessage();
        }
        map.put("status", HttpServletResponse.SC_BAD_REQUEST);
        map.put("message", message);
        return map;
    }

    /**
     * 处理系统自定义异常错误.
     * <p>
     * <br>?param: request 请求.
     * <br>?param: informationException.
     * <br>?return: Map.
     *
     * @throws Exception
     */
    @ExceptionHandler(value = GjSystemException.class)
    @ResponseBody
    public Map<String, Object> ErrorHandler(GjSystemException systemException) throws Exception {

        Map<String, Object> map = new HashMap<String, Object>(2);
        map.put("status", HttpServletResponse.SC_BAD_REQUEST);
        map.put("message", systemException.getMessage());
        return map;
    }

    /**
     * 处理系统异常错误.
     * <p>
     * <br>?param: request 请求.
     * <br>?param: informationException.
     * <br>?return: Map.
     *
     * @throws Exception
     */
    @ExceptionHandler(value = Exception.class)
    @ResponseBody
    public Map<String, Object> ErrorHandler(Exception exception) throws Exception {

        Map<String, Object> map = new HashMap<String, Object>(2);
        map.put("status", HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        map.put("message", exception.getMessage());
        return map;
    }
}