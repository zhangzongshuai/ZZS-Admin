package com.zzs.zzsadmin.dto.user;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * <p></p>
 *
 * @author 张宗帅
 * @date 2022-07-01 17:09:24
 */
@Data
public class ModifyPasswordDto {

    @ApiModelProperty(value = "用户id")
    private String userId;

    @ApiModelProperty(value = "旧密码")
    private String oldPassword;

    @ApiModelProperty(value = "新密码")
    private String newPassword;

    @ApiModelProperty(value = "确认密码")
    private String confirmPassword;
}
