package com.zzs.zzsadmin.common.utils;

import cn.hutool.core.util.ReflectUtil;

import java.lang.reflect.Field;

public class CopyUtil {
    /**
     * 复制对象
     *
     * @param fromObj 元对象
     * @param toObj   目标对象
     */
    public static void CobyTo(Object fromObj, Object toObj) {

        if (fromObj == null) {
            toObj = null;
        } else {
            Field[] fields = ReflectUtil.getFields(fromObj.getClass());
            Field[] fields1 = ReflectUtil.getFields(toObj.getClass());

            for (Field field : fields
            ) {
                Object fieldValue = ReflectUtil.getFieldValue(fromObj, field);
                Field toField = ReflectUtil.getField(toObj.getClass(), field.getName());
                if (toField != null) {
                    ReflectUtil.setFieldValue(toObj, toField, fieldValue);
                }
            }
        }

    }


}
