package com.zzs.zzsadmin.dto;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MenuDto {

    /**
     * id
     */
    @TableId(value = "id",type = IdType.INPUT)
    private String id;
    /**
     * name
     */
    private String name;
    /**
     * parent_id
     */
    private String parentId;

    private String type;

    private String permissionCode;

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
