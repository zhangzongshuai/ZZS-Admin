package com.zzs.zzsadmin.service.impl;


import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zzs.zzsadmin.entity.Role_Menu;
import com.zzs.zzsadmin.mapper.RoleMenuMapper;
import com.zzs.zzsadmin.service.IRoleMenuService;
import org.springframework.stereotype.Service;

@Service
public class RoleMenuService extends ServiceImpl<RoleMenuMapper, Role_Menu> implements IRoleMenuService {
}
