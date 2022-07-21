package com.zzs.zzsadmin.entity;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;


@Setter
@Getter
public class BaseModel {

    private String creator;

    @TableField(fill = FieldFill.INSERT)
    private Date createTime;

    private String modifier;

    @TableField(fill = FieldFill.UPDATE)
    private Date modifyTime;
}
