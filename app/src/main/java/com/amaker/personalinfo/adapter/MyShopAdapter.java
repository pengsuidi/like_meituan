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
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.amaker.personalinfo.Activities.NewUploadFoodMenuActivity;
import com.amaker.personalinfo.Activities.ShopActivity;

import com.amaker.personalinfo.R;
import com.amaker.personalinfo.entity.Shop_Info;
import com.amaker.personalinfo.util.CommonUtil;
import com.amaker.personalinfo.util.Config;

import java.util.List;

public class MyShopAdapter extends RecyclerView.Adapter<MyShopAdapter.MyShopsViewHolder> {

    private List<Shop_Info> datas;

    private Typeface typeface;


    public MyShopAdapter(List<Shop_Info> datas, Typeface typeface) {
        this.datas = datas;

        this.typeface=typeface;
    }

    /**
     * 加载 ViewHolder 对象
     */
    @NonNull
    @Override
    public MyShopsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        //此处的 布局文件.xml 是每条数据的展示效果布局样式图
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_myshop, parent, false);

        return new MyShopsViewHolder(view);
    }

    /**
     * TODO 绑定数据及事件
     */

    public void onBindViewHolder(@NonNull final MyShopsViewHolder holder, int position) {
        final Shop_Info shopInfo =
                datas.get(position);
        Bitmap bitmap=getBitmapFromByte(shopInfo.getShop_image());
        bitmap = Bitmap.createScaledBitmap(bitmap,240,240, true);
        bitmap= CommonUtil.getRoundedCornerBitmap(bitmap,20);
        holder.MyShop_img.setImageBitmap(bitmap);
        holder.MyShopName.setText(shopInfo.getShop_name());
        holder.MyShopName.setTypeface(typeface);
        holder.MyShopName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(view.getContext(), ShopActivity.class);
                intent.putExtra(Config.REQUEST_PARAMETER_SHOP_ID,shopInfo.getShop_id().toString());
                view.getContext().startActivity(intent);
            }
        });
        holder.MyShop_img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(view.getContext(), ShopActivity.class);
                intent.putExtra(Config.REQUEST_PARAMETER_SHOP_ID,shopInfo.getShop_id().toString());
                view.getContext().startActivity(intent);
            }
        });
        holder.to_upload_food_menu_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(view.getContext(), NewUploadFoodMenuActivity.class);
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
    class MyShopsViewHolder extends RecyclerView.ViewHolder {

        private TextView MyShopName;
        private ImageView MyShop_img;
        private Button to_upload_food_menu_btn;
        public MyShopsViewHolder(@NonNull View itemView) {
            super(itemView);
            MyShopName = itemView.findViewById(R.id.MyShopName);
            to_upload_food_menu_btn = itemView.findViewById(R.id.to_upload_food_menu_btn);
            MyShop_img = itemView.findViewById(R.id.MyShop_img);




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
