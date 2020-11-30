package com.amaker.personalinfo.adapter;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;


import com.amaker.personalinfo.Activities.ShopActivity;
import com.amaker.personalinfo.R;
import com.amaker.personalinfo.entity.Shop_Info;
import com.amaker.personalinfo.util.CommonUtil;
import com.amaker.personalinfo.util.Config;

import java.util.List;

public class FavouriteAdapter extends RecyclerView.Adapter<FavouriteAdapter.ShopsViewHolder> {

    private List<Shop_Info> datas;



    public FavouriteAdapter(List<Shop_Info> datas) {
        this.datas = datas;

    }

    /**
     * 加载 ViewHolder 对象
     */
    @NonNull
    @Override
    public ShopsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        //此处的 布局文件.xml 是每条数据的展示效果布局样式图
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_favourite_shop, parent, false);

        return new ShopsViewHolder(view);
    }

    /**
     * TODO 绑定数据及事件
     */

    public void onBindViewHolder(@NonNull final ShopsViewHolder holder, int position) {
        final Shop_Info shopInfo =
                datas.get(position);
        Bitmap bitmap=getBitmapFromByte(shopInfo.getShop_image());
        bitmap = Bitmap.createScaledBitmap(bitmap,360,330, true);
        bitmap= CommonUtil.getRoundedCornerBitmap(bitmap,20);
        holder.shop_img.setImageBitmap(bitmap);
        holder.shop_name.setText(shopInfo.getShop_name());
        holder.ratingbar.setRating(Float.parseFloat(shopInfo.getGrade()));
        holder.shop_type.setText(shopInfo.getShop_type());
        holder.to_shop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(view.getContext(), ShopActivity.class);
                intent.putExtra(Config.REQUEST_PARAMETER_SHOP_ID,shopInfo.getShop_id().toString());
                view.getContext().startActivity(intent);
            }
        });


    }

    /**
     * 告知 RecyclerView 子项（item） 的数量
     */
    @Override
    public int getItemCount() {
        return datas == null ? 0 : datas.size();
    }

    /**
     * ViewHolder 对象，是 RecyclerView 中对 ITEM 循环利用的一个机制
     */
    class ShopsViewHolder extends RecyclerView.ViewHolder {
        private RatingBar ratingbar;
        private TextView shop_name,shop_type;
        private ImageView shop_img;
        private ImageButton to_shop;
        public ShopsViewHolder(@NonNull View itemView) {
            super(itemView);
            shop_name = itemView.findViewById(R.id.shop_name);
            ratingbar = itemView.findViewById(R.id.ratingbar);
            shop_type = itemView.findViewById(R.id.shop_type);
            to_shop = itemView.findViewById(R.id.to_shop);
            shop_img = itemView.findViewById(R.id.shop_img);




        }
    }
    public Bitmap getBitmapFromByte(byte[] temp){   //将二进制转化为bitmap
        if(temp != null){
            Bitmap bitmap = BitmapFactory.decodeByteArray(temp, 0, temp.length);
            return bitmap;
        }else{
            return null;
        }
    }

}
