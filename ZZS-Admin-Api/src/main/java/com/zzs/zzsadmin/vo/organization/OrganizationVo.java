package com.zzs.zzsadmin.vo.organization;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;

/**
 * <p></p>
 *
 * @author zzs
 * @date 2022-09-20 22:15:43
 */
@Data
public class OrganizationVo {
    /**
     * id
     */
    private String id;
    /**
     * 名称
     */
    private String name;
    /**
     * 父id
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
     * 排序
     */
    private Integer orderId;
    /**
     * 是否启用
     */
    private Integer isEnabled;
}
