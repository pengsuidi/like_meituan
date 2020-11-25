package com.amaker.personalinfo.adapter;

import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.amaker.personalinfo.R;
import com.amaker.personalinfo.entity.Food_Menu;
import com.amaker.personalinfo.entity.ShoppingcarFood;
import com.amaker.personalinfo.util.CommonUtil;

import java.util.List;

public class ShoppingcarFoodAdapter extends RecyclerView.Adapter<ShoppingcarFoodAdapter.MoviesViewHolder> {
    private SharedPreferences sharedPreferences;

    private List<Food_Menu> datas;
    private Handler menuHandler;
    public ShoppingcarFoodAdapter(List<Food_Menu> datas) {
        this.datas = datas;

    }

    /**
     * 加载 ViewHolder 对象
     */
    @NonNull
    @Override
    public MoviesViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        //此处的 布局文件.xml 是每条数据的展示效果布局样式图
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_shoppingcar_food, parent, false);
        return new MoviesViewHolder(view);
    }

    /**
     * TODO 绑定数据及事件
     */
    @Override
    public void onBindViewHolder(@NonNull final MoviesViewHolder holder, int position) {
        final Food_Menu food = datas.get(position);
        System.out.println("ShoppingcarFoodAdapter:食物数量:"+datas.size());
        Bitmap bitmap= CommonUtil.getBitmapFromByte(food.getFood_image());
        bitmap=Bitmap.createScaledBitmap(bitmap,240,240,true);
        bitmap=CommonUtil.getRoundedCornerBitmap(bitmap,10);
        holder.bought_food_name.setText(food.getFood_name());
        holder.bought_food_image.setImageBitmap(bitmap);
        holder.bought_food_price.setText(food.getFood_price());
        holder.food_count.setText("X"+food.getCount());
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
    class MoviesViewHolder extends RecyclerView.ViewHolder {

        private ImageView bought_food_image;
        private TextView bought_food_name, bought_food_price,food_count;
        public MoviesViewHolder(@NonNull View itemView) {
            super(itemView);
            bought_food_image = itemView.findViewById(R.id.bought_food_image);
            bought_food_name = itemView.findViewById(R.id.bought_food_name);
            bought_food_price = itemView.findViewById(R.id.bought_food_price);
            food_count = itemView.findViewById(R.id.food_count);


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
