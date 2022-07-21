package com.zzs.zzsadmin.service;


import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.zzs.zzsadmin.dto.user.LoginDto;
import com.zzs.zzsadmin.dto.user.ModifyPasswordDto;
import com.zzs.zzsadmin.dto.user.UserDto;
import com.zzs.zzsadmin.dto.userinfo;
import com.zzs.zzsadmin.entity.User;
import com.zzs.zzsadmin.vo.user.UserTokenVo;

import java.util.List;

public interface IUserService extends IService<User> {

    /**
     * 根据用户名获取用户信息
     *
     * @param name
     * @return
     */
    User getUserByLoginName(String name);

    /**
     * 查询所有用户
     *
     * @param name
     * @return
     */
    List<User> getUser(String name);

    /**
     * 分页查询用户
     *
     * @param name
     * @param pageNum
     * @param pageSize
     * @return
     */
    IPage<User> getUserPage(String name, Long pageNum, Long pageSize);

    /**
     * 添加用户
     *
     * @param user
     * @return
     */
    int addUser(User user);

    int GetUserById(String id, User user);

    /**
     * 更改密码
     *
     * @param modifyPwd
     * @return
     */
    void modifyPassword(ModifyPasswordDto modifyPwd);

    void resetPassword(String id, String loginName);

    void deleteUser(String id, String loginName);

    void modifyUser(UserDto user);

    /**
     * 登录
     *
     * @param loginDto
     * @return
     */
    UserTokenVo login(LoginDto loginDto);

    /**
     * 登出
     *
     * @param userId
     */
    void logout(String userId);
}
