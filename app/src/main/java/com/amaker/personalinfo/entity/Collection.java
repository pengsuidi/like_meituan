package com.amaker.personalinfo.entity;

import android.content.Intent;

public class Collection {

    private Integer uid;
    private Integer shop_id;
    private String shop_name;
    private String shop_type;

    public void setUid(Integer i){
        uid=i;
    }
    public void setShopId(Integer i){
        this.shop_id=i;
    }
    public void setShopName(String s){
        this.shop_name=s;
    }

    public void setShop_type(String shop_type) {
        this.shop_type = shop_type;
    }

    public Integer getUid(){return this.uid;}
    public Integer getShopId(){return this.shop_id;}
    public String getShopName(){return shop_name;}

    public String getShop_type() {
        return shop_type;
    }

    @Override
    public String toString() {
        return "Favorite Shop{" +
                "shop_id=" + shop_id +
                ", shop_name='" + shop_name + '\'' +
                ", shop_type='" + shop_type + '\'' +
                '}';
    }


}
