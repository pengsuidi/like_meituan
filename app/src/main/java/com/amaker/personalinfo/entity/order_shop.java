package com.amaker.personalinfo.entity;

public class order_shop {
    private String shop_id;
    private String shop_name;
    private String total_price;
    private int oid;//订单号
    private String food_name;
    private String food_price;
    private String user_id;

    public int getOid() {
        return oid;
    }

    public void setOid(int oid) {
        this.oid = oid;
    }

    public String getFood_name() {
        return food_name;
    }

    public void setFood_name(String food_name) {
        this.food_name = food_name;
    }

    public String getFood_price() {
        return food_price;
    }

    public void setFood_price(String food_price) {
        this.food_price = food_price;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public order_shop(String shop_id, String shop_name, String total_price, int oid, String food_name, String food_price, String user_id) {
        this.shop_id = shop_id;
        this.shop_name = shop_name;
        this.total_price = total_price;
        this.oid = oid;
        this.food_name = food_name;
        this.food_price = food_price;
        this.user_id = user_id;
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

    public String getTotal_price() {
        return total_price;
    }

    public void setTotal_price(String total_price) {
        this.total_price = total_price;
    }

    public order_shop(String shop_id, String shop_name, String total_price) {
        this.shop_id = shop_id;
        this.shop_name = shop_name;
        this.total_price = total_price;
    }
    public order_shop() {

    }

}
