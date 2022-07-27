package com.zzs.zzsadmin.controller;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.zzs.zzsadmin.common.utils.AssertUtil;
import com.zzs.zzsadmin.common.vo.BaseResultData;
import com.zzs.zzsadmin.common.vo.PageResult;
import com.zzs.zzsadmin.common.vo.ResultData;
import com.zzs.zzsadmin.common.vo.ResultDataList;
import com.zzs.zzsadmin.dto.MenuDto;
import com.zzs.zzsadmin.dto.MenuTreeDto;
import com.zzs.zzsadmin.entity.Menu;
import com.zzs.zzsadmin.service.IMenuService;
import com.zzs.zzsadmin.vo.menu.MenuVo;
import com.zzs.zzsadmin.vo.menu.MenuTreeVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
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
        if (pageNum == null || pageNum == null) {
            pageNum = 1L;
            pageSize = -1L;
        }
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

    @ApiOperation("获取菜单树形列表")
    @GetMapping("/menuTree1")
    public ResultDataList<MenuTreeDto> GetMenuTree() {
        ResultDataList<MenuTreeDto> res = new ResultDataList<>();
        List<MenuTreeDto> menuTreeDtos = menuService.GetMenuTree();
        res.setDatas(menuTreeDtos);
        return res;
    }

    @ApiOperation("获取菜单树形列表")
    @GetMapping("/menuTree")
    public ResultDataList<MenuTreeVo> getMenuTree() {
        ResultDataList<MenuTreeVo> res = new ResultDataList<>();
        List<MenuTreeVo> menuTreeDtos = menuService.getMenuTree();
        res.setDatas(menuTreeDtos);
        return res;
    }


    @ApiOperation("修改菜单")
    @PutMapping("/modify")
    public BaseResultData modifyMenu(@RequestBody MenuVo menuVo, @RequestParam String loginName) {
        BaseResultData res = new BaseResultData();
        menuService.modifyMenu(menuVo, loginName);
        return res;
    }

    @ApiOperation("获取用户菜单权限")
    @GetMapping("/userMenu")
    public ResultDataList<MenuTreeVo> getUserMenuTree(String id) {
        ResultDataList<MenuTreeVo> res = new ResultDataList<>();
        List<MenuTreeVo> menuTreeDtos = menuService.GetUserMenuTree(id);
        res.setDatas(menuTreeDtos);
        return res;
    }

    @ApiOperation("获取用户按钮权限")
    @GetMapping("/userBtn")
    public ResultDataList<String> getUserBtn(String id) {
        ResultDataList<String> res = new ResultDataList<>();
        List<String> userBtn = menuService.getUserBtn(id);
        res.setDatas(userBtn);
        return res;
    }


}
