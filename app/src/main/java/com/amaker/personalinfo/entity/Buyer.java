package com.amaker.personalinfo.entity;

public class Buyer {
    private String user_id;
    private String pay_password;

    public Buyer(String user_id, String pay_password) {
        this.user_id = user_id;
        this.pay_password = pay_password;
    }
    public Buyer() {

    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public String getPay_password() {
        return pay_password;
    }

    public void setPay_password(String pay_password) {
        this.pay_password = pay_password;
    }
}
