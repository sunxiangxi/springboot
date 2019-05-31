package com.ahgj.community.canal.utils;

import java.util.ArrayList;
import java.util.List;

/**
 * 分批批量插入工具类
 * @author Hohn
 */
public class BatchInsertUtil<E> {
    /**
     * 分批批量插入
     * @param list
     * @return
     */
    public List<List<E>> getBatchInsertList(List<E> list) {
        //每批插入数目
        int batchCount = 1500;
        //定义全局执行插入List索引
        int batchLastIndex = batchCount;
        //全量list分批插入到一个集合
        List<List<E>> shareList = new ArrayList<>();
        for (int index = 0; index < list.size(); ) {
            if (batchLastIndex >= list.size()) {
                batchLastIndex = list.size();
                shareList.add(list.subList(index, batchLastIndex));
                break;
            } else {
                shareList.add(list.subList(index, batchLastIndex));
                index = batchLastIndex;// 设置下一批下标
                batchLastIndex = index + (batchCount - 1);
            }
        }
        return shareList;
    }


    /**
     * jdbcTemple List<Object[]> 参数转换
     *
     * @param metaDataList
     * @return
     */
    private List<Object[]> transformToObjects(List<Object> metaDataList) {
        List<Object[]> objectList = new ArrayList<>();
        Object[] object = null;
        for (Object metaData : metaDataList) {
            object = new Object[]{metaData};
            objectList.add(object);
        }
        return objectList;
    }

    /**
     * jdbcTemple List<Object[]> 参数转换
     *
     * @param metaDataList
     * @return
     */
    private List<Object[]> transformFlowToObjects(List<Object> metaDataList) {
        List<Object[]> objectList = new ArrayList<>();
        Object[] object = null;
        for (Object metaData : metaDataList) {

            object = new Object[]{metaData};
            objectList.add(object);
        }
        return objectList;
    }
}
