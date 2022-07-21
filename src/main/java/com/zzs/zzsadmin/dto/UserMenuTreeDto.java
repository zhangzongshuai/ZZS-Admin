package com.zzs.zzsadmin.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;


@Getter
@Setter
public class UserMenuTreeDto {
    public UserMenuTreeDto(){
        children = new ArrayList<>();
    }

    private String id;

    private String title;

    private String url;

    private List<UserMenuTreeDto> children;
}
