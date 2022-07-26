package com.zzs.zzsadmin.service;


import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.zzs.zzsadmin.entity.AccessLog;

/**
 * <p></p>
 *
 * @author 张宗帅
 * @date 2021-01-20
 */
public interface IAccessLogService extends IService<AccessLog> {

    IPage<AccessLog> getAccessLogPage(Long pageNum, Long pageSize, String moduleName, String userName, String startTime, String endTime);

    boolean addAccessLog(AccessLog accessLog);
}

