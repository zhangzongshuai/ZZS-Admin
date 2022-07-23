package com.zzs.zzsadmin.common.vo;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class ResultData<T> extends BaseResultData {

    public ResultData(){
        super();
    }
    public ResultData(T t){
        super();
        data = t;
    }

    private T data;
}
