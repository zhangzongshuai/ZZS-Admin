package com.zzs.zzsadmin.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.zzs.zzsadmin.entity.Dictionary;
import com.zzs.zzsadmin.vo.dictionary.DictionaryVo;

import java.util.List;

/**
 * (Dictionary)表服务接口
 *
 * @author makejava
 * @since 2022-12-22 13:59:39
 */
public interface IDictionaryService extends IService<Dictionary> {

    List<DictionaryVo> getList(String name);

}
