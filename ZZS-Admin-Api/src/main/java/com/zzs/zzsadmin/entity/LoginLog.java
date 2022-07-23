package com.zzs.zzsadmin.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

/**
 * <p></p>
 *
 * @author 张宗帅
 * @since
 * @date 2021-01-22
 */
@Data
@TableName("core_login_log")
public class LoginLog{

    /**
    *
    */
    @TableId(value = "id", type = IdType.INPUT)
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
    private Date inTime;

    /**
     *
     */
    private Date outTime;

}
