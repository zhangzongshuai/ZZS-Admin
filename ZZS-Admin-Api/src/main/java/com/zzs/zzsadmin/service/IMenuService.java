package com.zzs.zzsadmin.service;


import com.baomidou.mybatisplus.extension.service.IService;
import com.zzs.zzsadmin.dto.MenuTreeDto;
import com.zzs.zzsadmin.dto.UserMenuTreeDto;
import com.zzs.zzsadmin.entity.Menu;

import java.util.List;

public interface IMenuService  extends IService<Menu> {
    List<MenuTreeDto> GetMenuTree();

    List<UserMenuTreeDto> GetUserMenuTree(String userId);
}
