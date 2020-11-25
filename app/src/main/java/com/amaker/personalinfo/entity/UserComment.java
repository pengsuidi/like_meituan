package com.amaker.personalinfo.entity;

public class UserComment {
    private String shop_id;
    private byte[] comment_img;
    private String comment_img_addr;
    private String new_comment;
    private  String user_id;
    private  String grade;
    private  String user_pic_addr;
    private  byte[] user_pic;

    public UserComment(String shop_id, byte[] comment_img, String comment_img_addr, String new_comment, String user_id, String grade, String user_pic_addr, byte[] user_pic) {
        this.shop_id = shop_id;
        this.comment_img = comment_img;
        this.comment_img_addr = comment_img_addr;
        this.new_comment = new_comment;
        this.user_id = user_id;
        this.grade = grade;
        this.user_pic_addr = user_pic_addr;
        this.user_pic = user_pic;
    }

    public String getShop_id() {
        return shop_id;
    }

    public void setShop_id(String shop_id) {
        this.shop_id = shop_id;
    }

    public byte[] getComment_img() {
        return comment_img;
    }

    public void setComment_img(byte[] comment_img) {
        this.comment_img = comment_img;
    }

    public String getComment_img_addr() {
        return comment_img_addr;
    }

    public void setComment_img_addr(String comment_img_addr) {
        this.comment_img_addr = comment_img_addr;
    }

    public String getNew_comment() {
        return new_comment;
    }

    public void setNew_comment(String new_comment) {
        this.new_comment = new_comment;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public String getGrade() {
        return grade;
    }

    public void setGrade(String grade) {
        this.grade = grade;
    }

    public String getUser_pic_addr() {
        return user_pic_addr;
    }

    public void setUser_pic_addr(String user_pic_addr) {
        this.user_pic_addr = user_pic_addr;
    }

    public byte[] getUser_pic() {
        return user_pic;
    }

    public void setUser_pic(byte[] user_pic) {
        this.user_pic = user_pic;
    }

    public UserComment() {

    }
}
