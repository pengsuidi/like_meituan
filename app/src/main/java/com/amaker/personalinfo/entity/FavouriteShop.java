package com.amaker.personalinfo.entity;

public class FavouriteShop {
    private String user_id;
    private String shop_id;
    private String shop_name;
    public FavouriteShop(String user_id, String shop_id, String shop_name, String shop_type) {
        this.user_id = user_id;
        this.shop_id = shop_id;
        this.shop_name = shop_name;
    }



    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
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

    public FavouriteShop(String user_id, String shop_id, String shop_name) {
        this.user_id = user_id;
        this.shop_id = shop_id;
        this.shop_name = shop_name;
    }
    public FavouriteShop() {
    }
}
