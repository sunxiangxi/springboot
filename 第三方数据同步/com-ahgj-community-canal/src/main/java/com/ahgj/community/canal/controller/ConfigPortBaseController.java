package com.ahgj.community.canal.controller;

import com.ahgj.community.canal.bean.dao.PortBaseDao;
import com.ahgj.community.canal.request.validator.BasicInfoValidator;
import com.ahgj.community.canal.service.ConfigPortBaseService;
import com.ahgj.community.canal.utils.AssertUtil;
import com.ahgj.community.canal.utils.ReturnResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.validation.DataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import javax.validation.Valid;
import java.util.Map;

/**
 * 配置多源端连接方式
 *
 * @author Hohn
 */
@RestController
@RequestMapping(value = "/configPortBase")
public class ConfigPortBaseController {
    private static final Logger logger = LoggerFactory.getLogger(ConfigPortBaseController.class);
    /**
     * 注册校验器
     */
    @InitBinder
    public void initBinder(DataBinder binder){
        // 这个方法加载验证器，判断请求过来的要验证的对象，加载相对应的验证器。此方法是根据请求加载的，即n次请求就加载n次该方法。
        if (binder.getTarget() != null) {
            if(basicInfoValidator.supports(binder.getTarget().getClass())) {
                binder.addValidators(basicInfoValidator);
            }
        }
    }

    @Autowired
    private ConfigPortBaseService connectInfoService;
    @Autowired
    private BasicInfoValidator basicInfoValidator;

    /**
     * 增加多源端配置信息
     * <br>👇请求地址: /communityCanal/configPortBase/addPortInfo
     * <br>🌹:
     * <br>🌹param  map     不同端的相关配置信息
     * <br>🌹param  baseDao 端基础配置信息
     * <br>🌹return   
     */
    @RequestMapping(value = "/addPortInfo")
    public ReturnResult addPortInfo(@RequestParam Map<String,Object> map, @Valid PortBaseDao baseDao) {
        // 根据不同端插入配置信息
        //================================================
       int addStatus = connectInfoService.addPortInfo(map,baseDao);
        AssertUtil.intExecuteFailure(addStatus,"数据端配置信息失败",HttpStatus.BAD_REQUEST.value());
        return new ReturnResult(HttpStatus.OK, "数据端配置信息成功");
    }


}