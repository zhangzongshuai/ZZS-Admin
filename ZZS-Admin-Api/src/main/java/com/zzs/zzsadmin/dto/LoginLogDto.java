package com.zzs.zzsadmin.dto;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.zzs.zzsadmin.common.utils.DateTimeSerializer;
import lombok.Data;

import java.util.Date;

@Data
public class LoginLogDto {
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
