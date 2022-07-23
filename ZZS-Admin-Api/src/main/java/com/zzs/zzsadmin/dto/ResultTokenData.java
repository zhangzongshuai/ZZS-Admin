package com.zzs.zzsadmin.dto;

import com.zzs.zzsadmin.common.vo.BaseResultData;
import lombok.Data;

@Data
public class ResultTokenData extends BaseResultData {

    private String token;

    private String userId;

    private String userName;

    private String loginName;
}
