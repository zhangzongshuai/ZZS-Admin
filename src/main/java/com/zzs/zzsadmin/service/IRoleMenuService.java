package com.zzs.zzsadmin.service;


import com.baomidou.mybatisplus.extension.service.IService;
import com.zzs.zzsadmin.entity.Role_Menu;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

public interface IRoleMenuService  extends IService<Role_Menu> {
    /**
     * 根据角色id获取权限id
     * @param roleId
     * @return
     */
    List<Role_Menu> getMenusByRoleId(String roleId);

    /**
     * 配置角色权限
     * @param menuIds
     * @param roleId
     */
    void configRoleMenu(List<String> menuIds, String roleId);
}
