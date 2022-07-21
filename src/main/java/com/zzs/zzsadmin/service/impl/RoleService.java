package com.zzs.zzsadmin.service.impl;


import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zzs.zzsadmin.entity.Role;
import com.zzs.zzsadmin.mapper.RoleMapper;
import com.zzs.zzsadmin.service.IRoleService;
import org.springframework.stereotype.Service;

@Service
public class RoleService extends ServiceImpl<RoleMapper, Role> implements IRoleService {
}
