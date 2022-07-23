package com.zzs.zzsadmin.service;


import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.zzs.zzsadmin.dto.RoleDto;
import com.zzs.zzsadmin.entity.Role;

public interface IRoleService  extends IService<Role> {
    /**
     * 新增角色
     * @param roleDto
     * @param loginName
     */
    void addRole(RoleDto roleDto,String loginName);

    /**
     * 分页查询角色
     * @param name
     * @param pageNum
     * @param pageSize
     * @return
     */
    IPage<Role> getRolesPage(String name, Long pageNum, Long pageSize);

    /**
     * 修改角色信息
     * @param roleDto
     * @param loginName
     */
    void modifyRole(RoleDto roleDto,String loginName);

    /**
     * 删除角色
     * @param id
     */
    void deleteRole(String id);
}
