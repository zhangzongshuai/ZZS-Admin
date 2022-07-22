package com.zzs.zzsadmin.service;


import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.zzs.zzsadmin.dto.user.LoginDto;
import com.zzs.zzsadmin.dto.user.ModifyPasswordDto;
import com.zzs.zzsadmin.dto.user.UserDto;
import com.zzs.zzsadmin.dto.userinfo;
import com.zzs.zzsadmin.entity.User;
import com.zzs.zzsadmin.vo.user.UserTokenVo;
import org.springframework.web.bind.annotation.RequestParam;

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
    int addUser(User user,String loginName);

    /**
     * 根据用户id获取用户信息
     * @param id
     * @return
     */
    User getUserById(String id);

    /**
     * 更改密码
     *
     * @param modifyPwd
     * @return
     */
    void modifyPassword(ModifyPasswordDto modifyPwd);

    /**
     * 重置密码
     * @param id
     * @param loginName
     */
    void resetPassword(String id, String loginName);

    /**
     * 删除用户
     * @param id
     * @param loginName
     */
    void deleteUser(String id, String loginName);

    /**
     * 修改用户
     * @param user
     */
    void modifyUser(UserDto user);

    void configUserRoles(List<String> roleIds, String userId);

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
