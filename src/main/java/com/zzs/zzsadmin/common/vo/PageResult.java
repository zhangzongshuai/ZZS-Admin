package com.zzs.zzsadmin.common.vo;

import com.baomidou.mybatisplus.core.metadata.IPage;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class PageResult<T> extends ResultDataList<T> {

    public PageResult(){

    }
    public PageResult(IPage page){
        this.setDatas(page.getRecords());
        this.totalCount = page.getTotal();
        this.pages = page.getPages();
        this.pageNum = page.getCurrent();
        this.pageSize = page.getSize();
    }
    public PageResult(IPage page,Boolean isSetDatas){
        this.totalCount = page.getTotal();
        this.pages = page.getPages();
        this.pageNum = page.getCurrent();
        this.pageSize = page.getSize();
        if (isSetDatas){
            this.setDatas(page.getRecords());
        }
    }

    private long pageNum;

    private long pageSize;

    private long pages;

    private long totalCount;
}
