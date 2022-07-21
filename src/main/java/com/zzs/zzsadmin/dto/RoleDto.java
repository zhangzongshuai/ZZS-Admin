package com.zzs.zzsadmin.dto;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class RoleDto {

    private String id;
    /**
     * name
     */
    private String name;
    /**
     * description
     */
    private String description;
    /**
     * is_enabled
     */
    private Integer isEnabled;

    private int isAssign;
}
