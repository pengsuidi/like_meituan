package com.amaker.personalinfo.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.provider.MediaStore;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.RecyclerView;

import com.alibaba.fastjson.JSONObject;
import com.amaker.personalinfo.Activities.UploadFoodMenuActivity;
import com.amaker.personalinfo.R;
import com.amaker.personalinfo.entity.Food_Menu;
import com.amaker.personalinfo.entity.Result;
import com.amaker.personalinfo.util.Config;
import com.amaker.personalinfo.util.OkHttpUtil;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class UploadFoodMenuAdapter extends RecyclerView.Adapter<UploadFoodMenuAdapter.FoodsViewHolder> {
    private SharedPreferences sharedPreferences;
    private List<Food_Menu> datas;
    private Context context;
    private List<Food_Menu> foodMenuList;
    private Handler handler;

    private boolean isAdd=false;
    private String image64;
    public UploadFoodMenuAdapter(Context context,Handler handler, List<Food_Menu> foodMenuList) {
        this.context = context;
        this.foodMenuList=foodMenuList;
        this.handler=handler;

    }



    /**
     * 加载 ViewHolder 对象
     */
    @NonNull
    @Override
    public FoodsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        //此处的 布局文件.xml 是每条数据的展示效果布局样式图
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_upload_food_menu, parent, false);
        return new FoodsViewHolder(view);
    }

    /**
     * TODO 绑定数据及事件
     */
    @Override
    public void onBindViewHolder(@NonNull final FoodsViewHolder holder, final int position) {
        final Food_Menu food_menu = foodMenuList.get(position);
        System.out.println("Adapter:food_id:"+food_menu.getFood_id());
        holder.food_price.setText(String.valueOf(food_menu.getFood_id()));

        System.out.println("------item_name:"+food_menu.getFood_name());
        if(food_menu.getUri()!=null)
        {
            holder.to_choose_FoodImg.setImageURI(Uri.parse(food_menu.getUri()));
        }
        else {
            System.out.println("uri为空!");
        }
        holder.to_choose_FoodImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_PICK, null);
                intent.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*");
                ((Activity)context).startActivityForResult(intent, food_menu.getFood_id());
                //把需要修改图片的食物Id发送给Activity

            }
        });
        //将所填信息封装为food_menu以便传递给UploadFoodMenuActivity
            //封装

            //传递

        holder.save_FoodMenu.setOnClickListener(new View.OnClickListener() {
            boolean flag=true;//当flag为true说明图标为"√",可以修改.为false则不能修改,为"修改"图标
            public void onClick(View view) {
                if(flag)
                {
                    food_menu.setFood_type(holder.food_type.getText().toString());
                    food_menu.setFood_price(holder.food_price.getText().toString());
                    food_menu.setFood_name(holder.food_name.getText().toString());
                    food_menu.setFood_description(holder.food_description.getText().toString());
                    Bundle bundle = new Bundle();
                    ArrayList arr;//用来保存上传食物的数据
                    arr = new ArrayList();
                    arr.add(food_menu);
                    bundle.putStringArrayList(Config.FOOD_MENU_INFO, arr);
                    Message message = new Message();
                    message.what = Config.Upload_food_menu;
                    message.setData(bundle);
                    handler.sendMessage(message);
                    //更换"√"图标为"修改"图表
                    holder.save_FoodMenu.setImageResource(R.drawable.update);
                    holder.linearLayout.setClickable(false);
                    flag=false;
                }
                else

                {
                    holder.linearLayout.setClickable(true);
                    holder.save_FoodMenu.setImageResource(R.drawable.save_foodmenu);
                    flag=true;
                }

            }
        });
        holder.delete_FoodMenu.setOnClickListener(new View.OnClickListener() {//点击删除该食物
            @Override
            public void onClick(View view) {
                //发送消息给Activity.删除食物
                Bundle b=new Bundle();
                b.putInt(Config.DELETE_FOOD_ID,foodMenuList.get(position).getFood_id());
                Message message = new Message();
                message.what = Config.Delete_food_menu;
                message.setData(b);
                handler.sendMessage(message);
            }
        });
    }



    /**
     * 告知 RecyclerView 子项（item） 的数量
     */
    @Override
    public int getItemCount() {
        return foodMenuList == null ? 0 : foodMenuList.size();
    }

    /**
     * ViewHolder 对象，是 RecyclerView 中对 ITEM 循环利用的一个机制
     */
    class FoodsViewHolder extends RecyclerView.ViewHolder {

        private EditText food_name, food_description, food_price, food_type;
        private ImageButton to_choose_FoodImg,save_FoodMenu,delete_FoodMenu;
        private LinearLayout linearLayout;
        public FoodsViewHolder(@NonNull View itemView) {
            super(itemView);
            food_type = itemView.findViewById(R.id.food_type);
            linearLayout = itemView.findViewById(R.id.linearlayout);
            save_FoodMenu = itemView.findViewById(R.id.save_FoodMenu);
            delete_FoodMenu = itemView.findViewById(R.id.delete_FoodMenu);
            food_name = itemView.findViewById(R.id.food_name);
            food_description = itemView.findViewById(R.id.food_description);
            food_price = itemView.findViewById(R.id.food_price);
            to_choose_FoodImg = itemView.findViewById(R.id.to_choose_FoodImg);
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
