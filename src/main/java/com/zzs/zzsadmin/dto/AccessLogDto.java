package com.zzs.zzsadmin.dto;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.zzs.zzsadmin.common.utils.DateTimeSerializer;
import lombok.Data;


import java.util.Date;

@Data
public class AccessLogDto {
    private String id;

    /**
     *
     */
    private String requestUrl;

    /**
     *
     */
    private String operateModule;

    /**
     *
     */
    @JsonSerialize(using = DateTimeSerializer.class)
    private Date operateTime;

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
}
