package com.zzs.zzsadmin.common.exception;

public class ArgumentException extends RuntimeException {

    private String message;

    public ArgumentException() {
        super();
    }

    /**
     *
     * @param paramName 参数名
     * @param message 错误信息
     */
    public ArgumentException(String paramName,String message){
        super();
        this.message = message + "参数名:" + paramName;
    }

    @Override
    public String getMessage(){return this.message;}
    public void setMessage(String message){this.message = message;}

}
