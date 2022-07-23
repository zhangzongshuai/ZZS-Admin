package com.zzs.zzsadmin.controller;

import cn.hutool.core.util.IdUtil;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.zzs.zzsadmin.annotation.IgnoreToken;
import com.zzs.zzsadmin.common.utils.IpUtil;
import com.zzs.zzsadmin.common.utils.JWTUtil;
import com.zzs.zzsadmin.common.vo.BaseResultData;
import com.zzs.zzsadmin.common.vo.ResultData;
import com.zzs.zzsadmin.dto.Login;
import com.zzs.zzsadmin.dto.LoginUser;
import com.zzs.zzsadmin.dto.user.LoginDto;
import com.zzs.zzsadmin.entity.LoginLog;
import com.zzs.zzsadmin.entity.User;
import com.zzs.zzsadmin.service.ILoginLogService;
import com.zzs.zzsadmin.service.IUserService;
import com.zzs.zzsadmin.vo.user.UserTokenVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.DigestUtils;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;


@RestController
@RequestMapping("/api")
@Api(tags = "登录")
public class LoginController {

    @Autowired
    private IUserService userService;

    @IgnoreToken
    @PostMapping("/login")
    @ApiOperation(value = "登录")
    public ResultData<UserTokenVo> login(@RequestBody LoginDto login, HttpServletRequest request) {
        String ip = IpUtil.getIp(request);
        login.setIp(ip);
        UserTokenVo userTokenVo = userService.login(login);
        return new ResultData<>(userTokenVo);
    }


    @ApiOperation(value = "登出")
    @PostMapping("/logout")
    public BaseResultData logout(@RequestParam String userId) {
        userService.logout(userId);
        return new BaseResultData();
    }


}
