package com.zzs.zzsadmin.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zzs.zzsadmin.entity.AccessLog;
import com.zzs.zzsadmin.mapper.AccessLogMapper;
import com.zzs.zzsadmin.service.IAccessLogService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

/**
 * @author 张宗帅
 * @date 2021-01-20 23:06:15
 */
@Service
public class AccessLogServiceImpl extends ServiceImpl<AccessLogMapper, AccessLog> implements IAccessLogService {


    /**
     * 分页查询访问日志
     * @param pageNum
     * @param pageSize
     * @param moduleName
     * @param userName
     * @param startTime
     * @param endTime
     * @return
     */
    @Override
    public IPage<AccessLog> getAccessLogPage(Long pageNum, Long pageSize, String moduleName, String userName, String startTime, String endTime) {
        IPage<AccessLog> page = new Page<>(pageNum, pageSize);
        QueryWrapper<AccessLog> wrapper = new QueryWrapper<>();
        wrapper.like(StringUtils.isNotBlank(moduleName), "operate_module", moduleName)
                .like(StringUtils.isNotBlank(userName), "operator", userName)
                .ge(StringUtils.isNotBlank(startTime), "operate_time", startTime)
                .le(StringUtils.isNotBlank(endTime), "operate_time", endTime)
                .orderByDesc("operate_time");
        IPage<AccessLog> accessLogIPage = this.page(page);
        return accessLogIPage;
    }

    @Override
    public boolean addAccessLog(AccessLog accessLog) {
        return this.save(accessLog);
    }
}
