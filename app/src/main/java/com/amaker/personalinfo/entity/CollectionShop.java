package com.amaker.personalinfo.entity;

public class CollectionShop {

    private String uid;
    private String shop_id;
    private String shop_name;
    private String shop_type;

    public CollectionShop() {
    }

    public CollectionShop(String uid, String shop_id, String shop_name, String shop_type) {
        this.uid = uid;
        this.shop_id = shop_id;
        this.shop_name = shop_name;
        this.shop_type = shop_type;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getShop_id() {
        return shop_id;
    }

    public void setShop_id(String shop_id) {
        this.shop_id = shop_id;
    }

    public String getShop_name() {
        return shop_name;
    }

    public void setShop_name(String shop_name) {
        this.shop_name = shop_name;
    }

    public void setShopName(String s){
        this.shop_name=s;
    }

    public void setShop_type(String shop_type) {
        this.shop_type = shop_type;
    }

    public String getShopName(){return shop_name;}

    public String getShop_type() {
        return shop_type;
    }

    @Override
    public String toString() {
        return "Favorite Shop{" +
                "shopid=" + shop_id +
                ", shopname='" + shop_name + '\'' +
                ", shoptype='" + shop_type + '\'' +
                '}';
    }

}
