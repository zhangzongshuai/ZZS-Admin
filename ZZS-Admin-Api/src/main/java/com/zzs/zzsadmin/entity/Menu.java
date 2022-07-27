package com.zzs.zzsadmin.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@EqualsAndHashCode(callSuper = false)
@TableName("core_menu")
public class Menu extends BaseModel {

    /**
     * id
     */
    @TableId(value = "id", type = IdType.INPUT)
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
     * url
     */
    private String url;

    private String type;

    private String permissionCode;

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
