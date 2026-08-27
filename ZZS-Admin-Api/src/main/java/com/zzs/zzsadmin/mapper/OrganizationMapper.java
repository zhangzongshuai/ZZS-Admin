package com.zzs.zzsadmin.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.zzs.zzsadmin.entity.Organization;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

/**
 * <p></p>
 *
 * @author zzs
 * @date 2022-09-20 21:25:37
 */
@Mapper
@Repository
public interface OrganizationMapper extends BaseMapper<Organization> {
}
