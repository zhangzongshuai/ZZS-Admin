package com.zzs.zzsadmin.service.impl;


import cn.hutool.core.util.IdUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zzs.zzsadmin.entity.Role_Menu;
import com.zzs.zzsadmin.mapper.RoleMenuMapper;
import com.zzs.zzsadmin.service.IRoleMenuService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
public class RoleMenuServiceImpl extends ServiceImpl<RoleMenuMapper, Role_Menu> implements IRoleMenuService {
    @Override
    public List<Role_Menu> getMenusByRoleId(String roleId) {
        List<Role_Menu> menuIds = this.list(new QueryWrapper<Role_Menu>().eq("role_id", roleId));
        return menuIds;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void configRoleMenu(List<String> menuIds, String roleId) {
        List<Role_Menu> roleMenus = this.list(new QueryWrapper<Role_Menu>().eq("role_id", roleId));
        if (!roleMenus.isEmpty()) {
            this.remove(new QueryWrapper<Role_Menu>().eq("role_id", roleId));
        }
        List<Role_Menu> list = new ArrayList<>();
        menuIds.forEach(f -> {
            Role_Menu rm = new Role_Menu();
            rm.setId(IdUtil.simpleUUID());
            rm.setRoleId(roleId);
            rm.setMenuId(f);
            list.add(rm);
        });
        this.saveBatch(list);
    }
}
