package com.zzs.zzsadmin.controller;

import cn.hutool.core.bean.BeanUtil;
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
import com.zzs.zzsadmin.vo.menu.MenuVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiModelProperty;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@Api(tags = "菜单管理")
@RestController
@RequestMapping("api/menuManager")
public class MenuController {

    @Autowired
    IMenuService menuService;

    @ApiOperation("分页获取菜单列表")
    @GetMapping("pageList")
    public PageResult<MenuVo> getMenusPage(Long pageNum, Long pageSize, String name) {
        AssertUtil.valid(pageNum, "pageNum", "参数错误");
        AssertUtil.valid(pageSize, "pageSize", "参数错误");
        IPage<Menu> pageList = menuService.getMenusPage(pageNum, pageSize, name);
        PageResult<MenuVo> res = new PageResult<>(pageList);
        List<MenuVo> list = new ArrayList<>();
        for (Menu m : pageList.getRecords()) {
            MenuVo dto = new MenuVo();
            BeanUtil.copyProperties(m, dto);
            list.add(dto);
        }
        res.setDatas(list);
        return res;
    }

    @ApiOperation("添加菜单")
    @PostMapping("/add")
    public BaseResultData addMenu(@RequestBody MenuDto dto, @RequestParam String loginName) {
        BaseResultData res = new BaseResultData();
        menuService.addMenu(dto, loginName);
        return res;
    }

    @ApiOperation("删除菜单")
    @DeleteMapping("/delete/{id}")
    public BaseResultData deleteMenu(@PathVariable(value = "id") String id) {
        BaseResultData res = new BaseResultData();
        AssertUtil.valid(id, "id", "参数错误");
        menuService.deleteMenu(id);
        return res;
    }

    @ApiOperation("根据id获取菜单信息")
    @GetMapping("/menu")
    public ResultData<MenuVo> getMenuById(String id) {
        ResultData<MenuVo> res = new ResultData<>();
        AssertUtil.valid(id, "id", "参数错误");
        MenuVo menuVo = menuService.getMenuById(id);
        res.setData(menuVo);
        return res;
    }

    @ApiOperation("根据菜单树")
    @GetMapping("/menuTree")
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
        updateWrapper.eq("id", dto.getId());
        Boolean save = false;
        if (!StrUtil.equals(dto.getName(), menu.getName())) {
            save = true;
            //menu.setName(dto.getName());
            updateWrapper.set("name", dto.getName());
        }
        if (!StrUtil.equals(dto.getParentId(), menu.getParentId())) {
            save = true;
            //menu.setParentId(dto.getParentId());
            updateWrapper.set("parent_id", dto.getParentId());
        }
        if (!StrUtil.equals(String.valueOf(dto.getIsMenu()), String.valueOf(menu.getIsMenu()))) {
            save = true;
            // menu.setIsMenu(dto.getIsMenu());
            updateWrapper.set("is_menu", dto.getIsMenu());
        }
        if (!StrUtil.equals(dto.getUrl(), menu.getUrl())) {
            save = true;
            //menu.setUrl(dto.getUrl());
            updateWrapper.set("url", dto.getUrl());
        }
        if (!StrUtil.equals(String.valueOf(dto.getOrderId()), String.valueOf(menu.getOrderId()))) {
            save = true;
            //menu.setOrderId(dto.getOrderId());
            updateWrapper.set("order_id", dto.getOrderId());
        }
        if (!StrUtil.equals(String.valueOf(dto.getIsEnabled()), String.valueOf(menu.getIsEnabled()))) {
            save = true;
            //menu.setIsEnabled(dto.getIsEnabled());
            updateWrapper.set("is_enabled", dto.getIsEnabled());
        }
        if (save) {
            //menu.setModifier(loginName);
            updateWrapper.set("modifier", loginName);
            //menuService.updateById(menu);
            menuService.update(updateWrapper);
        }
        return res;
    }

    @GetMapping("/userMenu")
    public ResultDataList<UserMenuTreeDto> GetUserMenuTree(String id) {
        ResultDataList<UserMenuTreeDto> res = new ResultDataList<>();
        List<UserMenuTreeDto> menuTreeDtos = menuService.GetUserMenuTree(id);
        res.setDatas(menuTreeDtos);
        return res;
    }


}
