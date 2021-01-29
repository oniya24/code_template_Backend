package com.code_template.util;

public enum ResponseCode {
    // 200
    OK(200,null),
    // 300

    // 400

    // 500
    SERVER_ERROR(500,"服务器故障"),

    // 600 用户相关
    USER_NOT_EXIST(600,"用户不存在"),
    USERNAME_REGISTERED(601, "用户名被注册"),
    MOBILE_REGISTERED(602,"号码已被注册"),
    PASSWORD_ERROR(603,"密码错误,请重新输入"),
    TOKEN_NOT_EXIST(604,"Token不存在"),
    TOKEN_EXPIRE(605,"Token已经过期")
    ;

    private int code;
    private String message;
    ResponseCode(int code, String message) {
        this.code = code;
        this.message = message;
    }

    public int getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }
}
