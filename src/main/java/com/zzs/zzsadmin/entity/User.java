package com.zzs.zzsadmin.entity;

/**
 * <p></p>
 *
 * @author zzs
 * @date 2020-10-29 19:04:27
 * @since
 */

import com.baomidou.mybatisplus.annotation.*;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
@EqualsAndHashCode(callSuper = false)
@TableName("core_user")
public class User extends BaseModel {

    /**
     *
     */
    @TableId(value = "id", type = IdType.INPUT)
    private String id;

    /**
     *
     */
    private String name;

    /**
     *
     */
    private String loginName;

    /**
     *
     */
    private String password;

    /**
     *
     */
    private Integer sex;

    /**
     *
     */
    private String phoneNum;

    /**
     *
     */
    private String email;



    private int isEnabled;

    @TableField(fill = FieldFill.INSERT)
    private int isDeleted;

}

