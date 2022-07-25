package com.zzs.zzsadmin.dto.user;

import lombok.Data;

/**
 * <p></p>
 *
 * @author 张宗帅
 * @date 2022-07-01 17:27:49
 */
@Data
public class UserDto {

    /**
     *
     */
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
}
