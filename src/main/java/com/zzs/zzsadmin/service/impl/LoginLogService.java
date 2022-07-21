package com.zzs.zzsadmin.service.impl;


import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zzs.zzsadmin.entity.LoginLog;
import com.zzs.zzsadmin.mapper.LoginLogMapper;
import com.zzs.zzsadmin.service.ILoginLogService;
import org.springframework.stereotype.Service;

/**
 *
 *
 * @author 张宗帅
 * @date 2021-01-22 10:30:33
 */
@Service
public class LoginLogService extends ServiceImpl<LoginLogMapper, LoginLog> implements ILoginLogService {

}
