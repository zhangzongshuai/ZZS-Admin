package com.zzs.zzsadmin.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Setter
@Getter
public class MenuTreeDto {
    public MenuTreeDto(){
        children = new ArrayList<>();
    }

    private String id;

    private String label;

    private List<MenuTreeDto> children;
}
