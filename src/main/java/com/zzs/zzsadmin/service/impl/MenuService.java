package com.zzs.zzsadmin.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zzs.zzsadmin.dto.MenuTreeDto;
import com.zzs.zzsadmin.dto.UserMenuTreeDto;
import com.zzs.zzsadmin.entity.Menu;
import com.zzs.zzsadmin.entity.Role;
import com.zzs.zzsadmin.entity.Role_Menu;
import com.zzs.zzsadmin.entity.Role_User;
import com.zzs.zzsadmin.mapper.MenuMapper;
import com.zzs.zzsadmin.mapper.RoleMapper;
import com.zzs.zzsadmin.mapper.RoleMenuMapper;
import com.zzs.zzsadmin.mapper.RoleUserMapper;
import com.zzs.zzsadmin.service.IMenuService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
public class MenuService extends ServiceImpl<MenuMapper, Menu> implements IMenuService {
    @Autowired
    MenuMapper menuMapper;
    @Autowired
    RoleMapper roleMapper;
    @Autowired
    RoleUserMapper roleUserMapper;
    @Autowired
    RoleMenuMapper roleMenuMapper;

    @Override
    public List<MenuTreeDto> GetMenuTree() {

        QueryWrapper<Menu> wrapper = new QueryWrapper<>();
        wrapper.orderByAsc("order_id");
        List<Menu> menuList = menuMapper.selectList(wrapper);
        if (!menuList.isEmpty()) {
            List<MenuTreeDto> menuTreeDtos= BuildTree(menuList, "0");
            return menuTreeDtos;
        } else {
            return new ArrayList<>();
        }
    }

    @Override
    public List<UserMenuTreeDto> GetUserMenuTree(String userId) {
        List<Role_User> roleUsers = roleUserMapper.selectList(new QueryWrapper<Role_User>().eq("user_id", userId));
        if (roleUsers.isEmpty()){
            return new ArrayList<>();
        }
        List<String> roleIds = roleUsers.stream().map(m->m.getRoleId()).collect(Collectors.toList());

        List<Role> roles = roleMapper.selectList(new QueryWrapper<Role>().in("id", roleIds).eq("is_enabled", 1));
        if (roles.isEmpty()){
            return new ArrayList<>();
        }
        List<String> enabledRoleIds = roles.stream().map(m->m.getId()).collect(Collectors.toList());

        List<Role_Menu> roleMenus = roleMenuMapper.selectList(new QueryWrapper<Role_Menu>().in("role_id", enabledRoleIds));
        if (roleMenus.isEmpty()){
            return new ArrayList<>();
        }
        List<String> menuIds = roleMenus.stream().map(m->m.getMenuId()).collect(Collectors.toList());
        QueryWrapper<Menu> wrapper = new QueryWrapper<>();
        wrapper.eq("is_menu",1).eq("is_enabled",1).in("id",menuIds);
        wrapper.orderByAsc("order_id");
        List<Menu> menuList = menuMapper.selectList(wrapper);
        if (!menuList.isEmpty()) {
            List<UserMenuTreeDto> menuTreeDtos= BuildUserMenuTree(menuList, "0");
            return menuTreeDtos;
        } else {
            return new ArrayList<>();
        }

    }

    private List<MenuTreeDto> BuildTree(List<Menu> list, String pid){
        List<MenuTreeDto> childrenList = new ArrayList<>();
        List<Menu> parentList = new ArrayList<>();
        if (pid == "0") {
             parentList = list.stream()
                     .filter(s -> Objects.equals(s.getParentId(), "0") || Objects.equals(s.getParentId(), "")|| Objects.equals(s.getParentId(), null))
                     .collect(Collectors.toList());
        }
        else{
            parentList = list.stream().filter(s -> Objects.equals(s.getParentId(), pid)).collect(Collectors.toList());
        }


        if (!parentList.isEmpty()) {
            for (Menu o : parentList) {
                List<MenuTreeDto> child = BuildTree(list, o.getId());
                MenuTreeDto dto = new MenuTreeDto();
                dto.setId(o.getId());
                dto.setLabel(o.getName());
                dto.setChildren(child.isEmpty()?new ArrayList<>():child);
                childrenList.add(dto);

            }
        }

        return childrenList;
    }

    private List<UserMenuTreeDto> BuildUserMenuTree(List<Menu> list, String pid){
        List<UserMenuTreeDto> childrenList = new ArrayList<>();
        List<Menu> parentList = new ArrayList<>();
        if (pid == "0") {
            parentList = list.stream()
                    .filter(s -> Objects.equals(s.getParentId(), "0") || Objects.equals(s.getParentId(), "")|| Objects.equals(s.getParentId(), null))
                    .collect(Collectors.toList());
        }
        else{
            parentList = list.stream().filter(s -> Objects.equals(s.getParentId(), pid)).collect(Collectors.toList());
        }


        if (!parentList.isEmpty()) {
            for (Menu o : parentList) {
                List<UserMenuTreeDto> child = BuildUserMenuTree(list, o.getId());
                UserMenuTreeDto dto = new UserMenuTreeDto();
                dto.setId(o.getId());
                dto.setTitle(o.getName());
                dto.setUrl(o.getUrl()+"?title="+o.getName());
                dto.setChildren(child.isEmpty()?null:child);
                childrenList.add(dto);

            }
        }

        return childrenList;
    }
}
