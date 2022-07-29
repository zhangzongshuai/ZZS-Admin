package com.zzs.zzsadmin.controller;

import com.zzs.zzsadmin.annotation.IgnoreToken;
import com.zzs.zzsadmin.common.utils.IpUtil;
import com.zzs.zzsadmin.common.vo.BaseResultData;
import com.zzs.zzsadmin.common.vo.ResultData;
import com.zzs.zzsadmin.dto.user.LoginDto;
import com.zzs.zzsadmin.service.IUserService;
import com.zzs.zzsadmin.vo.user.UserTokenVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;


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

//    @ApiOperation(value = "刷新token")
//    @GetMapping("/refreshToken")
//    public ResultData<UserTokenVo> refreshToken(@RequestParam String loginName) {
//        UserTokenVo userTokenVo = userService.refreshToken(loginName);
//        return new ResultData<>(userTokenVo);
//    }

}
