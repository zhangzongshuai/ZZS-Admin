package com.zzs.zzsadmin.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zzs.zzsadmin.entity.AccessLog;
import com.zzs.zzsadmin.mapper.AccessLogMapper;
import com.zzs.zzsadmin.service.IAccessLogService;
import org.springframework.stereotype.Service;

/**
 *
 *
 * @author 张宗帅
 * @date 2021-01-20 23:06:15
 */
@Service
public class AccessLogService extends ServiceImpl<AccessLogMapper, AccessLog> implements IAccessLogService {

    @Override
    public boolean addAccessLog(AccessLog accessLog){
        return this.save(accessLog);
    }
}
