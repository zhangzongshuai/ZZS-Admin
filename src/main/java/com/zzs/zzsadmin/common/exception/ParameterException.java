package com.zzs.zzsadmin.common.exception;

public class ParameterException extends RuntimeException {

    private String paramName;

    private String message;

    public ParameterException() {
        super();
    }

    public ParameterException(String paramName,String message){
        super();
        this.paramName = paramName;
        this.message= message;
    }

    public String getParamName(){return this.paramName;}
    public void setParamName(String paramName){this.paramName = paramName;}
    public String getMessage(){return this.message;}
    public void setMessage(String message){this.message = message;}

}
