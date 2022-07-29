package com.zzs.zzsadmin.common.exception;

import lombok.Data;

/**
 * <p></p>
 *
 * @author 张宗帅
 * @date 2022-07-29 14:42:04
 */
@Data
public class TokenException extends RuntimeException {
    private String message;

    private String name;

    public TokenException(){
        super();
    }

    public TokenException (String mes){
        super(mes);
        this.message = mes;
    }
    public TokenException (String mes,String nm){
        super(mes);
        this.message = mes;
        this.name = nm;
    }
}
