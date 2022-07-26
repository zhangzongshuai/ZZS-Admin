package com.zzs.zzsadmin.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;


/**
 * (Icon)实体类
 *
 * @author zzs
 * @since 2022-07-26 10:04:30
 */
@Data
@TableName("core_icon")
public class Icon {
    private String id;

    private String iconCode;
}

