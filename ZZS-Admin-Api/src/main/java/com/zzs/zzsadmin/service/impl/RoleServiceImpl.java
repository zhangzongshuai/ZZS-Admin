package com.zzs.zzsadmin.service.impl;


import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.IdUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zzs.zzsadmin.common.exception.MessageException;
import com.zzs.zzsadmin.dto.RoleDto;
import com.zzs.zzsadmin.entity.Role;
import com.zzs.zzsadmin.entity.Role_User;
import com.zzs.zzsadmin.mapper.RoleMapper;
import com.zzs.zzsadmin.service.IRoleService;
import com.zzs.zzsadmin.service.IRoleUserService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author zzs
 */
@Service
public class RoleServiceImpl extends ServiceImpl<RoleMapper, Role> implements IRoleService {

    private IRoleUserService roleUserService;

    /**
     * 添加角色
     *
     * @param role
     * @param loginName
     */
    @Override
    public void addRole(Role role, String loginName) {
        role.setId(IdUtil.simpleUUID());
        role.setCreator(loginName);
        this.save(role);
    }

    /**
     * 分页查询角色
     *
     * @param name
     * @param pageNum
     * @param pageSize
     * @return
     */
    @Override
    public IPage<Role> getRolesPage(String name, Long pageNum, Long pageSize) {
        IPage<Role> page = new Page<>(pageNum, pageSize);
        QueryWrapper wrapper = new QueryWrapper();
        if (StringUtils.isNotBlank(name)) {
            wrapper.like("name", name);
        }
        wrapper.orderByAsc("create_time");
        IPage pageList = this.page(page, wrapper);
        return pageList;
    }

    /**
     * 修改角色信息
     *
     * @param roleDto
     * @param loginName
     */
    @Override
    public void modifyRole(RoleDto roleDto, String loginName) {
        Role role = this.getById(roleDto.getId());
        if (role == null) {
            throw new MessageException("角色不存在,请刷新后重试");
        }
        BeanUtil.copyProperties(roleDto, role);
        role.setModifier(loginName);
        this.updateById(role);
    }

    /**
     * 删除角色
     *
     * @param id
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteRole(String id) {
        Role byId = this.getById(id);
        if (byId == null) {
            throw new MessageException("角色不存在,请刷新后重试");
        }
        this.removeById(id);
        roleUserService.remove(new QueryWrapper<Role_User>().eq("role_id", id));
    }
}
