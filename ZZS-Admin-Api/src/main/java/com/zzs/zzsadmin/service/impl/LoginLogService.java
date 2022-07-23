package com.zzs.zzsadmin.service.impl;


import cn.hutool.core.date.DateTime;
import cn.hutool.core.date.DateUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zzs.zzsadmin.entity.LoginLog;
import com.zzs.zzsadmin.mapper.LoginLogMapper;
import com.zzs.zzsadmin.service.ILoginLogService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

/**
 * @author 张宗帅
 * @date 2021-01-22 10:30:33
 */
@Service
public class LoginLogService extends ServiceImpl<LoginLogMapper, LoginLog> implements ILoginLogService {
    @Override
    public IPage<LoginLog> getLoginLogPage(Long pageNum, Long pageSize, String userName, String startTime, String endTime) {
        IPage<LoginLog> page = new Page<>(pageNum, pageSize);
        QueryWrapper<LoginLog> wrapper = new QueryWrapper<>();
        wrapper.like(StringUtils.isNotBlank(userName), "operator", userName)
                .ge(StringUtils.isNotBlank(startTime), "in_time", startTime);
        if (StringUtils.isNotBlank(endTime)) {
            DateTime eTime = DateUtil.offsetDay(DateUtil.parse(endTime), 1);
            wrapper.lt("in_time", eTime);
        }

        wrapper.orderByDesc("in_time");
        IPage<LoginLog> pageList = this.page(page, wrapper);
        return pageList;
    }
}
