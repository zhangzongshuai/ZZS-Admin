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
@TableName("core_organization")
/**
 * <p></p>
 *
 * @author 张宗帅
 * @date 2022-09-20 21:26:03
 */
public class Organization extends BaseModel {
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
     * 编码
     */
    private String code;
    /**
     * 级别
     */
    private String level;
    /**
     * order_id
     */
    private Integer orderId;
    /**
     * is_enabled
     */
    private Integer isEnabled;
}
