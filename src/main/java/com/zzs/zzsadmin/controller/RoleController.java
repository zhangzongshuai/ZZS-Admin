package com.zzs.zzsadmin.controller;


import cn.hutool.core.util.IdUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.zzs.zzsadmin.common.utils.AssertUtil;
import com.zzs.zzsadmin.common.utils.CopyUtil;
import com.zzs.zzsadmin.common.vo.BaseResultData;
import com.zzs.zzsadmin.common.vo.PageResult;
import com.zzs.zzsadmin.common.vo.ResultData;
import com.zzs.zzsadmin.common.vo.ResultDataList;
import com.zzs.zzsadmin.dto.RoleDto;
import com.zzs.zzsadmin.dto.RoleUserDto;
import com.zzs.zzsadmin.entity.Role;
import com.zzs.zzsadmin.entity.Role_Menu;
import com.zzs.zzsadmin.entity.Role_User;
import com.zzs.zzsadmin.entity.User;
import com.zzs.zzsadmin.service.IRoleMenuService;
import com.zzs.zzsadmin.service.IRoleService;
import com.zzs.zzsadmin.service.IRoleUserService;
import com.zzs.zzsadmin.service.IUserService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;


@RestController
@RequestMapping("/api/role")
public class RoleController {

    @Autowired
    IRoleService roleService;
    @Autowired
    IRoleMenuService roleMenuService;

    @Autowired
    IRoleUserService roleUserService;

    @Autowired
    IUserService userService;

    @PostMapping("/add")
    public BaseResultData AddRole(@RequestBody RoleDto dto, @RequestParam String loginName) {
        AssertUtil.valid(loginName, "loginName", "参数错误");
        BaseResultData res = new BaseResultData();

        Role role = new Role();
        CopyUtil.CobyTo(dto, role);
        role.setId(IdUtil.simpleUUID());
        role.setCreator(loginName);
        roleService.save(role);

        return res;
    }

    @GetMapping("/pagelist")
    public PageResult<RoleDto> GetRolesPage(String name, Long pageNum, Long pageSize) {
        AssertUtil.valid(pageNum, "pageNum", "参数错误");
        AssertUtil.valid(pageSize, "pageSize", "参数错误");
        IPage<Role> page = new Page<>(pageNum, pageSize);
        QueryWrapper wrapper = new QueryWrapper();
        if (StringUtils.isNotBlank(name)) {
            wrapper.like("name", name);
        }
        wrapper.orderByAsc("create_time");
        IPage pageList = roleService.page(page, wrapper);
        PageResult<RoleDto> res = new PageResult<>(pageList, false);
        List<RoleDto> list = new ArrayList<>();
        pageList.getRecords().forEach(f -> {
            RoleDto dto = new RoleDto();
            CopyUtil.CobyTo(f, dto);
            list.add(dto);
        });
        res.setDatas(list);
        return res;
    }

    @GetMapping("/role")
    public ResultData<RoleDto> GetRole(String id) {
        AssertUtil.valid(id, "id", "参数错误");

        Role byId = roleService.getById(id);
        RoleDto dto = new RoleDto();
        CopyUtil.CobyTo(byId, dto);
        ResultData<RoleDto> res = new ResultData<>();
        res.setData(dto);
        return res;
    }

    @PutMapping("/modify")
    public BaseResultData ModifyRole(@RequestBody RoleDto dto, @RequestParam String loginName) {

        BaseResultData res = new BaseResultData();
        Role byId = roleService.getById(dto.getId());
        if (byId == null) {
            res.setErrcode(1);
            res.setErrmsg("数据不存在,请刷新后重试");
            return res;
        }
        byId.setName(dto.getName());
        boolean save = false;
        if (!Objects.equals(byId.getName(), dto.getName())) {
            byId.setName(dto.getName());
            save = true;
        }
        if (!Objects.equals(byId.getDescription(), dto.getDescription())) {
            byId.setDescription(dto.getDescription());
            save = true;
        }
        if (!byId.getIsEnabled().equals(dto.getIsEnabled())) {
            byId.setIsEnabled(dto.getIsEnabled());
            save = true;
        }

        if (save) {
            byId.setModifier(loginName);
            roleService.updateById(byId);
        }
        return res;
    }

    @DeleteMapping("/delete/{id}")
    @Transactional
    public BaseResultData DeleteRole(@PathVariable(value = "id") String id) {
        AssertUtil.valid(id, "id", "参数错误");
        BaseResultData res = new BaseResultData();
        Role byId = roleService.getById(id);
        if (byId == null) {
            res.setErrcode(1);
            res.setErrmsg("数据不存在,请刷新后重试");
            return res;
        }
        roleService.removeById(id);
        roleUserService.remove(new QueryWrapper<Role_User>().eq("role_id",id));//移除关系
        return res;
    }

