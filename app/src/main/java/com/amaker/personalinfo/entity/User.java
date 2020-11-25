package com.amaker.personalinfo.entity;

import java.sql.Date;

public class User {
    private Integer uid;
    private String user_img_addr;
    private byte[] user_img;
    private String uname;

    private String unickname;

    private String usex;

    private String upassword;

    private String ubirthday;

    private String uphone;

    private String uemail;

    private String ucity;

    private String uqqnum;
    private String pay_password;
    private Integer ustate;

    private Date createtime;

    private String headimg;

    private Integer urole;

    public Integer getUrole() {
        return urole;
    }

    public void setUrole(Integer urole) {
        this.urole = urole;
    }

    public String getUser_img_addr() {
        return user_img_addr;
    }

    public void setUser_img_addr(String user_img_addr) {
        this.user_img_addr = user_img_addr;
    }

    public byte[] getUser_img() {
        return user_img;
    }

    public void setUser_img(byte[] user_img) {
        this.user_img = user_img;
    }

    public User(Integer uid, String user_img_addr, byte[] user_img, String uname, String unickname, String usex, String upassword, String ubirthday, String uphone, String uemail, String ucity, String uqqnum, String pay_password, Integer ustate, Date createtime, String headimg, Integer urole) {
        this.uid = uid;
        this.user_img_addr = user_img_addr;
        this.user_img = user_img;
        this.uname = uname;
        this.unickname = unickname;
        this.usex = usex;
        this.upassword = upassword;
        this.ubirthday = ubirthday;
        this.uphone = uphone;
        this.uemail = uemail;
        this.ucity = ucity;
        this.uqqnum = uqqnum;
        this.pay_password = pay_password;
        this.ustate = ustate;
        this.createtime = createtime;
        this.headimg = headimg;
        this.urole = urole;
    }

    public String getPay_password() {
        return pay_password;
    }

    public void setPay_password(String pay_password) {
        this.pay_password = pay_password;
    }

    public User(Integer uid, String uname, String unickname, String usex, String upassword, String ubirthday, String uphone, String uemail, String ucity, String uqqnum, String pay_password, Integer ustate, Date createtime, String headimg, Integer urole) {
        this.uid = uid;
        this.uname = uname;
        this.unickname = unickname;
        this.usex = usex;
        this.upassword = upassword;
        this.ubirthday = ubirthday;
        this.uphone = uphone;
        this.uemail = uemail;
        this.ucity = ucity;
        this.uqqnum = uqqnum;
        this.pay_password = pay_password;
        this.ustate = ustate;
        this.createtime = createtime;
        this.headimg = headimg;
        this.urole = urole;
    }

    public User() {
    }

    public User(String uname, String upassword) {
        this.uname = uname;
        this.upassword = upassword;
    }

    public User(Integer uid, String uname, String upassword) {
        this.uid = uid;
        this.uname = uname;
        this.upassword = upassword;
    }

    public Integer getUid() {
        return uid;
    }

    public void setUid(Integer uid) {
        this.uid = uid;
    }

    public String getUname() {
        return uname;
    }

    public void setUname(String uname) {
        this.uname = uname == null ? null : uname.trim();
    }

    public String getUnickname() {
        return unickname;
    }

    public void setUnickname(String unickname) {
        this.unickname = unickname == null ? null : unickname.trim();
    }

    public String getUsex() {
        return usex;
    }

    public void setUsex(String usex) {
        this.usex = usex == null ? null : usex.trim();
    }

    public String getUpassword() {
        return upassword;
    }

    public void setUpassword(String upassword) {
        this.upassword = upassword == null ? null : upassword.trim();
    }

    public String getUbirthday() {
        return ubirthday;
    }

    public void setUbirthday(String ubirthday) {
        this.ubirthday = ubirthday;
    }

    public String getUphone() {
        return uphone;
    }

    public void setUphone(String uphone) {
        this.uphone = uphone == null ? null : uphone.trim();
    }

    public String getUemail() {
        return uemail;
    }

    public void setUemail(String uemail) {
        this.uemail = uemail == null ? null : uemail.trim();
    }

    public String getUcity() {
        return ucity;
    }

    public void setUcity(String ucity) {
        this.ucity = ucity == null ? null : ucity.trim();
    }

    public String getUqqnum() {
        return uqqnum;
    }

    public void setUqqnum(String uqqnum) {
        this.uqqnum = uqqnum == null ? null : uqqnum.trim();
    }

    public Integer getUstate() {
        return ustate;
    }

    public void setUstate(Integer ustate) {
        this.ustate = ustate;
    }

    public Date getCreatetime() {
        return createtime;
    }

    public void setCreatetime(Date createtime) {
        this.createtime = createtime;
    }

    public String getHeadimg() {
        return headimg;
    }

    public void setHeadimg(String headimg) {
        this.headimg = headimg == null ? null : headimg.trim();
    }

    @Override
    public String toString() {
        return "User{" +
                "uid=" + uid +
                ", uname='" + uname + '\'' +
                ", unickname='" + unickname + '\'' +
                ", usex='" + usex + '\'' +
                ", upassword='" + upassword + '\'' +
                ", ubirthday=" + ubirthday +
                ", uphone='" + uphone + '\'' +
                ", uemail='" + uemail + '\'' +
                ", ucity='" + ucity + '\'' +
                ", uqqnum='" + uqqnum + '\'' +
                ", ustate=" + ustate +
                ", createtime=" + createtime +
                ", headimg='" + headimg + '\'' +
                '}';
    }
}
