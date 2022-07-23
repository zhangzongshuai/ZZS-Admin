package com.zzs.zzsadmin.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.zzs.zzsadmin.entity.Role_User;
import com.zzs.zzsadmin.vo.Role.RoleUserVo;
import com.zzs.zzsadmin.vo.Role.UserRoleVo;


import java.util.List;

public interface IRoleUserService extends IService<Role_User> {

    IPage<RoleUserVo> getUsersByRoleId(String roleId,String name, Long pageNum, Long pageSize);

    void configRoleUser( List<String> userIds, String roleId);

    IPage<UserRoleVo> getRolesByUserId(String userId,Long pageNum, Long pageSize);
}
