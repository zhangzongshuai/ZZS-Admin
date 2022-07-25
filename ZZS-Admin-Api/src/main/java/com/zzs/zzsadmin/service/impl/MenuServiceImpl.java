package com.zzs.zzsadmin.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.IdUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zzs.zzsadmin.common.exception.MessageException;
import com.zzs.zzsadmin.common.utils.CopyUtil;
import com.zzs.zzsadmin.dto.MenuDto;
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
import com.zzs.zzsadmin.vo.menu.MenuVo;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
public class MenuServiceImpl extends ServiceImpl<MenuMapper, Menu> implements IMenuService {
    @Autowired
    MenuMapper menuMapper;
    @Autowired
    RoleMapper roleMapper;
    @Autowired
    RoleUserMapper roleUserMapper;
    @Autowired
    RoleMenuMapper roleMenuMapper;

    @Override
    public IPage<Menu> getMenusPage(Long pageNum, Long pageSize, String name) {
        QueryWrapper<Menu> wrapper = new QueryWrapper<>();
        wrapper.like(StringUtils.isNotBlank(name), "name", name)
                .orderByAsc("order_id");
        IPage<Menu> page = new Page<>(pageNum, pageSize);
        IPage<Menu> pageList = this.page(page, wrapper);
        return pageList;
    }

    @Override
    public void addMenu(MenuDto dto, String loginName) {
        Menu menu = new Menu();
        BeanUtil.copyProperties(dto, menu);
        menu.setId(IdUtil.simpleUUID());
        menu.setCreator(loginName);
        this.save(menu);
    }


    /**
     * TODO 删除权限关系
     *
     * @param id
     */
    @Override
    public void deleteMenu(String id) {
        Menu menu = this.getById(id);
        if (menu == null) {
            throw new MessageException("数据不存在,请刷新重试");
        }
        List<Menu> list = this.list(new QueryWrapper<Menu>().eq("parent_id", menu.getId()));
        if (list.size() > 0) {
            throw new MessageException("该菜单下有子菜单,请先移除后再删除");
        }
        this.removeById(id);
    }

    @Override
    public void modifyMenu(MenuVo menuVo, String loginName) {
        Menu menu = this.getById(menuVo.getId());
        if (menu == null) {
            throw new MessageException("数据不存在,请刷新重试");
        }
        BeanUtil.copyProperties(menuVo, menu);
        menu.setModifier(loginName);
        this.updateById(menu);
    }

    @Override
    public MenuVo getMenuById(String id) {
        Menu menu = this.getById(id);
        MenuVo menuVo = new MenuVo();
        BeanUtil.copyProperties(menu, menuVo);
        return menuVo;
    }

    @Override
    public List<MenuTreeDto> GetMenuTree() {

        QueryWrapper<Menu> wrapper = new QueryWrapper<>();
        wrapper.orderByAsc("order_id");
        List<Menu> menuList = menuMapper.selectList(wrapper);
        if (!menuList.isEmpty()) {
            List<MenuTreeDto> menuTreeDtos = BuildTree(menuList, "0");
            return menuTreeDtos;
        } else {
            return new ArrayList<>();
        }
    }

    @Override
    public List<UserMenuTreeDto> GetUserMenuTree(String userId) {
        List<Role_User> roleUsers = roleUserMapper.selectList(new QueryWrapper<Role_User>().eq("user_id", userId));
        if (roleUsers.isEmpty()) {
            return new ArrayList<>();
        }
        List<String> roleIds = roleUsers.stream().map(m -> m.getRoleId()).collect(Collectors.toList());

        List<Role> roles = roleMapper.selectList(new QueryWrapper<Role>().in("id", roleIds).eq("is_enabled", 1));
        if (roles.isEmpty()) {
            return new ArrayList<>();
        }
        List<String> enabledRoleIds = roles.stream().map(m -> m.getId()).collect(Collectors.toList());

        List<Role_Menu> roleMenus = roleMenuMapper.selectList(new QueryWrapper<Role_Menu>().in("role_id", enabledRoleIds));
        if (roleMenus.isEmpty()) {
            return new ArrayList<>();
        }
        List<String> menuIds = roleMenus.stream().map(m -> m.getMenuId()).collect(Collectors.toList());
        QueryWrapper<Menu> wrapper = new QueryWrapper<>();
        wrapper.eq("is_menu", 1).eq("is_enabled", 1).in("id", menuIds);
        wrapper.orderByAsc("order_id");
        List<Menu> menuList = menuMapper.selectList(wrapper);
        if (!menuList.isEmpty()) {
            List<UserMenuTreeDto> menuTreeDtos = BuildUserMenuTree(menuList, "0");
            return menuTreeDtos;
        } else {
            return new ArrayList<>();
        }

    }

    private List<MenuTreeDto> BuildTree(List<Menu> list, String pid) {
        List<MenuTreeDto> childrenList = new ArrayList<>();
        List<Menu> parentList = new ArrayList<>();
        if (pid == "0") {
            parentList = list.stream()
                    .filter(s -> Objects.equals(s.getParentId(), "0") || Objects.equals(s.getParentId(), "") || Objects.equals(s.getParentId(), null))
                    .collect(Collectors.toList());
        } else {
            parentList = list.stream().filter(s -> Objects.equals(s.getParentId(), pid)).collect(Collectors.toList());
        }


        if (!parentList.isEmpty()) {
            for (Menu o : parentList) {
                List<MenuTreeDto> child = BuildTree(list, o.getId());
                MenuTreeDto dto = new MenuTreeDto();
                dto.setId(o.getId());
                dto.setLabel(o.getName());
                dto.setChildren(child.isEmpty() ? new ArrayList<>() : child);
                childrenList.add(dto);

            }
        }

        return childrenList;
    }

    private List<UserMenuTreeDto> BuildUserMenuTree(List<Menu> list, String pid) {
        List<UserMenuTreeDto> childrenList = new ArrayList<>();
        List<Menu> parentList;
        if (pid == "0") {
            parentList = list.stream()
                    .filter(s -> Objects.equals(s.getParentId(), "0") || Objects.equals(s.getParentId(), "") || Objects.equals(s.getParentId(), null))
                    .collect(Collectors.toList());
        } else {
            parentList = list.stream().filter(s -> Objects.equals(s.getParentId(), pid)).collect(Collectors.toList());
        }


        if (!parentList.isEmpty()) {
            for (Menu o : parentList) {
                List<UserMenuTreeDto> child = BuildUserMenuTree(list, o.getId());
                UserMenuTreeDto dto = new UserMenuTreeDto();
                dto.setId(o.getId());
                dto.setTitle(o.getName());
                dto.setUrl(o.getUrl() + "?title=" + o.getName());
                dto.setIconCode(o.getIconCode());
                dto.setChildren(child.isEmpty() ? null : child);
                childrenList.add(dto);

            }
        }

        return childrenList;
    }
}
