package com.zzs.zzsadmin.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zzs.zzsadmin.entity.Icon;
import com.zzs.zzsadmin.mapper.IconMapper;
import com.zzs.zzsadmin.service.IconService;
import org.springframework.stereotype.Service;

import java.util.List;


/**
 * (Icon)表服务实现类
 *
 * @author makejava
 * @since 2022-07-26 10:04:30
 */
@Service
public class IconServiceImpl extends ServiceImpl<IconMapper, Icon> implements IconService {

    @Override
    public List<Icon> getIconList() {
        return this.list();
    }
}
