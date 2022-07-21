package com.zzs.zzsadmin.controller;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.IdUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;

import com.zzs.zzsadmin.common.utils.AssertUtil;
import com.zzs.zzsadmin.common.utils.CopyUtil;
import com.zzs.zzsadmin.common.vo.BaseResultData;
import com.zzs.zzsadmin.common.vo.PageResult;
import com.zzs.zzsadmin.common.vo.ResultData;
import com.zzs.zzsadmin.common.vo.ResultDataList;
import com.zzs.zzsadmin.dto.user.ModifyPasswordDto;
import com.zzs.zzsadmin.dto.user.UserDto;
import com.zzs.zzsadmin.dto.userinfo;
import com.zzs.zzsadmin.entity.Role_User;
import com.zzs.zzsadmin.entity.User;
import com.zzs.zzsadmin.service.IRoleUserService;
import com.zzs.zzsadmin.service.IUserService;
import com.zzs.zzsadmin.vo.user.UserVo;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api/user")
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
    @ApiOperation(value = "")
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
    @GetMapping("/pagelist")
    public PageResult<UserVo> GetUsersPage(String name, Long pageNum, Long pageSize) {
        AssertUtil.valid(pageNum, "pageNum", "参数错误");
        AssertUtil.valid(pageSize, "pageSize", "参数错误");
        IPage pageList = userService.getUserPage(name, pageNum, pageSize);
        PageResult<UserVo> res = new PageResult<>(pageList);
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
     * @param user
     * @return
     */
    @ApiOperation(value = "添加用户")
    @PostMapping("/addUser")
    public BaseResultData addUser(@RequestBody User user) {
        BaseResultData res = new BaseResultData();

        userService.addUser(user);
        return res;

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
    @GetMapping("/resetpwd")
    public BaseResultData ResetPassword(String id, String loginName) {
        userService.resetPassword(id, loginName);
        return new BaseResultData();
    }

    @ApiOperation(value = "删除用户")
    @DeleteMapping("/deleteuser/{id}")
    public BaseResultData DeleteUser(@PathVariable String id, @RequestParam String loginName) {
        userService.deleteUser(id, loginName);
        return new BaseResultData();
    }

    @GetMapping("/user")
    public ResultData<userinfo> GetUserById(String id) {
        ResultData res = new ResultData<userinfo>();

        String errmsg = "";
        User user = new User();
        res.setErrcode(userService.GetUserById(id, user));

        userinfo ui = new userinfo();
        CopyUtil.CobyTo(user, ui);
        res.setData(ui);
        return res;
    }

    @PutMapping("/modifyuser")
    public BaseResultData ModifyUser(@RequestBody UserDto user) {
        userService.modifyUser(user);
        return new BaseResultData();
    }


    @PostMapping("/configuserrole")
    @Transactional
    public BaseResultData ConfigUserRoles(@RequestBody List<String> roleIds, @RequestParam String userId) {

        AssertUtil.valid(userId, "userId", "参数错误");
        BaseResultData res = new BaseResultData();
        List<Role_User> roleUsers = roleUserService.list(new QueryWrapper<Role_User>().eq("user_id", userId));
        if (!roleUsers.isEmpty()) {
            roleUserService.remove(new QueryWrapper<Role_User>().eq("user_d", userId));
        }
        if (!roleIds.isEmpty()) {
            List<Role_User> list = new ArrayList<>();
            roleIds.forEach(f -> {
                Role_User rm = new Role_User();
                rm.setId(IdUtil.simpleUUID());
                rm.setRoleId(f);
                rm.setUserId(userId);
                list.add(rm);
            });
            roleUserService.saveBatch(list);
        }

        return res;
    }


}
