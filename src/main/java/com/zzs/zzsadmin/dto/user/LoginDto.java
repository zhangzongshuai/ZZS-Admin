package com.zzs.zzsadmin.dto.user;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * <p></p>
 *
 * @author 张宗帅
 * @date 2022-06-30 11:27:03
 */
@Data
public class LoginDto {

    @ApiModelProperty(value = "登录名")
    private String loginName;

    @ApiModelProperty(value = "密码")
    private String password;

    @ApiModelProperty(hidden = true)
//    @JsonIgnore
    private String ip;

}
