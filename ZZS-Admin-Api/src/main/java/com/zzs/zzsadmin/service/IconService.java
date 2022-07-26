package com.zzs.zzsadmin.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.zzs.zzsadmin.entity.Icon;

import java.util.List;

/**
 * (Icon)表服务接口
 *
 * @author makejava
 * @since 2022-07-26 10:04:30
 */
public interface IconService extends IService<Icon> {
    List<Icon> getIconList();
}
