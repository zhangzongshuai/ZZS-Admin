package com.zzs.zzsadmin.service;


import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.zzs.zzsadmin.dto.MenuDto;
import com.zzs.zzsadmin.dto.MenuTreeDto;
import com.zzs.zzsadmin.dto.UserMenuTreeDto;
import com.zzs.zzsadmin.entity.Menu;
import com.zzs.zzsadmin.vo.menu.MenuVo;

import java.util.List;

public interface IMenuService  extends IService<Menu> {

    IPage<Menu> getMenusPage(Long pageNum, Long pageSize, String name);

    void addMenu(MenuDto menu, String loginName);

    void deleteMenu(String id);

    MenuVo getMenuById(String id);

    List<MenuTreeDto> GetMenuTree();

    List<UserMenuTreeDto> GetUserMenuTree(String userId);
}
