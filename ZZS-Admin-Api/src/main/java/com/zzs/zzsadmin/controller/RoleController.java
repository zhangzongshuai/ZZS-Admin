package com.zzs.zzsadmin.controller;


import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.zzs.zzsadmin.common.utils.AssertUtil;
import com.zzs.zzsadmin.common.vo.BaseResultData;
import com.zzs.zzsadmin.common.vo.PageResult;
import com.zzs.zzsadmin.common.vo.ResultData;
import com.zzs.zzsadmin.common.vo.ResultDataList;
import com.zzs.zzsadmin.dto.RoleDto;
import com.zzs.zzsadmin.entity.Role;
import com.zzs.zzsadmin.entity.Role_Menu;
import com.zzs.zzsadmin.service.IRoleMenuService;
import com.zzs.zzsadmin.service.IRoleService;
import com.zzs.zzsadmin.service.IRoleUserService;
import com.zzs.zzsadmin.service.IUserService;
import com.zzs.zzsadmin.vo.role.RoleUserVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@Api(tags = "角色管理")
@RestController
@RequestMapping("/api/roleManager")
public class RoleController {

    @Autowired
    IRoleService roleService;
    @Autowired
    IRoleMenuService roleMenuService;

    @Autowired
    IRoleUserService roleUserService;

    @Autowired
    IUserService userService;

    /**
     * 新增角色
     *
     * @param dto
     * @param loginName
     * @return
     */
    @ApiOperation(value = "添加角色")
    @PostMapping("/add")
    public BaseResultData addRole(@RequestBody RoleDto dto, @RequestParam String loginName) {
        roleService.addRole(dto, loginName);
        return new BaseResultData();
    }

    /**
     * 分页查询角色
     *
     * @param name
     * @param pageNum
     * @param pageSize
     * @return
     */
    @ApiOperation(value = "分页查询角色")
    @GetMapping("/pageList")
    public PageResult<RoleDto> getRolesPage(String name, Long pageNum, Long pageSize) {
        AssertUtil.valid(pageNum, "pageNum", "参数错误");
        AssertUtil.valid(pageSize, "pageSize", "参数错误");
        IPage pageList = roleService.getRolesPage(name, pageNum, pageSize);
        PageResult<RoleDto> res = new PageResult<>(pageList, false);
        List<RoleDto> list = new ArrayList<>();
        pageList.getRecords().forEach(f -> {
            RoleDto dto = new RoleDto();
            BeanUtil.copyProperties(f, dto);
            list.add(dto);
        });
        res.setDatas(list);
        return res;
    }

    /**
     * 根据id获取角色信息
     *
     * @param id
     * @return
     */
    @ApiOperation(value = "根据id获取角色信息")
    @GetMapping("/role")
    public ResultData<RoleDto> getRole(String id) {
        AssertUtil.valid(id, "id", "参数错误");
        Role byId = roleService.getById(id);
        RoleDto dto = new RoleDto();
        BeanUtil.copyProperties(byId, dto);
        ResultData<RoleDto> res = new ResultData<>();
        res.setData(dto);
        return res;
    }

    /**
     * 修改角色信息
     *
     * @param dto
     * @param loginName
     * @return
     */
    @ApiOperation(value = "修改角色信息")
    @PutMapping("/modifyRole")
    public BaseResultData modifyRole(@RequestBody RoleDto dto, @RequestParam String loginName) {
        roleService.modifyRole(dto, loginName);
        return new BaseResultData();
    }

    /**
     * 删除角色
     *
     * @param id
     * @return
     */
    @ApiOperation(value = "删除角色")
    @DeleteMapping("/delete/{id}")
    public BaseResultData deleteRole(@PathVariable(value = "id") String id) {
        AssertUtil.valid(id, "id", "参数错误");
        roleService.deleteRole(id);
        return new BaseResultData();
    }

    /**
     * 根据角色id获取权限菜单id
     *
     * @param roleId
     * @return
     */
    @ApiOperation(value = "根据角色id获取权限菜单id")
    @GetMapping("/roleMenus")
    public ResultDataList<Role_Menu> getMenusByRoleId(String roleId) {
        ResultDataList<Role_Menu> res = new ResultDataList<>();
        List<Role_Menu> list = roleMenuService.getMenusByRoleId(roleId);
        res.setDatas(list);
        return res;
    }

    /**
     * 配置角色权限
     *
     * @param menuIds
     * @param roleId
     * @return
     */
    @ApiOperation(value = "配置角色权限")
    @PostMapping("configRoleMenu")
    public BaseResultData configRoleMenu(@RequestBody List<String> menuIds, @RequestParam String roleId) {
        BaseResultData res = new BaseResultData();
        roleMenuService.configRoleMenu(menuIds, roleId);
        return res;
    }

    @ApiOperation(value = "根据角色id获取用户")
    @GetMapping("/usersByRoleId")
    public PageResult<RoleUserVo> getUsersByRoleId(String name, String roleId, Long pageNum, Long pageSize) {
        IPage<RoleUserVo> usersByRoleId = roleUserService.getUsersByRoleId(roleId, name, pageNum, pageSize);
        PageResult<RoleUserVo> res = new PageResult<>(usersByRoleId);
        return res;
    }

    @ApiOperation(value = "角色配置用户")
    @PostMapping("/configRoleUser")
    public BaseResultData configRoleUser(@RequestBody List<String> userIds, @RequestParam String roleId) {
        BaseResultData res = new BaseResultData();
        roleUserService.configRoleUser(userIds, roleId);
        return res;
    }


}
