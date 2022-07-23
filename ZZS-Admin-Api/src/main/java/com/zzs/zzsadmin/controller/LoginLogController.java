package com.zzs.zzsadmin.controller;

import cn.hutool.core.date.DateUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.zzs.zzsadmin.common.utils.CopyUtil;
import com.zzs.zzsadmin.common.vo.PageResult;
import com.zzs.zzsadmin.dto.LoginLogDto;
import com.zzs.zzsadmin.entity.LoginLog;
import com.zzs.zzsadmin.service.ILoginLogService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * <p></p>
 *
 * @author 张宗帅
 * @date 2021-01-22 10:30:33
 */
@RestController
@RequestMapping("/api/loginlog")
public class LoginLogController {
    @Autowired
    private ILoginLogService loginLogService;

    /**
     * <p>查询列表</p>
     *
     * @param
     * @return PageResult<LoginLog> 分页返回结果 {@link LoginLog}
     * @author 张宗帅
     * @date 2021-01-22 10:30:33
     */
    @GetMapping("/pagelist")
    public PageResult<LoginLogDto> list(Long pageNum, Long pageSize, String userName, String startTime, String endTime) {

        IPage<LoginLog> page = new Page<>(pageNum, pageSize);
        QueryWrapper<LoginLog> wrapper = new QueryWrapper<>();
        if (StringUtils.isNotBlank(userName)) {
            wrapper.like("operator", userName);
        }
        if (StringUtils.isNotBlank(startTime)) {
            wrapper.ge("in_time", startTime);
        }
        if (StringUtils.isNotBlank(endTime)) {
            Date eTime = DateUtil.offsetDay(DateUtil.parse(endTime), 1);
            wrapper.lt("in_time", eTime);
        }
        wrapper.orderByDesc("in_time");
        IPage<LoginLog> pageList = loginLogService.page(page, wrapper);
        PageResult<LoginLogDto> res = new PageResult<>(pageList, false);
        List<LoginLogDto> list = new ArrayList<>();
        pageList.getRecords().forEach(f -> {
            LoginLogDto dto = new LoginLogDto();
            CopyUtil.CobyTo(f, dto);
            list.add(dto);
        });
        res.setDatas(list);
        return res;
    }

}