    @GetMapping("/rolemenus")
    public ResultDataList<Role_Menu> GetMenusByRoleId(String roleId) {
        ResultDataList<Role_Menu> res = new ResultDataList<>();
        List<Role_Menu> list = roleMenuService.list(new QueryWrapper<Role_Menu>().eq("role_id", roleId));
        res.setDatas(list);
        return res;
    }

    @PostMapping("configrolemenu")
    @Transactional
    public BaseResultData ConfigRoleMenu(@RequestBody List<String> menuIds, @RequestParam String roleId) {
        BaseResultData res = new BaseResultData();
        List<Role_Menu> roleMenus = roleMenuService.list(new QueryWrapper<Role_Menu>().eq("role_id", roleId));
        if (!roleMenus.isEmpty()) {
            roleMenuService.remove(new QueryWrapper<Role_Menu>().eq("role_id", roleId));
        }
        List<Role_Menu> list = new ArrayList<>();
        menuIds.forEach(f -> {
            Role_Menu rm = new Role_Menu();
            rm.setId(IdUtil.simpleUUID());
            rm.setRoleId(roleId);
            rm.setMenuId(f);
            list.add(rm);
        });
        roleMenuService.saveBatch(list);
        return res;
    }

    @GetMapping("/usersByRoleId")
    public PageResult<RoleUserDto> GetUsersByRoleId(String name, String roleId, Long pageNum, Long pageSize) {
        PageResult<RoleUserDto> res = new PageResult<>();
        QueryWrapper<User> wrapper = new QueryWrapper<>();
        if (StringUtils.isNotBlank(name)) {
            wrapper.like("name", name);
        }
        List<User> users = userService.list(wrapper.eq("is_deleted", 0));
        List<Role_User> role_users = roleUserService.list(new QueryWrapper<Role_User>().eq("role_id", roleId));
        List<String> uids = role_users.stream().map(m -> m.getUserId()).collect(Collectors.toList());

        List<RoleUserDto> list = new ArrayList<>();

        users.forEach(f -> {
            RoleUserDto dto = new RoleUserDto();
            CopyUtil.CobyTo(f, dto);
            if (uids.contains(f.getId())) {
                dto.setIsAssign(1);
            } else {
                dto.setIsAssign(0);
            }
            list.add(dto);
        });
        list.sort(new Comparator<RoleUserDto>() {
            @Override
            public int compare(RoleUserDto o1, RoleUserDto o2) {
                return o1.getLoginName().compareTo(o2.getLoginName());
            }
        });
        list.sort(new Comparator<RoleUserDto>() {
            @Override
            public int compare(RoleUserDto o1, RoleUserDto o2) {
                return o2.getIsAssign() - o1.getIsAssign();
            }
        });
        Long skipNum = (pageNum - 1) * pageSize;
        List<RoleUserDto> dtos = list.stream().skip(skipNum).limit(pageSize).collect(Collectors.toList());
        res.setDatas(dtos);
        res.setTotalCount(list.size());
        res.setPageSize(pageNum);
        res.setPageSize(pageSize);
        res.setPages(list.size() % pageSize == 0 ? list.size() / pageSize : list.size() / pageSize + 1);
        return res;
    }

    @PostMapping("/configroleuser")
    @Transactional
    public BaseResultData ConfigRoleUser(@RequestBody List<String> userIds, @RequestParam String roleId) {
        BaseResultData res = new BaseResultData();
        List<Role_User> roleUsers = roleUserService.list(new QueryWrapper<Role_User>().eq("role_id", roleId));
        if (!roleUsers.isEmpty()) {
            roleUserService.remove(new QueryWrapper<Role_User>().eq("role_id", roleId));
        }
        if (!userIds.isEmpty()) {
            List<Role_User> list = new ArrayList<>();
            userIds.forEach(f -> {
                Role_User rm = new Role_User();
                rm.setId(IdUtil.simpleUUID());
                rm.setRoleId(roleId);
                rm.setUserId(f);
                list.add(rm);
            });
            roleUserService.saveBatch(list);
        }
        return res;
    }

    @GetMapping("/rolesByUserId")
    public ResultDataList<RoleDto> GetRolesByUserId(String userId) {

        List<Role_User> roleUsers = roleUserService.list(new QueryWrapper<Role_User>().eq("user_id", userId));

        List<Role> roles = roleService.list(new QueryWrapper<Role>().eq("is_enabled", 1).orderByAsc("create_time"));
        List<RoleDto> list = new ArrayList<>();
        roles.forEach(f -> {
            RoleDto dto = new RoleDto();
            CopyUtil.CobyTo(f, dto);
            long count = roleUsers.stream().filter(s -> Objects.equals(s.getRoleId(), f.getId())).count();
            if (count > 0) {
                dto.setIsAssign(1);
            }
            list.add(dto);
        });
        ResultDataList<RoleDto> res = new ResultDataList<>();
        res.setDatas(list);
        return res;
    }
}
