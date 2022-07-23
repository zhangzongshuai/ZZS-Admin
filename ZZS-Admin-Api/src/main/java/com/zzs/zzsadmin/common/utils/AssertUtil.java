package com.zzs.zzsadmin.common.utils;

import com.zzs.zzsadmin.common.exception.ArgumentException;
import org.apache.commons.lang3.StringUtils;

public class AssertUtil {
    public AssertUtil() {
    }

    public static final void valid(String param) {
        if (StringUtils.isBlank(param)){
            throw new ArgumentException("id","不可为空");
        }
    }

    public static final void valid(Object param,String paramName,String message) {
        if ( param == null || StringUtils.isBlank(param.toString())){
            throw new ArgumentException(paramName,message);
        }
    }
}
