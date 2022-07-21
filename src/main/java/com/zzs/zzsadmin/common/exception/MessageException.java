package com.zzs.zzsadmin.common.exception;

public class MessageException extends RuntimeException {
    private String message;

    public MessageException(){
        super();
    }

    public MessageException (String mes){
        super(mes);
        this.message = mes;
    }
}
