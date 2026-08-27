package com.zzs.zzsadmin.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zzs.zzsadmin.common.utils.CopyUtil;
import com.zzs.zzsadmin.entity.Dictionary;
import com.zzs.zzsadmin.mapper.DictionaryMapper;
import com.zzs.zzsadmin.service.IDictionaryService;
import com.zzs.zzsadmin.vo.dictionary.DictionaryVo;
import org.apache.commons.lang3.StringUtils;

import javax.annotation.Resource;
import java.util.List;

/**
 * (Dictionary)表服务实现类
 *
 * @author makejava
 * @since 2022-12-22 13:59:39
 */
public class DictionaryServiceImpl extends ServiceImpl<DictionaryMapper,Dictionary> implements IDictionaryService {
    @Resource
    private DictionaryMapper dictionaryMapper;

    @Override
    public List<DictionaryVo> getList(String name) {
        QueryWrapper<Dictionary> wrapper = new QueryWrapper<>();
        wrapper.like(StringUtils.isNotBlank(name),"name",name)
                .eq("is_deleted",0);
        List<Dictionary> dictionaries = dictionaryMapper.selectList(wrapper);
        List<DictionaryVo> list = CopyUtil.entityToVoList(dictionaries,DictionaryVo.class);
        return list;
    }
}
