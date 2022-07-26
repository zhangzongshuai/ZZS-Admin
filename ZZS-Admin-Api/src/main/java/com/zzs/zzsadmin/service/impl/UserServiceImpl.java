package com.zzs.zzsadmin.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.IdUtil;

import cn.hutool.crypto.digest.DigestUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.zzs.zzsadmin.common.exception.MessageException;
import com.zzs.zzsadmin.common.utils.CopyUtil;
import com.zzs.zzsadmin.common.utils.JWTUtil;
import com.zzs.zzsadmin.common.vo.BaseResultData;
import com.zzs.zzsadmin.dto.LoginUser;
import com.zzs.zzsadmin.dto.user.LoginDto;
import com.zzs.zzsadmin.dto.user.ModifyPasswordDto;
import com.zzs.zzsadmin.dto.user.UserDto;
import com.zzs.zzsadmin.dto.userinfo;
import com.zzs.zzsadmin.entity.LoginLog;
import com.zzs.zzsadmin.entity.Role_User;
import com.zzs.zzsadmin.entity.User;
import com.zzs.zzsadmin.mapper.UserMapper;
import com.zzs.zzsadmin.service.ILoginLogService;
import com.zzs.zzsadmin.service.IRoleUserService;
import com.zzs.zzsadmin.service.IUserService;
import com.zzs.zzsadmin.vo.user.UserTokenVo;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.DigestUtils;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements IUserService {

    @Autowired
    private UserMapper um;

    @Autowired
    private ILoginLogService loginLogService;

    @Autowired
    private IRoleUserService roleUserService;

    @Override
    public User getUserByLoginName(String name) {
        QueryWrapper<User> wrapper = Wrappers.query();
        wrapper.eq("login_name", name).eq("is_deleted", 0);
        User user = um.selectOne(wrapper);
        return user;
    }

    /**
     * 获取所有用户
     *
     * @param name
     * @return
     */
    @Override
    public List<User> getUser(String name) {
        QueryWrapper<User> wrapper = Wrappers.query();
        wrapper.like(StringUtils.isNotBlank(name), "name", name)
                .ne("is_deleted", 1)
                .orderByAsc("login_name");
        return um.selectList(wrapper);
    }

    /**
     * 分页查询用户
     *
     * @param name
     * @param pageNum
     * @param pageSize
     * @return
     */
    @Override
    public IPage<User> getUserPage(String name, Long pageNum, Long pageSize) {
        IPage<User> page = new Page<>(pageNum, pageSize);
        QueryWrapper<User> wrapper = new QueryWrapper();
        wrapper.like(StringUtils.isNoneBlank(name), "name", name)
                .eq("is_deleted", 0)
                .orderByAsc("login_name");
        IPage pageList = this.page(page, wrapper);
        return pageList;
    }

    /**
     * 新增用户
     *
     * @param userDto
     * @return
     */
    @Override
    public void addUser(UserDto userDto) {
        User user1 = this.getUserByLoginName(userDto.getLoginName());
        if (user1 != null) {
            throw new MessageException("登录名已存在");
        }
        User user = new User();
        BeanUtil.copyProperties(userDto, user);
        user.setId(IdUtil.simpleUUID());
        user.setPassword(DigestUtils.md5DigestAsHex("123456".getBytes()).toLowerCase());
        this.save(user);
    }


    @Override
    public User getUserById(String id) {
        User user = um.selectById(id);
        return user;
    }

    /**
     * 更改密码
     *
     * @param modifyPwd
     * @return
     */
    @Override
    public void modifyPassword(ModifyPasswordDto modifyPwd) {

        User user = um.selectById(modifyPwd.getUserId());
        if (user == null) {
            throw new MessageException("用户不存在");
        }
        if (!user.getPassword().toLowerCase().equals(DigestUtil.md5Hex(modifyPwd.getOldPassword()).toLowerCase())) {
            throw new MessageException("旧密码输入错误");
        } else {
            if (modifyPwd.getNewPassword().equals(modifyPwd.getConfirmPassword())) {
                String password = DigestUtil.md5Hex(modifyPwd.getNewPassword()).toUpperCase();
                user.setPassword(password);
                user.setModifier(user.getLoginName());
                um.updateById(user);
            } else {
                throw new MessageException("两次密码输入不一致");
            }
        }
    }

    /**
     * 重置密码
     *
     * @param id
     * @param loginName
     */
    @Override
    public void resetPassword(String id, String loginName) {

        User user = um.selectById(id);
        if (user == null) {
            throw new MessageException("用户不存在");
        } else {
            user.setPassword(DigestUtil.md5Hex("123456".getBytes()).toLowerCase());
            user.setModifier(loginName);
            um.updateById(user);
        }
    }

    /**
     * 删除用户
     *
     * @param id
     * @param loginName
     */
    @Override
    public void deleteUser(String id, String loginName) {
        User user = um.selectById(id);
        if (user == null) {
            throw new MessageException("用户不存在");
        } else {
            user.setIsDeleted(1);
            user.setModifier(loginName);
            um.updateById(user);
        }
    }


    /**
     * 编辑用户
     *
     * @param user
     */
    @Override
    public void modifyUser(UserDto user) {
        User entity = um.selectById(user.getId());
        if (entity == null) {
            throw new MessageException("用户不存在,请刷新后重试");
        }
        BeanUtil.copyProperties(user, entity);
        um.updateById(entity);
    }


    /**
     * 配置用户角色
     *
     * @param roleIds
     * @param userId
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void configUserRoles(List<String> roleIds, String userId) {

        List<Role_User> roleUsers = roleUserService.list(new QueryWrapper<Role_User>().eq("user_id", userId));
        if (roleIds != null && roleUsers.size() > 0) {
            roleUserService.remove(new QueryWrapper<Role_User>().eq("user_d", userId));
        }
        if (roleIds.size() > 0) {
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
    }

    /**
     * 登录
     *
     * @param loginDto
     * @return
     */
    @Override
    public UserTokenVo login(LoginDto loginDto) {

        String pwd = DigestUtils.md5DigestAsHex(loginDto.getPassword().getBytes()).toUpperCase();
        User user = getUserByLoginName(loginDto.getLoginName());

        if (user == null) {
            throw new MessageException("用户名不存在!");
        }
        if (user.getIsEnabled() != 1) {
            throw new MessageException("用户已被禁用,请联系管理员!");
        }
        if (!pwd.equals(user.getPassword().toUpperCase())) {
            throw new MessageException("密码错误!");
        }

        LoginUser u = new LoginUser();
        u.setId(user.getId());
        u.setLoginName(user.getLoginName());
        ObjectMapper objectMapper = new ObjectMapper();
        String json = null;

        try {
            json = objectMapper.writeValueAsString(u);
        } catch (JsonProcessingException e) {
            throw new MessageException("密码错误!");
        }
        // JWT
        String token = JWTUtil.CreateToken(loginDto.getLoginName(), json, 2 * 60L * 60L * 1000L);
        UserTokenVo tokenVo = new UserTokenVo();
        tokenVo.setToken(token);
        tokenVo.setUserId(user.getId());
        tokenVo.setUserName(user.getName());
        tokenVo.setLoginName(user.getLoginName());

        // 登录日志
        LoginLog log = new LoginLog();
        log.setId(IdUtil.simpleUUID());
        log.setClientIp(loginDto.getIp());
        log.setOperator(user.getName());
        log.setOperatorId(user.getId());
        log.setOperatorLoginName(user.getLoginName());
        log.setInTime(new Date());
        loginLogService.save(log);

        return tokenVo;
    }

    /**
     * 登出
     *
     * @param userId
     */
    @Override
    public void logout(String userId) {
        QueryWrapper<LoginLog> wrapper = new QueryWrapper<>();
        wrapper.eq("operator_id", userId);
        wrapper.orderByDesc("in_time").last("limit 1");
        LoginLog one = loginLogService.getOne(wrapper);
        one.setOutTime(new Date());
        loginLogService.updateById(one);
    }

    @Override
    public UserTokenVo refreshToken(String loginName){
        User user = getUserByLoginName(loginName);

        if (user == null) {
            throw new MessageException("用户名不存在!");
        }
        LoginUser u = new LoginUser();
        u.setId(user.getId());
        u.setLoginName(user.getLoginName());
        ObjectMapper objectMapper = new ObjectMapper();
        String json = null;

        try {
            json = objectMapper.writeValueAsString(u);
        } catch (JsonProcessingException e) {
            throw new MessageException("密码错误!");
        }
        // JWT
        String token = JWTUtil.CreateToken(loginName, json,2 * 60L * 60L * 1000L);
        String refreshToken = JWTUtil.CreateToken(loginName, json,2 * 60L * 60L * 1000L);
        UserTokenVo tokenVo = new UserTokenVo();
        tokenVo.setToken(token);
        tokenVo.setUserId(user.getId());
        tokenVo.setUserName(user.getName());
        tokenVo.setLoginName(user.getLoginName());
        return tokenVo;
    }
}
