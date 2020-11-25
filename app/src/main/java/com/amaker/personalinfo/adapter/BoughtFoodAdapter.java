package com.amaker.personalinfo.adapter;

import android.os.Build;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.RecyclerView;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.amaker.personalinfo.R;
import com.amaker.personalinfo.entity.Result;
import com.amaker.personalinfo.entity.order_shop;
import com.amaker.personalinfo.util.Config;

import java.util.List;

public class BoughtFoodAdapter extends RecyclerView.Adapter<BoughtFoodAdapter.BoughtFoodViewHolder> {

    private List<order_shop> datas;

    private Handler handler = new Handler(Looper.myLooper()) {
        @RequiresApi(api = Build.VERSION_CODES.O)
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case Config.STATUS_OK://网络请求成功，但响应需要根据实际来操作
                    Result result = JSONObject.parseObject(msg.obj.toString(), Result.class);
                    JSONArray jsonArray = (JSONArray) result.getData();
                    List<order_shop> orders = jsonArray.toJavaList(order_shop.class);
                    System.out.println("个数:" + orders.size());
                    datas.clear();
                    datas.addAll(orders);

                    break;
                default://登陆失败
                    break;
            }

        }
    };

    public BoughtFoodAdapter(List<order_shop> datas ) {
        this.datas = datas;

    }

    /**
     * 加载 ViewHolder 对象
     */
    @NonNull
    @Override
    public BoughtFoodViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        //此处的 布局文件.xml 是每条数据的展示效果布局样式图
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_bought_food, null, false);
        return new BoughtFoodViewHolder(view);
    }

    /**
     * TODO 绑定数据及事件
     */
    @Override
    public void onBindViewHolder(@NonNull final BoughtFoodViewHolder holder, int position) {
        final order_shop orders = datas.get(position);
        holder.food_price.setText(orders.getFood_price());
        System.out.println("user_id:"+orders.getUser_id());
        holder.food_name.setText(orders.getFood_name());

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
    class BoughtFoodViewHolder extends RecyclerView.ViewHolder {

        private TextView food_name, food_price;


        public BoughtFoodViewHolder(@NonNull View itemView) {
            super(itemView);
            food_name = itemView.findViewById(R.id.food_name);
            food_price = itemView.findViewById(R.id.food_price);


        }
    }


}
