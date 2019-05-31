package com.ahgj.community.canal.quartz;

import com.ahgj.community.canal.bean.dao.PortBaseDao;
import com.ahgj.community.canal.bean.dao.RestSynchronizationRelDao;
import com.ahgj.community.canal.bean.dao.SynchronizationResultDao;
import com.ahgj.community.canal.service.RestConnectService;
import com.ahgj.community.canal.service.impl.BatchInsertDataServiceImpl;
import com.ahgj.community.canal.service.impl.SynchronizationResultServiceImpl;
import com.ahgj.community.canal.utils.DateUtil;
import com.ahgj.community.canal.utils.SpringUtils;
import com.alibaba.fastjson.JSON;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Rest端同步任务
 * 注意:
 * <br>🌹    使用@Autowired @Resource控制反转原理获取注入对象会为null,
 * <br>🌹    因为在容器启动开始定时任务时未完成实例化而且线程定时任务的根据反射机制动态生成的,
 * <br>🌹    需要动态获取,要使用项目中SpringUtils.getBean方法
 *
 * @author Hohn
 */

public class RestDynamicTask implements Runnable {
    private static final Logger logger = LoggerFactory.getLogger(RestDynamicTask.class);

    /**
     * 多源端基本信息
     */
    private PortBaseDao portBaseDao;

    public RestDynamicTask(PortBaseDao portBaseDao) {
        this.portBaseDao = portBaseDao;
    }

    @Override
    public void run() {
        //定义同步结果信息
        SynchronizationResultDao synchronizationResultDao = new SynchronizationResultDao();
        List<String> failureReasonsList = new ArrayList<>();
        String startTime = DateUtil.getCurrentFormatDateTime();
        logger.info("============Rest同步任务开始时间：" + startTime);
        //执行Rest同步
        int piecesNumber = executeRestSynchronization(synchronizationResultDao, failureReasonsList);

        //从容器中获取SynchronizationResultServiceImpl实例
        SynchronizationResultServiceImpl synchronizationResultServiceImpl = SpringUtils.getBean(SynchronizationResultServiceImpl.class);
        //设置同步结果信息
        synchronizationResultServiceImpl.setSynchronizationResult(synchronizationResultDao, failureReasonsList, startTime);

    }


    /**
     * 执行rest同步
     *
     * @param synchronizationResultDao
     * @param failureReasonsList
     * @return
     */
    private int executeRestSynchronization(SynchronizationResultDao synchronizationResultDao, List<String> failureReasonsList) {
        int piecesNumber = 0;
        RestConnectService restConnectService = SpringUtils.getBean(RestConnectService.class);
        //查询数据库连接配置信息
        RestSynchronizationRelDao restConnectRel = restConnectService.queryRestConnectRel(this.portBaseDao.getId());
        if (restConnectRel != null) {
            //定义接受转换结果json对象
            List<Map<String, Object>> mapList = new ArrayList<>();
            //获取容器RestTemplate实例
            RestTemplate restTemplate = SpringUtils.getBean(RestTemplate.class);
            BatchInsertDataServiceImpl batchInsertDataService = SpringUtils.getBean(BatchInsertDataServiceImpl.class);
            //忽略大小写判断get/post 请求
            if (restConnectRel.getRequestMethod().equalsIgnoreCase(HttpMethod.GET.name())) {
                //调用url获取响应实例
                ResponseEntity<String> results = restTemplate.exchange(restConnectRel.getHttpUrl(), HttpMethod.GET, null, String.class);
                //插入获取的元数据
                piecesNumber = resolveMetaData(synchronizationResultDao, failureReasonsList, piecesNumber, restConnectRel, mapList, batchInsertDataService, results);
            }
            if (restConnectRel.getRequestMethod().equalsIgnoreCase(HttpMethod.POST.name())) {
                ResponseEntity<String> jsonEntity = restTemplate.postForEntity(restConnectRel.getHttpUrl(), restConnectRel.getStringEntity(), String.class);
                //插入获取的元数据
                piecesNumber = resolveMetaData(synchronizationResultDao, failureReasonsList, piecesNumber, restConnectRel, mapList, batchInsertDataService, jsonEntity);
            }
        }
        return piecesNumber;
    }

    private int resolveMetaData(SynchronizationResultDao synchronizationResultDao, List<String> failureReasonsList, int piecesNumber, RestSynchronizationRelDao restConnectRel, List<Map<String, Object>> mapList, BatchInsertDataServiceImpl batchInsertDataService, ResponseEntity<String> results) {
        //========================================
        // 客户端返回状态码200,解析数据执行插入
        if (results.getStatusCode() == HttpStatus.OK) {
            //响应数据转换map,遍历map key值匹配 rest同步信息配置数据dataKey值获取结果集
            Map<String, Object> mapTypes = JSON.parseObject(results.getBody());
            for (Object obj : mapTypes.keySet()) {
                if (obj.equals(restConnectRel.getDataKey())) {
                    mapList = new Gson().fromJson(mapTypes.get(obj).toString(), new TypeToken<List<Map<String, Object>>>() {
                    }.getType());
                }
            }
            //插入同步数据
            if (mapList.size() == 0) {
                //设置同步结果集异常信息
                failureReasonsList.add("根据Rest配置获取第三方数据为空");
            }
            piecesNumber = batchInsertDataService.executeFullInsert(mapList, synchronizationResultDao, failureReasonsList, portBaseDao);
            //设置同步结果信息  同步数据量/成功条数/失败条数/成功率
            SynchronizationResultServiceImpl synchronizationResultServiceImpl = SpringUtils.getBean(SynchronizationResultServiceImpl.class);
            synchronizationResultServiceImpl.setSynchronizationResult(synchronizationResultDao, piecesNumber, mapList.size());

            //==================================
        } else {
            //设置异常信息
            failureReasonsList.add("服务端异常,请求失败");
        }
        return piecesNumber;
    }
}