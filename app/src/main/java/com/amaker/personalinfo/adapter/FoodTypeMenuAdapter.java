package com.amaker.personalinfo.adapter;

import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.RecyclerView;

import com.alibaba.fastjson.JSONObject;
import com.amaker.personalinfo.R;
import com.amaker.personalinfo.entity.Food_Menu;
import com.amaker.personalinfo.entity.Result;
import com.amaker.personalinfo.util.Config;
import com.amaker.personalinfo.util.OkHttpUtil;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class FoodTypeMenuAdapter extends RecyclerView.Adapter<FoodTypeMenuAdapter.FoodTypesViewHolder> {


    private List<String> FoodTypes=new ArrayList<>();
    private Handler mHandler;
    public FoodTypeMenuAdapter(List<String> FoodTypes,Handler mHandler) {
        this.mHandler = mHandler;
        this.FoodTypes=FoodTypes;
    }

    /**
     * 加载 ViewHolder 对象
     */
    @NonNull
    @Override
    public FoodTypesViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        //此处的 布局文件.xml 是每条数据的展示效果布局样式图
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_food_type, parent, false);
        return new FoodTypesViewHolder(view);
    }

    /**
     * TODO 绑定数据及事件
     */
    @Override
    public void onBindViewHolder(@NonNull final FoodTypesViewHolder holder, int position) {
        final String  foodtype = FoodTypes.get(position);
        //   SharedPreferences.Editor editor= getSharedPreferences("data",MODE_PRIVATE).edit();

        holder.food_type_name.setText(foodtype);

        holder.food_type_name.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                view.setFocusable(true);
                view.requestFocus();
                view.requestFocusFromTouch();
                Message msg = mHandler.obtainMessage();
                msg.what = Config.STATUS_OK;
                Bundle bundle=new Bundle();
                bundle.putString(Config.FOOD_TYPE,foodtype);
                msg.setData(bundle);
                mHandler.sendMessage(msg);
            }
        });


    }

    /**
     * 告知 RecyclerView 子项（item） 的数量
     */
    @Override
    public int getItemCount() {
        return FoodTypes == null ? 0 : FoodTypes.size();
    }

    /**
     * ViewHolder 对象，是 RecyclerView 中对 ITEM 循环利用的一个机制
     */
    class FoodTypesViewHolder extends RecyclerView.ViewHolder {
        private Button food_type_name;


        public FoodTypesViewHolder(@NonNull View itemView) {
            super(itemView);
            food_type_name = itemView.findViewById(R.id.food_type_name);

        }
    }

    public Bitmap getBitmapFromByte(byte[] temp) {   //将二进制转化为bitmap
        if (temp != null) {
            Bitmap bitmap = BitmapFactory.decodeByteArray(temp, 0, temp.length);
            return bitmap;
        } else {
            return null;
        }
    }

}
