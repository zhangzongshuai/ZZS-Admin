package com.zzs.zzsadmin.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.zzs.zzsadmin.entity.AccessLog;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

/**
 * <p></p>
 *
 * @author 张宗帅
 * @date 2021-01-20
 */
@Mapper
@Repository
public interface AccessLogMapper extends BaseMapper<AccessLog> {

}
