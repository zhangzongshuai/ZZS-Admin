package com.zzs.zzsadmin.vo.log;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.zzs.zzsadmin.common.utils.DateTimeSerializer;
import lombok.Data;

import java.util.Date;

/**
 * <p></p>
 *
 * @author 张宗帅
 * @date 2022-07-23 21:38:11
 */
@Data
public class LoginLogVo {
    private String id;

    /**
     *
     */
    private String clientIp;

    /**
     *
     */
    private String operatorId;

    /**
     *
     */
    private String operatorLoginName;

    /**
     *
     */
    private String operator;

    /**
     *
     */
    @JsonSerialize(using = DateTimeSerializer.class)
    private Date inTime;

    /**
     *
     */
    @JsonSerialize(using = DateTimeSerializer.class)
    private Date outTime;
}
