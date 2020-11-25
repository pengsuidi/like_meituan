package com.amaker.personalinfo.entity;

public class SellerInfo {
    private String shop_id;
    private String user_id;
    private String sell_amount;

    public SellerInfo(String shop_id, String user_id, String sell_amount) {
        this.shop_id = shop_id;
        this.user_id = user_id;
        this.sell_amount = sell_amount;
    }

    public SellerInfo() {
    }

    public String getShop_id() {
        return shop_id;
    }

    public void setShop_id(String shop_id) {
        this.shop_id = shop_id;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public String getSell_amount() {
        return sell_amount;
    }

    public void setSell_amount(String sell_amount) {
        this.sell_amount = sell_amount;
    }
}
