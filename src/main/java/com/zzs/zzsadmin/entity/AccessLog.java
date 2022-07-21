package com.zzs.zzsadmin.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.util.Date;

/**
 * <p></p>
 *
 * @author 张宗帅
 * @since
 * @date 2021-01-20
 */
@Data
@TableName("core_access_log")
public class AccessLog {

    /**
    *
    */
    @TableId(value = "id", type = IdType.INPUT)
    private String id;

    private String clientIp;
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
