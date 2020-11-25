package com.amaker.personalinfo.entity;

public class Shop {
    private int shop_id;
    private String shop_name;
    private String shop_introduction;
    private int shop_imageId;

    public Shop(int id,String shop_name,String shop_introduction,int shop_imageId){
        this.shop_id=id;
        this.shop_imageId=shop_imageId;
        this.shop_introduction=shop_introduction;
        this.shop_name=shop_name;
    }

    public int getShop_id(){return shop_id;}

    public String getShop_name(){return shop_name;}

    public String getShop_introduction(){return shop_introduction;}

    public int getShop_imageId(){return shop_imageId;}
}
