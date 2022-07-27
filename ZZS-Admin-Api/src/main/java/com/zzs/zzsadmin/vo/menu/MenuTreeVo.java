package com.zzs.zzsadmin.vo.menu;


import lombok.Data;

import java.util.ArrayList;
import java.util.List;

/**
 * <p></p>
 *
 * @author 张宗帅
 * @date 2022-07-26 16:54:03
 */
@Data
public class MenuTreeVo {

    public MenuTreeVo() {
        children = new ArrayList<>();
    }

    private String id;

    private String parentId;

    private String name;

    private String url;

    private String type;

    private String permissionCode;

    private String iconCode;

    private String orderId;

    private List<MenuTreeVo> children;
}
