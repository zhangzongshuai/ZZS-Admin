package com.zzs.zzsadmin.dto;

import lombok.Data;

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
