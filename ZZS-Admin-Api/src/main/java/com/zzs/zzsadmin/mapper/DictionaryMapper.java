package com.zzs.zzsadmin.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.zzs.zzsadmin.entity.Dictionary;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * (Dictionary)表数据库访问层
 *
 * @author makejava
 * @since 2022-12-22 14:01:39
 */
@Mapper
@Repository
public interface DictionaryMapper  extends BaseMapper<Dictionary> {

}

