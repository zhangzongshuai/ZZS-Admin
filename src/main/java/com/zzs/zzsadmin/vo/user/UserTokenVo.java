package com.zzs.zzsadmin.vo.user;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * <p>用户token</p>
 *
 * @author 张宗帅
 * @date 2022-06-30 10:46:33
 */
@Data
public class UserTokenVo {
    /**
     * token
     */
    @ApiModelProperty(value = "token")
    private String token;

    /**
     * 用户id
     */
    @ApiModelProperty(value = "用户id")
    private String userId;

    /**
     * 用户姓名
     */
    @ApiModelProperty(value = "用户姓名")
    private String userName;

    /**
     * 账户
     */
    @ApiModelProperty(value = "账户")
    private String loginName;
}
