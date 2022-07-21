package com.zzs.zzsadmin.service.impl;


import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zzs.zzsadmin.entity.Role_User;
import com.zzs.zzsadmin.mapper.RoleUserMapper;
import com.zzs.zzsadmin.service.IRoleUserService;
import org.springframework.stereotype.Service;

@Service
public class RoleUserService extends ServiceImpl<RoleUserMapper, Role_User> implements IRoleUserService {
}
