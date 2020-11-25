package com.amaker.personalinfo.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.TextView;

import com.amaker.personalinfo.Activities.ShopActivity;
import com.amaker.personalinfo.R;
import com.amaker.personalinfo.entity.Shop_Info;
import com.amaker.personalinfo.util.CommonUtil;
import com.amaker.personalinfo.util.Config;

import java.util.ArrayList;
import java.util.List;

public class MainShopListAdapter extends BaseAdapter {

    private List<Shop_Info> ShopInfoList;
    private LayoutInflater layoutInflater;
    private Context context;

    public MainShopListAdapter(Context context,List<Shop_Info> ShopInfoList) {
        System.out.println("!!!size:"+ShopInfoList.size());
        this.ShopInfoList=ShopInfoList;
        this.context=context;

    }


    @Override
    public int getCount() {
        System.out.println("123size:"+ShopInfoList.size());
        return ShopInfoList.size();
    }

    @Override
    public Object getItem(int position) {
        return ShopInfoList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        layoutInflater = LayoutInflater.from(context);
        ViewHolder holder = null;
        final Shop_Info shop_info=ShopInfoList.get(position);
        if (convertView == null) {
            convertView = layoutInflater.inflate(R.layout.main_shop_item, null);
            holder = new ViewHolder();
            holder.main_shop_name =  convertView.findViewById(R.id.main_shop_name);
            holder.main_shop_image = convertView.findViewById(R.id.main_shop_image);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        holder.main_shop_name.setText(shop_info.getShop_name());
        Bitmap bitmap=CommonUtil.getBitmapFromByte(shop_info.getShop_image());
        bitmap=bitmap.createScaledBitmap(bitmap,600,360,true);
        bitmap=CommonUtil.getRoundedCornerBitmap(bitmap,10);
        holder.main_shop_image.setImageBitmap(bitmap);
        holder.main_shop_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(v.getContext(), ShopActivity.class);
                intent.putExtra(Config.REQUEST_PARAMETER_SHOP_ID,shop_info.getShop_id().toString());
                System.out.println("myshoplist:shopid:"+shop_info.getShop_id().toString());
                v.getContext().startActivity(intent);
            }
        });
        return convertView;

    }
    class ViewHolder {
        ImageButton main_shop_image;
        TextView main_shop_name;
    }
}