package com.zzs.zzsadmin.vo.user;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;

/**
 * <p></p>
 *
 * @author 张宗帅
 * @date 2022-06-29 14:22:58
 */
@Data
public class UserVo {
    /**
     *
     */
    @TableId(value = "id", type = IdType.INPUT)
    private String id;

    /**
     *
     */
    private String name;

    /**
     *
     */
    private String loginName;


    /**
     *
     */
    private Integer sex;

    /**
     *
     */
    private String phoneNum;

    /**
     *
     */
    private String email;



    private int isEnabled;
}
