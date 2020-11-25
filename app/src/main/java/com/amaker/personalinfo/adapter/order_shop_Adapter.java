package com.amaker.personalinfo.adapter;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.amaker.personalinfo.Activities.ShopActivity;
import com.amaker.personalinfo.Activities.user_comment_Activity;
import com.amaker.personalinfo.R;
import com.amaker.personalinfo.entity.Shop_Info;
import com.amaker.personalinfo.entity.order_shop;
import com.amaker.personalinfo.util.CommonUtil;
import com.amaker.personalinfo.util.Config;

import java.util.ArrayList;
import java.util.List;

public class order_shop_Adapter extends RecyclerView.Adapter<order_shop_Adapter.orderviewHolder> {
    private SharedPreferences sharedPreferences;

    private Context context;
    private List<order_shop> datas = new ArrayList<>();
    private List<Shop_Info> shop_info_buyList = new ArrayList<>();
    private RecyclerView.Adapter adapter;
    private List<order_shop> Alldatas=new ArrayList<>();
    public order_shop_Adapter(List<order_shop> datas, SharedPreferences sharedPreferences, Context context, List<Shop_Info> shop_info_buyList, List<order_shop> Alldatas) {
        this.datas = datas;//datas包含所有的该用户的购买数据
        this.context = context;
        this.sharedPreferences = sharedPreferences;
        this.shop_info_buyList=shop_info_buyList;
        this.Alldatas=Alldatas;
    }

    /**
     * 加载 ViewHolder 对象
     *
     * @return
     */
    @NonNull
    @Override
    public orderviewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        //此处的 布局文件.xml 是每条数据的展示效果布局样式图
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_order, parent, false);
        return new orderviewHolder(view);
    }


    /**
     * TODO 绑定数据及事件
     */
    @Override
    public void onBindViewHolder(@NonNull final orderviewHolder holder, int position) {
        final order_shop order_shop = datas.get(position);

        for (int i = 0; i < shop_info_buyList.size(); i++) {
            if (order_shop.getShop_name()!=null&&order_shop.getShop_name().contentEquals(shop_info_buyList.get(i).getShop_name())) {
                Bitmap tmp=getBitmapFromByte(shop_info_buyList.get(i).getShop_image());
                tmp=Bitmap.createScaledBitmap(tmp,450,450,true);
                tmp= CommonUtil.getRoundedCornerBitmap(tmp,20);
                holder.shop_img.setImageBitmap(tmp);
                break;
            }
        }

        holder.shop_name.setText(order_shop.getShop_name());

        holder.oid.setText("订单号:" + order_shop.getOid());
        holder.total_price.setText("¥ " + order_shop.getTotal_price());

        holder.to_comment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(view.getContext(), user_comment_Activity.class);
                intent.putExtra(Config.REQUEST_PARAMETER_SHOP_ID, order_shop.getShop_id());
                System.out.println("--------shop_name:" + order_shop.getShop_name());
                view.getContext().startActivity(intent);
            }
        });
        holder.shop_name.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(view.getContext(), ShopActivity.class);
                intent.putExtra(Config.REQUEST_PARAMETER_SHOP_ID, order_shop.getShop_id());
                view.getContext().startActivity(intent);
            }
        });
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(context, RecyclerView.VERTICAL, false);
//        linearLayoutManager.setAutoMeasureEnabled(true);
        holder.child_recyclerView.setLayoutManager(linearLayoutManager);

        List<order_shop> orderShopList = new ArrayList<>();//用以从所有数据即Alldatas中储存该oid(订单号)的食物数据
        for (int i = 0; i < Alldatas.size(); i++)
        {
            if(Alldatas.get(i).getOid()==order_shop.getOid())
            {
                orderShopList.add(Alldatas.get(i));
                System.out.println("大小:"+orderShopList.size());
            }
        }
        adapter=new HistoryFoodOrdersAdapter(orderShopList);
        if(holder.child_recyclerView.getAdapter()==null)
        {
            holder.child_recyclerView.setAdapter(adapter);
        }



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
    class orderviewHolder extends RecyclerView.ViewHolder {
        private Button to_comment,shop_name;
        private ImageButton shop_img;
        private TextView  total_price, oid;
        private RecyclerView child_recyclerView;

        public orderviewHolder(@NonNull View itemView) {
            super(itemView);
            to_comment = itemView.findViewById(R.id.to_comment);
            oid = itemView.findViewById(R.id.oid);

            shop_name = itemView.findViewById(R.id.shop_name);
            shop_img = itemView.findViewById(R.id.shop_img);
            total_price = itemView.findViewById(R.id.total_price);
            child_recyclerView = itemView.findViewById(R.id.child_recyclerview);
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
