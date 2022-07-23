package com.zzs.zzsadmin.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RoleUserDto {
    private String id;
    /**
     *
     */
    private String name;
    /**
     *
     */
    private String loginName;

    private int isAssign;
}
