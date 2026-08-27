package com.zzs.zzsadmin.controller;

import cn.hutool.captcha.CaptchaUtil;
import cn.hutool.captcha.ICaptcha;
import com.zzs.zzsadmin.annotation.IgnoreToken;
import com.zzs.zzsadmin.common.exception.MessageException;
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
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;


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
        HttpSession session = request.getSession();
        String realCaptcha = (String) session.getAttribute("captcha");
        if (realCaptcha == null || !realCaptcha.toUpperCase().equals(login.getCaptcha().toUpperCase())){
            throw new MessageException("验证码错误!");
        }
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


    @IgnoreToken
    @ApiOperation(value = "验证码")
    @GetMapping("/captcha")
    public void captcha(HttpServletRequest request, HttpServletResponse response)throws IOException {
        response.setContentType("image/jpeg");
        //定义图形验证码的长和宽
        ICaptcha captcha = CaptchaUtil.createLineCaptcha(80, 40, 4, 100);
        captcha.write(response.getOutputStream());
        String captchaText = captcha.getCode();
        HttpSession session = request.getSession();
        session.setAttribute("captcha", captchaText);
    }

//    @ApiOperation(value = "刷新token")
//    @GetMapping("/refreshToken")
//    public ResultData<UserTokenVo> refreshToken(@RequestParam String loginName) {
//        UserTokenVo userTokenVo = userService.refreshToken(loginName);
//        return new ResultData<>(userTokenVo);
//    }

}
