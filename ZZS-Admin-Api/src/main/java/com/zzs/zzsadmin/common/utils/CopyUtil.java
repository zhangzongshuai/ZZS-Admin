package com.zzs.zzsadmin.common.utils;

import cn.hutool.core.util.ReflectUtil;
import org.springframework.beans.BeanUtils;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

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


    public static <T> T entityToVo(Object source, Class<T> target) {
        if (source == null) {
            return null;
        }
        T targetObject = null;
        try {
            targetObject = target.newInstance();
            BeanUtils.copyProperties(source, targetObject);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return targetObject;
    }



    public static <T> List<T> entityToVoList(Collection<?> sourceList, Class<T> target) {
        if (sourceList == null) {
            return null;
        }
        List<T> targetList = new ArrayList<>(sourceList.size());

        try {
            for (Object source : sourceList) {
                T targetObject = target.newInstance();
                BeanUtils.copyProperties(source, targetObject);
                targetList.add(targetObject);
            }
        } catch (Exception e) {
            // TODO
            e.printStackTrace();
//            logger.error("convert error ", e);
        }
        return targetList;
    }


    public static <T> void entityToVoList(Collection<?> sourceList, List<T> targetList) {
        if (sourceList == null) {
            targetList = null;
        }
        targetList = new ArrayList<>(sourceList.size());

        try {
            for (Object source : sourceList) {
                T targetObject = (T) targetList.get(0).getClass().newInstance();
                BeanUtils.copyProperties(source, targetObject);
                targetList.add(targetObject);
            }
        } catch (Exception e) {
            // TODO
            e.printStackTrace();
//            logger.error("convert error ", e);
        }
    }

}
