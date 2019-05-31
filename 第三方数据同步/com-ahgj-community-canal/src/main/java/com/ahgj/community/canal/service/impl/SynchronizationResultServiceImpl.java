package com.ahgj.community.canal.service.impl;

import com.ahgj.community.canal.bean.dao.SynchronizationResultDao;
import com.ahgj.community.canal.mapper.SynchronizationResultMapper;
import com.ahgj.community.canal.utils.AssertUtil;
import com.ahgj.community.canal.utils.DateUtil;
import com.ahgj.community.canal.utils.SpringUtils;
import org.apache.commons.collections4.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

/**
 * 同步结果
 *
 * @author Hohn
 */
@Service
public class SynchronizationResultServiceImpl {
    private static final Logger logger = LoggerFactory.getLogger(SynchronizationResultServiceImpl.class);

    /**
     * 设置同步结果其他信息
     * @param synchronizationResultDao
     * @param piecesNumber
     * @param size
     */
    public void setSynchronizationResult(SynchronizationResultDao synchronizationResultDao, int piecesNumber, int size) {

        //设置同步结果信息 同步数据量
        synchronizationResultDao.setSynchronizedDataVolume(size);
        //成功条数
        synchronizationResultDao.setSuccessNumber(piecesNumber);
        //失败条数
        synchronizationResultDao.setFailedNumber(size - piecesNumber);
        if (size == 0) {
            //成功率
            synchronizationResultDao.setSuccessRate(0.00);
        }else {
            BigDecimal b = new BigDecimal(piecesNumber / size);
            //成功率
            synchronizationResultDao.setSuccessRate(b.setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue());
        }

    }

    /**
     * 插入同步结果对象
     *
     * @param synchronizationResultDao
     * @param failureReasonsList
     * @param startTime
     */
    public void setSynchronizationResult(SynchronizationResultDao synchronizationResultDao, List<String> failureReasonsList, String startTime) {
        String endTime = DateUtil.getCurrentFormatDateTime();
        long loseMinuteTime = DateUtil.getLoseTimeSecond(endTime, startTime);
        logger.info("============数据库同步任务结束时间，" + endTime);
        logger.info("============数据库同步任务耗时，" + loseMinuteTime + "分钟");
        //插入同步结果信息
        if (CollectionUtils.isNotEmpty(failureReasonsList)) {
            synchronizationResultDao.setFailureReasons(String.join(",", failureReasonsList));
            synchronizationResultDao.setSynchronizedStatus(1);
        }
        synchronizationResultDao.setStartTime(startTime);
        synchronizationResultDao.setEndTime(endTime);
        //耗时
        synchronizationResultDao.setElapsedTime(loseMinuteTime);
        synchronizationResultDao.setId(UUID.randomUUID().toString().replace("-", ""));
        SynchronizationResultMapper synchronizationResultMapper = SpringUtils.getBean(SynchronizationResultMapper.class);
        int insert = synchronizationResultMapper.insertSelective(synchronizationResultDao);
        AssertUtil.intExecuteFailure(insert, "同步结果信息插入失败", HttpStatus.BAD_REQUEST.value());
    }
}
