package com.zzs.zzsadmin.vo.Role;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * <p>用户分配的角色信息</p>
 *
 * @author 张宗帅
 * @date 2022-07-22 16:24:18
 */
@Data
public class UserRoleVo {

    @ApiModelProperty(value = "角色id")
    private String id;
    /**
     * 角色名称
     */
    @ApiModelProperty(value = "角色名称")
    private String name;

    /**
     * 是否分配
     */
    @ApiModelProperty(value = "是否分配")
    private int isAssign;
}
