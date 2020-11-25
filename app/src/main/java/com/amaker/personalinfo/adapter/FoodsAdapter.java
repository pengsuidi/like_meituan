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

public class FoodsAdapter extends RecyclerView.Adapter<FoodsAdapter.FoodsViewHolder> {
    private SharedPreferences sharedPreferences;

    private Handler handler = new Handler(Looper.myLooper()) {
        @RequiresApi(api = Build.VERSION_CODES.O)
        @Override

        public void handleMessage(Message msg) {
            switch (msg.what) {
                case Config.STATUS_OK://网络请求成功，但响应需要根据实际来操作
                    Result result = JSONObject.parseObject(msg.obj.toString(), Result.class);
                    JSONObject object = (JSONObject) result.getData();

                    break;
                default://登陆失败
                    break;
            }

        }
    };
    private List<Food_Menu> datas;
//    private Handler menuHandler;
    private String shop_name;
    private int update_food_id=-1;
    private Handler getOrderedFoodHandler;
    public FoodsAdapter(List<Food_Menu> datas, SharedPreferences sharedPreferences,String shop_name,Handler getOrderedFoodHandler) {
        this.datas = datas;
        this.shop_name=shop_name;
        this.sharedPreferences=sharedPreferences;
        this.getOrderedFoodHandler=getOrderedFoodHandler;
    }

    /**
     * 加载 ViewHolder 对象
     */
    @NonNull
    @Override
    public FoodsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        //此处的 布局文件.xml 是每条数据的展示效果布局样式图
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_food, parent, false);
        return new FoodsViewHolder(view);
    }

    /**
     * TODO 绑定数据及事件
     */
    @Override
    public void onBindViewHolder(@NonNull final FoodsViewHolder holder, int position) {
        final Food_Menu food = datas.get(position);
     //   SharedPreferences.Editor editor= getSharedPreferences("data",MODE_PRIVATE).edit();

        holder.food_img.setImageBitmap(getBitmapFromByte(food.getFood_image()));
        holder.food_name.setText(food.getFood_name());
        holder.food_txt.setText(food.getFood_description());
        holder.food_price.setText(food.getFood_price()+"元");
        holder.add_food.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                holder.amount++;
                holder.buy_amount.setText("已购买:"+holder.amount);
                holder.minus_food.setVisibility(View.VISIBLE);
                //向menuFragment发送点击购买了的食物id

                Message message = new Message();
                message.what = Config.ADD_FOOD;
                message.obj=food.getFood_name();
                getOrderedFoodHandler.sendMessage(message);

            }
        });
        holder.minus_food.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                holder.amount--;
                holder.buy_amount.setText("已购买:"+holder.amount);

                if (holder.amount==0)
                {
                    holder.minus_food.setVisibility(View.INVISIBLE);
                }

                //向menuFragment发送点击购买了的食物id

                Message message = new Message();
                message.what = Config.DEL_FOOD;
                message.obj=food.getFood_name();
                getOrderedFoodHandler.sendMessage(message);

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
    class FoodsViewHolder extends RecyclerView.ViewHolder {
        private int amount=0;
        private ImageView food_img;
        private TextView food_name, food_txt, food_price, buy_amount;
        private ImageButton add_food, minus_food;

        public FoodsViewHolder(@NonNull View itemView) {
            super(itemView);
            minus_food = itemView.findViewById(R.id.minus_food);
            food_img = itemView.findViewById(R.id.food_img);
            food_name = itemView.findViewById(R.id.food_name);
            food_txt = itemView.findViewById(R.id.food_txt);
            food_price = itemView.findViewById(R.id.food_price);
            add_food = itemView.findViewById(R.id.add_food);
            buy_amount = itemView.findViewById(R.id.buy_amount);

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
