package com.amaker.personalinfo.entity;

public class Shop_Info {
    private String user_id;
    private String shop_image_addr;
    private Integer shop_id;
    private String shop_name;
    private String shop_type;
    private String grade;
    private byte[] shop_image;

    public String getShop_type() {
        return shop_type;
    }

    public void setShop_type(String shop_type) {
        this.shop_type = shop_type;
    }

    public String getGrade() {
        return grade;
    }

    public void setGrade(String grade) {
        this.grade = grade;
    }

    public Shop_Info(String user_id, String shop_image_addr, Integer shop_id, String shop_name, String shop_type, String grade, byte[] shop_image) {
        this.user_id = user_id;
        this.shop_image_addr = shop_image_addr;
        this.shop_id = shop_id;
        this.shop_name = shop_name;
        this.shop_type = shop_type;
        this.grade = grade;
        this.shop_image = shop_image;
    }

    public Shop_Info(String user_id, String shop_image_addr, Integer shop_id, String shop_name, byte[] shop_image) {
        this.user_id = user_id;
        this.shop_image_addr = shop_image_addr;
        this.shop_id = shop_id;
        this.shop_name = shop_name;
        this.shop_image = shop_image;
    }

    public Shop_Info() {
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public String getShop_image_addr() {
        return shop_image_addr;
    }

    public void setShop_image_addr(String shop_image_addr) {
        this.shop_image_addr = shop_image_addr;
    }

    public Integer getShop_id() {
        return shop_id;
    }

    public void setShop_id(Integer shop_id) {
        this.shop_id = shop_id;
    }

    public String getShop_name() {
        return shop_name;
    }

    public void setShop_name(String shop_name) {
        this.shop_name = shop_name;
    }

    public byte[] getShop_image() {
        return shop_image;
    }

    public void setShop_image(byte[] shop_image) {
        this.shop_image = shop_image;
    }
}
