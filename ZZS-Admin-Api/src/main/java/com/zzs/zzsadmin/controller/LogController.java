package com.zzs.zzsadmin.controller;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.IdUtil;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.zzs.zzsadmin.common.utils.AssertUtil;
import com.zzs.zzsadmin.common.utils.CopyUtil;
import com.zzs.zzsadmin.common.utils.IpUtil;
import com.zzs.zzsadmin.common.vo.BaseResultData;
import com.zzs.zzsadmin.common.vo.PageResult;
import com.zzs.zzsadmin.dto.AccessLogDto;
import com.zzs.zzsadmin.entity.AccessLog;
import com.zzs.zzsadmin.entity.LoginLog;
import com.zzs.zzsadmin.service.IAccessLogService;
import com.zzs.zzsadmin.service.ILoginLogService;
import com.zzs.zzsadmin.vo.log.AccessLogVo;
import com.zzs.zzsadmin.vo.log.LoginLogVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
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
 * @date 2021-01-22 10:30:33
 */
@Api(tags = "登录日志管理")
@RestController
@RequestMapping("/api/logManager")
public class LogController {
    @Autowired
    private ILoginLogService loginLogService;

    @Autowired
    private IAccessLogService accessLogService;

    /**
     * <p>查询列表</p>
     *
     * @param
     * @return PageResult<LoginLog> 分页返回结果 {@link LoginLog}
     * @author 张宗帅
     * @date 2021-01-22 10:30:33
     */
    @ApiOperation("分页查询登录日志")
    @GetMapping("/loginLogPage")
    public PageResult<LoginLogVo> getLoginLogPage(Long pageNum, Long pageSize, String userName, String startTime, String endTime) {

        IPage<LoginLog> pageList = loginLogService.getLoginLogPage(pageNum, pageSize, userName, startTime, endTime);
        PageResult<LoginLogVo> res = new PageResult<>(pageList, false);
        List<LoginLogVo> list = new ArrayList<>();
        pageList.getRecords().forEach(f -> {
            LoginLogVo dto = new LoginLogVo();
            BeanUtil.copyProperties(f, dto);
            list.add(dto);
        });
        res.setDatas(list);
        return res;
    }

    @ApiOperation("分页查询访问日志")
    @GetMapping("/accessLogPage")
    public PageResult<AccessLogVo> getAccessLogPage(Long pageNum, Long pageSize, String moduleName, String userName, String startTime, String endTime) {
        AssertUtil.valid(pageNum, "pageNum", "参数错误");
        AssertUtil.valid(pageSize, "pageSize", "参数错误");
        IPage<AccessLog> pageList = accessLogService.getAccessLogPage(pageNum, pageSize, moduleName, userName, startTime, endTime);
        PageResult<AccessLogVo> res = new PageResult<>(pageList, false);
        List<AccessLogVo> list = new ArrayList<>();
        pageList.getRecords().forEach(f -> {
            AccessLogVo accessLogVo = new AccessLogVo();
            BeanUtil.copyProperties(f, accessLogVo);
            list.add(accessLogVo);
        });
        res.setDatas(list);
        return res;
    }


    @ApiOperation("添加访问日志")
    @PostMapping("/addAccessLog")
    public BaseResultData addAccessLog(@RequestBody AccessLogDto accessLogDto, HttpServletRequest request) {
        AccessLog accessLog = new AccessLog();
        BeanUtil.copyProperties(accessLogDto, accessLog);
        accessLog.setId(IdUtil.simpleUUID());
        accessLog.setClientIp(IpUtil.getIp(request));
        accessLog.setOperateTime(new Date());
        accessLogService.addAccessLog(accessLog);
        return new BaseResultData();
    }

}
