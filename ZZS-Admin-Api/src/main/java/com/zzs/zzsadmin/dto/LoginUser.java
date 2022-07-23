package com.zzs.zzsadmin.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class LoginUser {
    private String id;

    private String loginName;

    private String userRoles;

    private String userMenus;
}
