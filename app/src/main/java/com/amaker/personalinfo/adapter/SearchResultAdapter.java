package com.amaker.personalinfo.adapter;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
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
import com.amaker.personalinfo.Activities.ShopActivity;
import com.amaker.personalinfo.R;
import com.amaker.personalinfo.entity.Food_Menu;
import com.amaker.personalinfo.entity.Result;
import com.amaker.personalinfo.entity.Shop_Info;
import com.amaker.personalinfo.util.Config;
import com.amaker.personalinfo.util.OkHttpUtil;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SearchResultAdapter extends RecyclerView.Adapter<SearchResultAdapter.ResultsViewHolder> {
    private List<Shop_Info> datas;
    public SearchResultAdapter(List<Shop_Info> datas) {
        this.datas = datas;
    }
    /**
     * 加载 ViewHolder 对象
     */
    @NonNull
    @Override
    public ResultsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        //此处的 布局文件.xml 是每条数据的展示效果布局样式图
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_seach_res, parent, false);
        return new ResultsViewHolder(view);
    }

    /**
     * TODO 绑定数据及事件
     */
    @Override
    public void onBindViewHolder(@NonNull final ResultsViewHolder holder, int position) {
        final Shop_Info shop_info = datas.get(position);
        //   SharedPreferences.Editor editor= getSharedPreferences("data",MODE_PRIVATE).edit();
        holder.res.setText(shop_info.getShop_name());
        holder.res.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(v.getContext(), ShopActivity.class);
                intent.putExtra(Config.REQUEST_PARAMETER_SHOP_ID,shop_info.getShop_id().toString());
                v.getContext().startActivity(intent);
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
    class ResultsViewHolder extends RecyclerView.ViewHolder {
        private Button res;


        public ResultsViewHolder(@NonNull View itemView) {
            super(itemView);
            res = itemView.findViewById(R.id.search_item);


        }
    }


}
