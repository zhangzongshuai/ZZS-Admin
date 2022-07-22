package com.zzs.zzsadmin.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.zzs.zzsadmin.entity.Role_User;
import com.zzs.zzsadmin.vo.Role.RoleUserVo;
import com.zzs.zzsadmin.vo.Role.UserRoleVo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

@Mapper
@Repository
public interface RoleUserMapper extends BaseMapper<Role_User> {

    IPage<RoleUserVo> getUsersByRoleId(IPage<RoleUserVo> page, @Param("roleId") String roleId,@Param("name") String name);

    IPage<UserRoleVo> getRolesByUserId(IPage<UserRoleVo> page, @Param("userId") String userId);
}
