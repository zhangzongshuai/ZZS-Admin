package com.zzs.zzsadmin.dto;


import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class userinfo {
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
