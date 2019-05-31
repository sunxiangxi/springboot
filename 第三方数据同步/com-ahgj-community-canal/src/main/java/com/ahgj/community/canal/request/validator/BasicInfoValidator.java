package com.ahgj.community.canal.request.validator;

import com.ahgj.community.canal.bean.dao.PortBaseDao;
        import com.ahgj.community.canal.mapper.PortBaseInfoMapper;
        import com.ahgj.community.canal.utils.SpringUtils;
        import org.springframework.http.HttpStatus;
        import org.springframework.stereotype.Component;
        import org.springframework.validation.Errors;
        import org.springframework.validation.Validator;

/**
 * 多端同步配置信息效验
 *
 * @author Hohn
 */
@Component
public class BasicInfoValidator implements Validator {

    @Override
    public boolean supports(Class<?> classz) {
        // 判断是否是要校验的类,这里是PortBaseDao.
        return PortBaseDao.class.equals(classz);
    }

    @Override
    public void validate(Object o, Errors errors) {
        PortBaseDao basicInfo = (PortBaseDao) o;

        PortBaseInfoMapper baseInfoMapper = SpringUtils.getBean(PortBaseInfoMapper.class);

        //=====================================================
        // 验证资产编码唯一性.
        //=====================================================
        boolean result = true;
        try {
            result = baseInfoMapper.isSynchronizationTableExist(basicInfo.getTargetTableName()) > 0 ? true : false;
        } catch (Exception e) {
            e.printStackTrace();
        }
        if(result) {
            errors.rejectValue("targetTableName", HttpStatus.BAD_REQUEST.toString(), null, "此平台端数据库表已配置,请配置其他端或替换目标表名");
        }
    }

}