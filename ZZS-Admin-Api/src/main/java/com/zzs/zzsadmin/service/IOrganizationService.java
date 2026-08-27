package com.zzs.zzsadmin.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.zzs.zzsadmin.entity.Organization;
import com.zzs.zzsadmin.vo.organization.OrganizationVo;

/**
 * <p></p>
 *
 * @author zzs
 * @date 2022-09-20 21:52:02
 */
public interface IOrganizationService extends IService<Organization> {

    /***
    * <p></p>
    * @param  id
    * @return com.zzs.zzsadmin.vo.organization.OrganizationVo
    * @author zzs
    * @date 2022/9/20 22:20
    */
    OrganizationVo getOrgById(String id);

}
