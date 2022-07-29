package com.zzs.zzsadmin.config;


import cn.hutool.core.exceptions.ExceptionUtil;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.zzs.zzsadmin.common.exception.ArgumentException;
import com.zzs.zzsadmin.common.exception.MessageException;
import com.zzs.zzsadmin.common.exception.TokenException;
import com.zzs.zzsadmin.common.utils.JWTUtil;
import com.zzs.zzsadmin.common.vo.BaseResultData;
import com.zzs.zzsadmin.common.vo.ResultData;
import com.zzs.zzsadmin.dto.LoginUser;
import com.zzs.zzsadmin.entity.User;
import com.zzs.zzsadmin.service.IUserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.util.DigestUtils;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

@ControllerAdvice
@ResponseBody
//若所有异常处理类返回的数据格式为json，则可以使用 @RestControllerAdvice 代替 @ControllerAdvice
//@RestControllerAdvice = @ControllerAdvice + @ResponseBody
public class ExceptionAdvice {

    private static final Logger log = LoggerFactory.getLogger(ExceptionAdvice.class);

    @Autowired
    private IUserService userService;


    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler({IllegalArgumentException.class})
    public BaseResultData badRequestException(IllegalArgumentException e) {
        return this.defHandler(e);
    }

    @ExceptionHandler({ArgumentException.class})
    public BaseResultData paramsException(ArgumentException e) {
        return this.defHandler(e);
    }

    @ExceptionHandler({MessageException.class})
    public BaseResultData mesException(MessageException e) {
        return this.defHandler(e, false);
    }

    @ExceptionHandler({TokenException.class})
    public ResultData tokenException(TokenException e) {
        return this.defHandler(e, 409);
    }

    @ExceptionHandler({RuntimeException.class})
    public BaseResultData fullException(RuntimeException e) {
        return this.defHandler(e);
    }

    @ExceptionHandler({JsonProcessingException.class})
    public BaseResultData JsonException(RuntimeException e) {
        return this.defHandler(e);
    }


    private BaseResultData defHandler(RuntimeException e) {
        return defHandler(e, true);
    }

    private BaseResultData defHandler(RuntimeException e, boolean writeLog) {
        BaseResultData res = new BaseResultData();
        res.setErrcode(1);
        res.setErrmsg(e.getMessage());
        res.setDetail_msg(ExceptionUtil.stacktraceToString(e));
        e.printStackTrace();
        if (writeLog) {
            log.error(res.getDetail_msg(), e);
        }
        return res;
    }
    private ResultData<String> defHandler(TokenException e, int errcode) {
        ResultData res = new ResultData();
        res.setErrcode(errcode);
        res.setErrmsg("刷新token");

        User user = userService.getUserByLoginName(e.getName());

        if (user == null) {
            throw new MessageException("用户名不存在!");
        }
        if (user.getIsEnabled() != 1) {
            throw new MessageException("用户已被禁用,请联系管理员!");
        }
        LoginUser u = new LoginUser();
        u.setId(user.getId());
        u.setLoginName(user.getLoginName());
        ObjectMapper objectMapper = new ObjectMapper();
        String json = null;

        try {
            json = objectMapper.writeValueAsString(u);
        } catch (JsonProcessingException exp) {
            throw new MessageException("密码错误!");
        }
        // JWT
        String token = JWTUtil.CreateToken(e.getName(), json, 2 * 60L * 60L * 1000L);
        res.setDetail_msg("");
        res.setData(token);
        return res;
    }
}
