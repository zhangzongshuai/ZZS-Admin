package com.zzs.zzsadmin.common.vo;


import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class ResultDataList<T> extends BaseResultData {

    public ResultDataList()
    {
        super();
        this.datas = new ArrayList<>();
    }
    public ResultDataList(List<T> list){
        super();
        this.datas = list;
    }
    private List<T> datas;

}
