package com.zzs.zzsadmin.controller;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.zzs.zzsadmin.common.utils.AssertUtil;
import com.zzs.zzsadmin.common.vo.BaseResultData;
import com.zzs.zzsadmin.common.vo.PageResult;
import com.zzs.zzsadmin.common.vo.ResultData;
import com.zzs.zzsadmin.common.vo.ResultDataList;
import com.zzs.zzsadmin.dto.user.ModifyPasswordDto;
import com.zzs.zzsadmin.dto.user.UserDto;
import com.zzs.zzsadmin.entity.User;
import com.zzs.zzsadmin.service.IRoleUserService;
import com.zzs.zzsadmin.service.IUserService;
import com.zzs.zzsadmin.vo.role.UserRoleVo;
import com.zzs.zzsadmin.vo.user.UserVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api/userManager")
@Api(tags = "用户管理")
public class UserController {
    @Autowired
    IUserService userService;

    @Autowired
    IRoleUserService roleUserService;

    /**
     * 查询所有用户
     *
     * @param name
     * @return
     */
    @ApiOperation(value = "查询所有用户")
    @GetMapping("/list")
    public ResultDataList<UserVo> getUsers(String name) {
        ResultDataList<UserVo> res = new ResultDataList<>();
        List<User> users = userService.getUser(name);
        for (User u : users) {
            UserVo ui = new UserVo();
            BeanUtil.copyProperties(u, ui);
            res.getDatas().add(ui);
        }
        return res;
    }

    /**
     * 分页查询用户
     *
     * @param name
     * @param pageNum
     * @param pageSize
     * @return
     */
    @ApiOperation(value = "分页查询用户")
    @GetMapping("/pageList")
    public PageResult<UserVo> GetUsersPage(String name, Long pageNum, Long pageSize) {
        AssertUtil.valid(pageNum, "pageNum", "参数错误");
        AssertUtil.valid(pageSize, "pageSize", "参数错误");
        IPage pageList = userService.getUserPage(name, pageNum, pageSize);
        PageResult<UserVo> res = new PageResult<>(pageList, false);
        List<User> users = pageList.getRecords();
        List<UserVo> list = new ArrayList<>();
        for (User u : users
        ) {
            UserVo ui = new UserVo();
            BeanUtil.copyProperties(u, ui);
            list.add(ui);
        }
        res.setDatas(list);
        return res;
    }

    /**
     * 添加用户
     *
     * @param userDto
     * @return
     */
    @ApiOperation(value = "添加用户")
    @PostMapping("/addUser")
    public BaseResultData addUser(@RequestBody UserDto userDto) {
        userService.addUser(userDto);
        return new BaseResultData();

    }

    /**
     * 修改密码
     *
     * @param modifyPwd
     * @return
     */
    @ApiOperation(value = "修改密码")
    @PutMapping("/modifyPassword")
    public BaseResultData modifyPassword(@RequestBody ModifyPasswordDto modifyPwd) {
        AssertUtil.valid(modifyPwd.getUserId(), "userId", "参数不可为空");
        AssertUtil.valid(modifyPwd.getOldPassword(), "oldPassword", "参数不可为空");
        AssertUtil.valid(modifyPwd.getNewPassword(), "newPasssword", "参数不可为空");
        userService.modifyPassword(modifyPwd);
        return new BaseResultData();
    }

    /**
     * 重置密码
     *
     * @param id
     * @param loginName
     * @return
     */
    @ApiOperation(value = "重置密码")
    @GetMapping("/resetPassword")
    public BaseResultData resetPassword(String id, String loginName) {
        userService.resetPassword(id, loginName);
        return new BaseResultData();
    }

    /**
     * 删除用户
     *
     * @param id
     * @param loginName
     * @return
     */
    @ApiOperation(value = "删除用户")
    @DeleteMapping("/deleteUser/{id}")
    public BaseResultData deleteUser(@PathVariable String id, @RequestParam String loginName) {
        userService.deleteUser(id, loginName);
        return new BaseResultData();
    }

    /**
     * 根据id获取用户信息
     *
     * @param id
     * @return
     */
    @ApiOperation(value = "根据id获取用户信息")
    @GetMapping("/user")
    public ResultData<UserVo> getUserById(String id) {
        ResultData res = new ResultData<UserVo>();
        User user = userService.getUserById(id);
        UserVo ui = new UserVo();
        BeanUtil.copyProperties(user, ui);
        res.setData(ui);
        return res;
    }


    /**
     * 修改用户信息
     *
     * @param user
     * @return
     */
    @ApiOperation(value = "修改用户信息")
    @PutMapping("/modifyUser")
    public BaseResultData modifyUser(@RequestBody UserDto user) {
        userService.modifyUser(user);
        return new BaseResultData();
    }


    @ApiOperation(value = "根据用户id获取角色")
    @GetMapping("/rolesByUserId")
    public ResultDataList<UserRoleVo> getRolesByUserId(String userId, Long pageNum, Long pageSize) {
        if (pageSize == null){
            pageSize = -1L;
            pageNum = 1L;
        }
        IPage<UserRoleVo> rolesByUserId = roleUserService.getRolesByUserId(userId, pageNum, pageSize);
        ResultDataList<UserRoleVo> res = new ResultDataList<>(rolesByUserId.getRecords());
        return res;
    }

    /**
     * 配置用户角色
     *
     * @param roleIds
     * @param userId
     * @return
     */
    @ApiOperation(value = "配置用户角色")
    @PostMapping("/configUserRoles")
    public BaseResultData configUserRoles(@RequestBody List<String> roleIds, @RequestParam String userId) {

        AssertUtil.valid(userId, "userId", "参数错误");
        BaseResultData res = new BaseResultData();
        userService.configUserRoles(roleIds, userId);
        return res;
    }


}
