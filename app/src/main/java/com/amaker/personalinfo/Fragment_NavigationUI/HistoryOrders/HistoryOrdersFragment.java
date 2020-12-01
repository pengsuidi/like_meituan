package com.amaker.personalinfo.Fragment_NavigationUI.HistoryOrders;

import android.content.Context;
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

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

import com.amaker.personalinfo.R;
import com.amaker.personalinfo.adapter.order_shop_Adapter;
import com.amaker.personalinfo.entity.Result;


import com.amaker.personalinfo.entity.Shop_Info;
import com.amaker.personalinfo.entity.order_shop;
import com.amaker.personalinfo.util.Config;
import com.amaker.personalinfo.util.OkHttpUtil;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import pl.droidsonroids.gif.GifImageView;


public class HistoryOrdersFragment extends Fragment {
    private RecyclerView.Adapter adapter;
    SharedPreferences sharedPreferences;
    private RecyclerView recyclerView;
    private List<order_shop> datas = new ArrayList<>();
    private List<order_shop> Alldatas = new ArrayList<>();//储存该用户在payment中的每一行数据
    private int inoid=-1;
    private GifImageView gifView;
    private List<Shop_Info> shop_info_buyList=new ArrayList<>();

    private Handler handler = new Handler(Looper.myLooper()) {
        @RequiresApi(api = Build.VERSION_CODES.O)
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case Config.STATUS_OK://网络请求成功，但响应需要根据实际来操作
                    Result result = JSONObject.parseObject(msg.obj.toString(), Result.class);
                    System.out.println("result:---" + result);
                    if (result.getCode() == Config.STATUS_SUCCESS) {
                        //网络请求成功
                        JSONArray jsonArray = (JSONArray) result.getData();
                        List<order_shop> order_shops = jsonArray.toJavaList(order_shop.class);
//                        System.out.println("个数:" + order_shops.size());
//                        System.out.println("--------inoid:"+inoid);
                        datas.clear();
                        Alldatas.clear();
                        Alldatas.addAll(order_shops);

                        for (int i = 0; i < order_shops.size(); i++) {
                            if(order_shops.get(i).getOid()!=inoid)
                            {
                                inoid=order_shops.get(i).getOid();
                                datas.add(order_shops.get(i));//datas为每个订单只有一行数据
                                //根据ShopId获取其图片
                                Map<String, Object> param = new HashMap<>();
                                param.put(Config.REQUEST_PARAMETER_SHOP_ID,order_shops.get(i).getShop_id());
                                System.out.println("shopid:--"+order_shops.get(i).getShop_id());

                                OkHttpUtil.post(Config.URL_GetShopInfoByShopidServlet, param, GetShopInfoHandler);
                            }

                        }
                        // adapter.notifyDataSetChanged();

                        break;
                    }
                default://网络请求失败

                    break;
            }
        }
    };

    private Handler GetShopInfoHandler = new Handler(Looper.myLooper()) {
        @RequiresApi(api = Build.VERSION_CODES.O)
        @Override

        public void handleMessage(Message msg) {
            switch (msg.what) {
                case Config.STATUS_OK://网络请求成功，但响应需要根据实际来操作
                    Result result = JSONObject.parseObject(msg.obj.toString(), Result.class);
                    JSONObject object = (JSONObject) result.getData();
                    Shop_Info shop_info = object.toJavaObject(Shop_Info.class);
                    shop_info_buyList.add(shop_info);
                    if(shop_info_buyList.size()==datas.size())
                        adapter.notifyDataSetChanged();
                    gifView.setVisibility(View.INVISIBLE);
                    break;
                default:
                    break;
            }

        }
    };
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.activity_order,container,false);
        recyclerView =view.findViewById(R.id.recycler_orders);
        sharedPreferences=getContext().getSharedPreferences("data", Context.MODE_PRIVATE);

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
        layoutManager.setAutoMeasureEnabled(true);
        recyclerView.setLayoutManager(layoutManager);
        adapter = new order_shop_Adapter(datas,sharedPreferences, getActivity(),shop_info_buyList,Alldatas);
        recyclerView.setAdapter(adapter);
        new Thread(new Runnable() {
            @Override
            public void run() {
                Map<String, Object> params = new HashMap<>();
                params.put(Config.REQUEST_PARAMETER_USER_ID, sharedPreferences.getString(Config.REQUEST_PARAMETER_USER_ID,null));//userid暂定为1
                System.out.println("userid:"+sharedPreferences.getString(Config.REQUEST_PARAMETER_USER_ID,null));
                OkHttpUtil.post(Config.URL_GET_ORDERS, params, handler);
            }
        }).start();
        //设置动画
        gifView =  view.findViewById(R.id.gif_view);
        gifView.setImageResource(R.drawable.loading);
        gifView.setStateListAnimator(null);
        gifView.setVisibility(View.VISIBLE);

        return view;
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