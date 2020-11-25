package com.amaker.personalinfo.entity;

public class ShoppingcarFood {
    private String food_name;
    private String food_price;
    private String shop_name;
    private String user_id;

    public String getShop_name() {
        return shop_name;
    }

    public void setShop_name(String shop_name) {
        this.shop_name = shop_name;
    }

    public ShoppingcarFood(String food_name, String food_price, String shop_name, String user_id) {
        this.food_name = food_name;
        this.food_price = food_price;
        this.shop_name = shop_name;
        this.user_id = user_id;
    }

    public ShoppingcarFood() {

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
}
