package com.zzs.zzsadmin.vo.log;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.zzs.zzsadmin.common.utils.DateTimeSerializer;
import lombok.Data;

import java.util.Date;

/**
 * <p></p>
 *
 * @author 张宗帅
 * @date 2022-07-23 21:51:13
 */
@Data
public class AccessLogVo {
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
