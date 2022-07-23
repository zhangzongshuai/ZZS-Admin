package com.zzs.zzsadmin.controller;

import cn.hutool.core.util.IdUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.zzs.zzsadmin.common.utils.AssertUtil;
import com.zzs.zzsadmin.common.utils.CopyUtil;
import com.zzs.zzsadmin.common.vo.BaseResultData;
import com.zzs.zzsadmin.common.vo.PageResult;
import com.zzs.zzsadmin.common.vo.ResultData;
import com.zzs.zzsadmin.common.vo.ResultDataList;
import com.zzs.zzsadmin.dto.MenuDto;
import com.zzs.zzsadmin.dto.MenuTreeDto;
import com.zzs.zzsadmin.dto.UserMenuTreeDto;
import com.zzs.zzsadmin.entity.Menu;
import com.zzs.zzsadmin.service.IMenuService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("api/menu")
public class MenuController {

    @Autowired
    IMenuService menuService;

    @GetMapping("pagelist")
    public PageResult<MenuDto> GetMenusPage(Long pageNum, Long pageSize, String name) {
        AssertUtil.valid(pageNum, "pageNum", "参数错误");
        AssertUtil.valid(pageSize, "pageSize", "参数错误");
        QueryWrapper<Menu> wrapper = new QueryWrapper<>();
        if (StringUtils.isNotBlank(name)) {
            wrapper.like("name", name);
        }
        wrapper.orderByAsc("order_id");
        IPage<Menu> page = new Page<>(pageNum, pageSize);
        IPage<Menu> pageList = menuService.page(page, wrapper);
        PageResult<MenuDto> res = new PageResult<>(pageList);
        List<MenuDto> list = new ArrayList<>();
        List<Menu> records = pageList.getRecords();
        for (Menu m : records
        ) {
            MenuDto dto = new MenuDto();
            CopyUtil.CobyTo(m, dto);
            list.add(dto);
        }
        res.setDatas(list);

        return res;
    }

    @PostMapping("/add")
    public BaseResultData AddMenu(@RequestBody MenuDto dto, @RequestParam String loginName) {
        BaseResultData res = new BaseResultData();
        Menu menu = new Menu();
        CopyUtil.CobyTo(dto, menu);
        menu.setId(IdUtil.simpleUUID());
        menu.setCreator(loginName);
        menuService.save(menu);
        return res;
    }

    @DeleteMapping("/delete/{id}")
    public BaseResultData DeleteMenu(@PathVariable(value = "id") String id) {
        BaseResultData res = new BaseResultData();
        AssertUtil.valid(id, "id", "参数错误");
        Menu menu = menuService.getById(id);

        if (menu == null) {
            res.setErrcode(1);
            res.setErrmsg("数据不存在,请刷新重试");
            return res;
        }
        List<Menu> list = menuService.list(new QueryWrapper<Menu>().eq("parent_id", menu.getId()));
        if (!list.isEmpty()) {
            res.setErrcode(1);
            res.setErrmsg("该菜单下有子菜单,请先移除后再删除");
            return res;
        }
        menuService.removeById(id);

        return res;
    }

    @GetMapping("/menu")
    public ResultData<MenuDto> GetMenuById(String id) {
        ResultData<MenuDto> res = new ResultData<>();
        AssertUtil.valid(id, "id", "参数错误");
        Menu menu = menuService.getById(id);
        if (menu == null) {
            res.setErrcode(1);
            res.setErrmsg("数据不存在,请刷新重试");
            return res;
        }
        MenuDto dto = new MenuDto();
        CopyUtil.CobyTo(menu, dto);
        res.setData(dto);
        return res;
    }

    @GetMapping("/menutree")
    public ResultDataList<MenuTreeDto> GetMenuTree() {
        ResultDataList<MenuTreeDto> res = new ResultDataList<>();

        List<MenuTreeDto> menuTreeDtos = menuService.GetMenuTree();
        res.setDatas(menuTreeDtos);
        return res;
    }

    @PutMapping("/modify")
    public BaseResultData ModifyMenu(@RequestBody MenuDto dto, @RequestParam String loginName) {
        BaseResultData res = new BaseResultData();
        Menu menu = menuService.getById(dto.getId());
        if (menu == null) {
            res.setErrcode(1);
            res.setErrmsg("数据不存在,请刷新重试");
            return res;
        }
        UpdateWrapper<Menu> updateWrapper = new UpdateWrapper<>();
        updateWrapper.eq("id",dto.getId());
        Boolean save = false;
        if (!StrUtil.equals(dto.getName(), menu.getName())) {
            save = true;
            //menu.setName(dto.getName());
            updateWrapper.set("name",dto.getName());
        }
        if (!StrUtil.equals(dto.getParentId(), menu.getParentId())) {
            save = true;
            //menu.setParentId(dto.getParentId());
            updateWrapper.set("parent_id",dto.getParentId());
        }
        if (!StrUtil.equals(String.valueOf(dto.getIsMenu()), String.valueOf(menu.getIsMenu()))) {
            save = true;
           // menu.setIsMenu(dto.getIsMenu());
            updateWrapper.set("is_menu",dto.getIsMenu());
        }
        if (!StrUtil.equals(dto.getUrl(), menu.getUrl())) {
            save = true;
            //menu.setUrl(dto.getUrl());
            updateWrapper.set("url",dto.getUrl());
        }
        if (!StrUtil.equals(String.valueOf(dto.getOrderId()), String.valueOf(menu.getOrderId()))) {
            save = true;
            //menu.setOrderId(dto.getOrderId());
            updateWrapper.set("order_id",dto.getOrderId());
        }
        if (!StrUtil.equals(String.valueOf(dto.getIsEnabled()), String.valueOf(menu.getIsEnabled()))) {
            save = true;
            //menu.setIsEnabled(dto.getIsEnabled());
            updateWrapper.set("is_enabled",dto.getIsEnabled());
        }
        if (save) {
            //menu.setModifier(loginName);
            updateWrapper.set("modifier",loginName);
            //menuService.updateById(menu);
            menuService.update(updateWrapper);
        }
        return res;
    }

    @GetMapping("/usermenu")
    public ResultDataList<UserMenuTreeDto> GetUserMenuTree(String id) {
        ResultDataList<UserMenuTreeDto> res = new ResultDataList<>();
        List<UserMenuTreeDto> menuTreeDtos = menuService.GetUserMenuTree(id);
        res.setDatas(menuTreeDtos);
        return res;
    }


}
