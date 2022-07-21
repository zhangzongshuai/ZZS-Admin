package com.zzs.zzsadmin.dto;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class ModifyPwd {
    private String userId;
    private String oldPassword;
    private String newPassword;
    private String confirmPassword;
}
