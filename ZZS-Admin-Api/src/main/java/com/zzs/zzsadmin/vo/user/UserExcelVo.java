package com.zzs.zzsadmin.vo.user;

import lombok.Data;

/**
 * <p></p>
 *
 * @author 张宗帅
 * @date 2022-07-28 14:32:34
 */
@Data
public class UserExcelVo {


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
    private String sex;

    /**
     *
     */
    private String phoneNum;

    /**
     *
     */
    private String email;



//    private int isEnabled;

    public void setSex(Integer _sex) {
        if (_sex == 1){
            this.sex = "男";
        }
        else{
            this.sex = "女";
        }

    }
}
