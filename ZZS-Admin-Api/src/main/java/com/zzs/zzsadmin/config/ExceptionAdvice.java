package com.zzs.zzsadmin.config;


import cn.hutool.core.exceptions.ExceptionUtil;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.zzs.zzsadmin.common.exception.ArgumentException;
import com.zzs.zzsadmin.common.exception.MessageException;
import com.zzs.zzsadmin.common.vo.BaseResultData;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
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
}
