package com.zzs.zzsadmin.interceptor;

import com.auth0.jwt.exceptions.JWTDecodeException;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.zzs.zzsadmin.annotation.IgnoreToken;
import com.zzs.zzsadmin.common.utils.JWTUtil;
import com.zzs.zzsadmin.entity.User;
import com.zzs.zzsadmin.service.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.reflect.Method;
import java.util.Date;

/**
 * 拦截器
 *
 * @author 张宗帅
 * @date
 */
public class AuthenticationInterceptor implements HandlerInterceptor {

    @Autowired
    private IUserService userService;

    @Override
    public boolean preHandle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object object) throws Exception {
        // 从 http 请求头中取出 token
        String token = httpServletRequest.getHeader("Authorization");
        // 如果不是映射到方法直接通过
        if (!(object instanceof HandlerMethod)) {
            return true;
        }
        HandlerMethod handlerMethod = (HandlerMethod) object;
        Method method = handlerMethod.getMethod();

        //检查是否有IgnoreToken注释，有则跳过认证
        if (method.isAnnotationPresent(IgnoreToken.class)) {
            IgnoreToken ignoreToken = method.getAnnotation(IgnoreToken.class);
            if (ignoreToken.required()) {
                return true;
            }
        }
        if (token == null) {
            httpServletResponse.setStatus(401);
            return false;
        }

        String username;
        Date expiresAt;
        try {
            DecodedJWT decodedJWT = JWTUtil.deToken(token);
            username = decodedJWT.getClaim("username").asString();
            expiresAt = decodedJWT.getExpiresAt();
            if (expiresAt.getTime() < System.currentTimeMillis()) {
                httpServletResponse.setStatus(401);
                return false;
            }
        } catch (JWTDecodeException j) {
            httpServletResponse.setStatus(401);
            return false;
        } catch (JWTVerificationException e) {
            httpServletResponse.setStatus(401);
            return false;
        }


        User user = userService.getUserByLoginName(username);
        if (user == null) {
            httpServletResponse.setStatus(401);
            return false;
        }
        if (user.getIsEnabled() != 1) {
            httpServletResponse.setStatus(401);
            return false;
        }
        return true;
    }

    @Override
    public void postHandle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o, ModelAndView modelAndView) throws Exception {

    }

    @Override
    public void afterCompletion(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o, Exception e) throws Exception {

    }
}
