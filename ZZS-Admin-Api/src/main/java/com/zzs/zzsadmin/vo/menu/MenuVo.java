package com.zzs.zzsadmin.vo.menu;


import lombok.Data;

/**
 * <p></p>
 *
 * @author 张宗帅
 * @date 2022-07-23 14:17:51
 */
@Data
public class MenuVo {

    /**
     * id
     */
    private String id;
    /**
     * name
     */
    private String name;
    /**
     * parent_id
     */
    private String parentId;
    /**
     * is_menu
     */
    private Integer isMenu;

    /**
     * url
     */
    private String url;


    private String iconCode;
    /**
     * order_id
     */
    private Integer orderId;
    /**
     * is_enabled
     */
    private Integer isEnabled;
}
