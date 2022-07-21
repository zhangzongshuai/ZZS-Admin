package com.zzs.zzsadmin.controller;


import cn.hutool.core.util.IdUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.zzs.zzsadmin.common.utils.AssertUtil;
import com.zzs.zzsadmin.common.utils.CopyUtil;
import com.zzs.zzsadmin.common.utils.IpUtil;
import com.zzs.zzsadmin.common.vo.BaseResultData;
import com.zzs.zzsadmin.common.vo.PageResult;
import com.zzs.zzsadmin.dto.AccessLogDto;
import com.zzs.zzsadmin.entity.AccessLog;
import com.zzs.zzsadmin.service.IAccessLogService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * <p></p>
 *
 * @author 张宗帅
 * @date 2021-01-20 23:06:15
 */
@RestController
@RequestMapping("/api/accesslog")
public class AccessLogController {

    @Autowired
    private IAccessLogService accessLogService;

    /**
     * <p>查询列表</p>
     *
     * @param
     * @return PageResult<AccessLog> 分页返回结果 {@link AccessLog}
     * @author 张宗帅
     * @date 2021-01-20
     */
    @GetMapping("/pagelist")
    public PageResult<AccessLogDto> list(Long pageNum, Long pageSize, String moduleName, String userName, String startTime, String endTime) {

        AssertUtil.valid(pageNum, "pageNum", "参数错误");
        AssertUtil.valid(pageSize, "pageSize", "参数错误");
        IPage<AccessLog> page = new Page<>(pageNum, pageSize);
        QueryWrapper<AccessLog> wrapper = new QueryWrapper<>();
        if (StringUtils.isNotBlank(moduleName)) {
            wrapper.like("operate_module", moduleName);
        }
        if (StringUtils.isNotBlank(userName)){
            wrapper.like("operator",userName);
        }
        if (StringUtils.isNotBlank(startTime)){
            wrapper.ge("operate_time",startTime);
        }
        if (StringUtils.isNotBlank(endTime)){
            wrapper.le("operate_time",endTime);
        }
        wrapper.orderByDesc("operate_time");
        IPage<AccessLog> pageList = accessLogService.page(page, wrapper);
        PageResult<AccessLogDto> res = new PageResult<>(pageList,false);
        List<AccessLogDto> list = new ArrayList<>();
        pageList.getRecords().forEach(f->{
            AccessLogDto dto = new AccessLogDto();
            CopyUtil.CobyTo(f,dto);
            list.add(dto);
        });
        res.setDatas(list);
        return res;
    }

    @PostMapping("/add")
    public BaseResultData Add(@RequestBody AccessLog log, HttpServletRequest request) {

        log.setId(IdUtil.simpleUUID());
        log.setClientIp(IpUtil.getIp(request));
        log.setOperateTime(new Date());
        accessLogService.addAccessLog(log);
        return new BaseResultData();
    }
}
