package com.zzs.zzsadmin.controller;

import cn.hutool.core.bean.BeanUtil;
import com.zzs.zzsadmin.common.vo.ResultDataList;
import com.zzs.zzsadmin.entity.Icon;
import com.zzs.zzsadmin.service.IconService;
import com.zzs.zzsadmin.vo.icon.IconVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

/**
 * (Icon)表控制层
 *
 * @author makejava
 * @since 2022-07-26 10:04:30
 */
@Api(tags = "图标管理")
@RestController
@RequestMapping("api/iconManager")
public class IconController {
    /**
     * 服务对象
     */
    @Autowired
    private IconService iconService;

    /**
     * 查询所有图标
     *
     * @return 查询结果
     */
    @ApiOperation("查询所有图标")
    @GetMapping("/iconList")
    public ResultDataList<IconVo> getIconList() {
        List<Icon> iconList = iconService.getIconList();
        List<IconVo> list = new ArrayList<>();
        iconList.forEach(f -> {
            IconVo iconVo = new IconVo();
            BeanUtil.copyProperties(f, iconVo);
            list.add(iconVo);
        });
        return new ResultDataList<>(list);
    }

}

