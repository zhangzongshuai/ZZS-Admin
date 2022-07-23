package com.zzs.zzsadmin.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.zzs.zzsadmin.entity.Role_Menu;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

@Mapper
@Repository
public interface RoleMenuMapper extends BaseMapper<Role_Menu> {
}
