package com.zzs.zzsadmin.service.impl;


import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zzs.zzsadmin.entity.Organization;
import com.zzs.zzsadmin.mapper.OrganizationMapper;
import com.zzs.zzsadmin.service.IOrganizationService;
import com.zzs.zzsadmin.vo.organization.OrganizationVo;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * (Organization)表服务实现类
 *
 * @author makejava
 * @since 2022-09-20 22:07:41
 */
@Service
public class OrganizationServiceImpl extends ServiceImpl<OrganizationMapper, Organization> implements IOrganizationService {
    @Resource
    private OrganizationMapper organizationMapper;

    /**
     * 通过ID查询单条数据
     *
     * @param id 主键
     * @return 实例对象
     */
    @Override
    public OrganizationVo getOrgById(String id) {
        Organization organization = this.getById(id);
        OrganizationVo organizationVo = new OrganizationVo();
        BeanUtil.copyProperties(organization,organizationVo);
        return organizationVo;
    }
}
