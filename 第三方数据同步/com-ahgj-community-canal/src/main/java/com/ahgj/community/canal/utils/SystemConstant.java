package com.ahgj.community.canal.utils;

public interface SystemConstant {
    /* 删除状态标识 0未删除、1已删除，默认0 */
    String DELETE_STATUS_1 = "1";

    /* 未删除状态标识 0未删除、1已删除，默认0 */
    String NOT_DELETE_STATUS_0 = "0";

    /* 数据库同步任务 */
    String DATA_DYNAMIC_TASK = "dataDynamicTaskFuture";

    /* http协议同步任务 */
    String HTTP_DYNAMIC_TASK = "restDynamicTaskFuture";

}
