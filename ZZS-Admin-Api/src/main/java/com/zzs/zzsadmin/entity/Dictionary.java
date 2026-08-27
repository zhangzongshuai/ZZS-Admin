package com.zzs.zzsadmin.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.util.Date;
import java.io.Serializable;

/**
 * (Dictionary)实体类
 *
 * @author makejava
 * @since 2022-12-22 14:00:18
 */
@Data
@TableName("core_dictionary")
public class Dictionary implements Serializable {
    private static final long serialVersionUID = -16821470491756994L;

    @TableId(value = "id", type = IdType.INPUT)
    private String id;

    private String name;

    private String code;

    private Integer isEnabled;

    private String remark;

    private String creator;

    private Date createTime;

    private String modifier;

    private Date modifyTime;

    private Integer isDeleted;

}

