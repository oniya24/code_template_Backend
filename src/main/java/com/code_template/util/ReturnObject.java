package com.code_template.util;

public class ReturnObject<T> {
    private T data = null;
    private String errMsg = null;
    private int code = ResponseCode.OK.getCode();

    public ReturnObject(T data) {
        this.data = data;
    }

    public ReturnObject(ResponseCode code) {
        this.errMsg = code.getMessage();
        this.code = code.getCode();
    }
    public ReturnObject(ResponseCode code, T data) {
        this.errMsg = code.getMessage();
        this.code = code.getCode();
        this.data = data;
    }

    public ReturnObject(String errMsg, int code) {
        this.errMsg = errMsg;
        this.code = code;
    }

    public T getData() {
        return data;
    }

    public String getErrMsg() {
        return this.errMsg;
    }

    public int getCode() {
        return code;
    }
}
