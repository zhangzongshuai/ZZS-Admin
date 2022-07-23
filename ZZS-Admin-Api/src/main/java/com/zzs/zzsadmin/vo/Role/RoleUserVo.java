package com.zzs.zzsadmin.vo.Role;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * <p>角色分配的用户信息</p>
 *
 * @author 张宗帅
 * @date 2022-07-22 13:43:00
 */
@Data
public class RoleUserVo {

    @ApiModelProperty(value = "id")
    private String id;
    /**
     * 名称
     */
    @ApiModelProperty(value = "名称")
    private String name;
    /**
     * 登录名
     */
    @ApiModelProperty(value = "登录名")
    private String loginName;

    /**
     * 是否绑定
     */
    @ApiModelProperty(value = "是否绑定")
    private int isAssign;
}
