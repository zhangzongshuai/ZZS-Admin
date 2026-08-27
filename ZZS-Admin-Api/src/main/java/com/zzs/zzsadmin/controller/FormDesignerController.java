package com.zzs.zzsadmin.controller;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.IdUtil;
import com.zzs.zzsadmin.common.utils.IpUtil;
import com.zzs.zzsadmin.common.vo.BaseResultData;
import com.zzs.zzsadmin.common.vo.ResultData;
import com.zzs.zzsadmin.dto.AccessLogDto;
import com.zzs.zzsadmin.entity.AccessLog;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.websocket.server.PathParam;
import java.util.Date;

/**
 * <p></p>
 *
 * @author 张宗帅
 * @date 2022-08-10 17:02:56
 */
@Api(tags = "表单设计器")
@RestController
@RequestMapping("/api/formDesigner")
public class FormDesignerController {
    @ApiOperation("添加访问日志")
    @PostMapping("/form/{formCode}")
    public ResultData addAccessLog(@PathParam(value = "formCode") String formCode) {
        return new ResultData();
    }
}
