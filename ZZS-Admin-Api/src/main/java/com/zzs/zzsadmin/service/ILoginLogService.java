package com.zzs.zzsadmin.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.zzs.zzsadmin.entity.LoginLog;

/**
 * <p></p>
 *
 * @author 张宗帅
 * @date 2021-01-22 10:30:33
 */
public interface ILoginLogService extends IService<LoginLog> {

    IPage<LoginLog> getLoginLogPage(Long pageNum, Long pageSize, String userName, String startTime, String endTime);
}

