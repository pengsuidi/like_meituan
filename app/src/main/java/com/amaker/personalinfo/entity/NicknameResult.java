package com.amaker.personalinfo.entity;

public class NicknameResult {
    //内部逻辑代码
    private int code;
    private String message;
    private Object data;

    public NicknameResult(int code, String message, Object data) {
        this.code = code;
        this.message = message;
        this.data = data;
    }

    public NicknameResult(int code, String message) {
        this.code = code;
        this.message = message;
    }

    public NicknameResult() {
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }

    @Override
    public String toString() {
        return //"Result{" +
                //"code=" + code +
                "当前昵称：" + message;
    }
}
