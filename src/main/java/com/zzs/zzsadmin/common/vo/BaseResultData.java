package com.zzs.zzsadmin.common.vo;

import lombok.Getter;
import lombok.Setter;
import org.apache.commons.lang3.StringUtils;

@Getter
@Setter
public class BaseResultData {

    public BaseResultData(){}

    public BaseResultData(String errmsg){

        if (StringUtils.isNotBlank(errmsg)){
            this.errcode = 1;
            this.errmsg = errmsg;
        }
    }
    public BaseResultData(String errmsg,String errDetail){

        if (StringUtils.isNotBlank(errmsg)){
            this.errcode = 1;
            this.errmsg = errmsg;
        }
        if (StringUtils.isNotBlank(errDetail)){
            this.errcode = 1;
            this.detail_msg = errDetail;
        }
    }


    private int errcode;

    private String errmsg;

    private String detail_msg;

}

