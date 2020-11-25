package com.amaker.personalinfo.entity;

public class BirthdayResult {
    //内部逻辑代码
    private int code;
    private String message;
    private Object data;

    public BirthdayResult(int code, String message, Object data) {
        this.code = code;
        this.message = message;
        this.data = data;
    }

    public BirthdayResult(int code, String message) {
        this.code = code;
        this.message = message;
    }

    public BirthdayResult() {
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
                "当前生日：" + message;
    }
}
