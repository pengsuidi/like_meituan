package com.amaker.personalinfo.Activities;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.amaker.personalinfo.Fragment.menuFragment;
import com.amaker.personalinfo.R;
import com.amaker.personalinfo.adapter.FavouriteAdapter;
import com.amaker.personalinfo.entity.FavouriteShop;
import com.amaker.personalinfo.entity.Food_Menu;
import com.amaker.personalinfo.entity.Result;
import com.amaker.personalinfo.entity.Shop_Info;
import com.amaker.personalinfo.util.CommonUtil;
import com.amaker.personalinfo.util.Config;
import com.amaker.personalinfo.util.OkHttpUtil;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.amaker.personalinfo.util.CommonUtil.getBitmapFromByte;

public class FavouriteActivity extends AppCompatActivity {
    private RecyclerView.Adapter adapter;
    private RecyclerView recyclerView;
    private Toolbar toolbar;
    private List<Shop_Info> datas= new ArrayList<>();
    private int count=0;
    private Handler handler = new Handler(Looper.myLooper()) {
        @RequiresApi(api = Build.VERSION_CODES.O)
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case Config.STATUS_OK://网络请求成功，但响应需要根据实际来操作
                    Result result = JSONObject.parseObject(msg.obj.toString(), Result.class);

                    if (result.getCode() == Config.STATUS_SUCCESS) {
                        //网络请求成功
                        System.out.println("result:" + result);
                        JSONArray jsonArray = (JSONArray) result.getData();
                        List<FavouriteShop> list =  jsonArray.toJavaList(FavouriteShop.class);
                        System.out.println("个数:" + list.size());
                        count=list.size();
                        //根据获得的shopid 查找shopinfo
                        for(int i=0;i<list.size();i++)
                        {
                            Map<String, Object> param = new HashMap<>();
                            param.put(Config.REQUEST_PARAMETER_SHOP_ID, list.get(i).getShop_id());
                            OkHttpUtil.post(Config.URL_GetShopInfoByShopidServlet, param, inithandler);
                        }

                        break;
                    }

                default://网络请求失败

                    break;
            }
        }
    };
    private Handler inithandler = new Handler(Looper.myLooper()) {
        @RequiresApi(api = Build.VERSION_CODES.O)
        @Override

        public void handleMessage(Message msg) {
            switch (msg.what) {
                case Config.STATUS_OK://网络请求成功，但响应需要根据实际来操作
                    Result result = JSONObject.parseObject(msg.obj.toString(), Result.class);
                    JSONObject object = (JSONObject) result.getData();
                    Shop_Info shopInfobuy = object.toJavaObject(Shop_Info.class);
                    datas.add(shopInfobuy);
                    --count;
                    System.out.println("--------------count:"+count);
                    if(count==0)
                    {
                        adapter.notifyDataSetChanged();
                    }
                    break;

                default:
                    break;
            }

        }
    };

    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favourite);
        initData();
        init();
        initFunction();

    }

    private void initData() {
        //获得用户的收藏夹
        Map<String, Object> param = new HashMap<>();
        param.put(Config.REQUEST_PARAMETER_USERID, getSharedPreferences("data",MODE_PRIVATE).getString(Config.REQUEST_PARAMETER_USERID,null));
        OkHttpUtil.post(Config.URL_GET_FAVOURITE_LIST, param, handler);

    }

    private void initFunction() {
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        LinearLayoutManager linearLayoutManager=new LinearLayoutManager(this,RecyclerView.VERTICAL,false);
        adapter=new FavouriteAdapter(datas);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setAdapter(adapter);
    }


    private void init() {
        recyclerView=findViewById(R.id.recyclerview);
        toolbar=findViewById(R.id.toolbar);
    }
}
