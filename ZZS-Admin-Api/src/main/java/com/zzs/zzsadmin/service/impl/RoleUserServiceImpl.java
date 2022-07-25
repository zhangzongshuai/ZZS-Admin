package com.zzs.zzsadmin.service.impl;


import cn.hutool.core.util.IdUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zzs.zzsadmin.entity.Role_User;
import com.zzs.zzsadmin.mapper.RoleUserMapper;
import com.zzs.zzsadmin.service.IRoleUserService;
import com.zzs.zzsadmin.vo.role.RoleUserVo;
import com.zzs.zzsadmin.vo.role.UserRoleVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
public class RoleUserServiceImpl extends ServiceImpl<RoleUserMapper, Role_User> implements IRoleUserService {

    @Autowired
    private RoleUserMapper roleUserMapper;

    /**
     * 很具角色id获取用户
     *
     * @param roleId
     * @param name
     * @param pageNum
     * @param pageSize
     * @return
     */
    @Override
    public IPage<RoleUserVo> getUsersByRoleId(String roleId, String name, Long pageNum, Long pageSize) {
        IPage<RoleUserVo> page = new Page<>();
        page.setSize(pageSize);
        page.setCurrent(pageNum);
        IPage<RoleUserVo> usersByRoleId = roleUserMapper.getUsersByRoleId(page, roleId, name);
        return usersByRoleId;
    }

    /**
     * 角色分配用户
     *
     * @param userIds
     * @param roleId
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void configRoleUser(List<String> userIds, String roleId) {
        List<Role_User> roleUsers = this.list(new QueryWrapper<Role_User>().eq("role_id", roleId));
        if (roleUsers.size() > 0) {
            this.remove(new QueryWrapper<Role_User>().eq("role_id", roleId));
        }
        if (userIds.size() > 0) {
            List<Role_User> list = new ArrayList<>();
            userIds.forEach(f -> {
                Role_User rm = new Role_User();
                rm.setId(IdUtil.simpleUUID());
                rm.setRoleId(roleId);
                rm.setUserId(f);
                list.add(rm);
            });
            this.saveBatch(list);
        }
    }

    /**
     * 根据用户id分配的角色
     *
     * @param userId
     * @param pageNum
     * @param pageSize
     * @return
     */
    @Override
    public IPage<UserRoleVo> getRolesByUserId(String userId, Long pageNum, Long pageSize) {
        IPage<UserRoleVo> page = new Page<>();
        page.setCurrent(pageNum);
        page.setSize(pageSize);
        IPage<UserRoleVo> rolesByUserId = roleUserMapper.getRolesByUserId(page, userId);
        return rolesByUserId;
    }
}
