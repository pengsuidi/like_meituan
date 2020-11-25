//package com.amaker.personalinfo.Activities;
//
//import android.content.Context;
//import android.content.SharedPreferences;
//import android.os.Build;
//import android.os.Bundle;
//import android.os.Handler;
//import android.os.Looper;
//import android.os.Message;
//
//import androidx.annotation.RequiresApi;
//import androidx.appcompat.app.AppCompatActivity;
//import androidx.recyclerview.widget.LinearLayoutManager;
//import androidx.recyclerview.widget.RecyclerView;
//
//import com.alibaba.fastjson.JSONArray;
//import com.alibaba.fastjson.JSONObject;
//import com.amaker.personalinfo.R;
//import com.amaker.personalinfo.adapter.order_shop_Adapter;
//import com.amaker.personalinfo.entity.Result;
//import com.amaker.personalinfo.entity.order_shop;
//import com.amaker.personalinfo.util.Config;
//import com.amaker.personalinfo.util.OkHttpUtil;
//
//import java.util.ArrayList;
//import java.util.HashMap;
//import java.util.List;
//import java.util.Map;
//
//public class OrderActivity extends AppCompatActivity {
//    private RecyclerView.Adapter adapter;
//    SharedPreferences sharedPreferences;
//    private RecyclerView recyclerView;
//    private List<order_shop> datas = new ArrayList<>();
//    private int [] oids=new int[100];
//    private int inoid=-1;
//    private  boolean isInoids=false;
//    private Handler handler = new Handler(Looper.myLooper()) {
//        @RequiresApi(api = Build.VERSION_CODES.O)
//        @Override
//        public void handleMessage(Message msg) {
//            switch (msg.what) {
//                case Config.STATUS_OK://网络请求成功，但响应需要根据实际来操作
//                    Result result = JSONObject.parseObject(msg.obj.toString(), Result.class);
//                    System.out.println("result:---" + result);
//                    if (result.getCode() == Config.STATUS_SUCCESS) {
//                        //网络请求成功
//                        JSONArray jsonArray = (JSONArray) result.getData();
//                        List<order_shop> order_shops = jsonArray.toJavaList(order_shop.class);
//                        System.out.println("个数:" + order_shops.size());
//                        System.out.println("--------inoid:"+inoid);
//                        datas.clear();
//                        for (int i = 0; i < order_shops.size(); i++) {
//                            if(order_shops.get(i).getOid()!=inoid)
//                            {
//                                System.out.println("--------inoid:"+inoid);
//                                inoid=order_shops.get(i).getOid();
//                                datas.add(order_shops.get(i));
//                            }
//                        }
//                        adapter.notifyDataSetChanged();
//                        break;
//                    }
//                default://网络请求失败
//
//                    break;
//            }
//        }
//    };
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_order);
//        recyclerView =findViewById(R.id.recycler_orders);
//        sharedPreferences=getSharedPreferences("data", Context.MODE_PRIVATE);
//
//
//        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
//        recyclerView.setLayoutManager(layoutManager);
//        adapter = new order_shop_Adapter(datas,sharedPreferences,OrderActivity.this,);
//        recyclerView.setAdapter(adapter);
//
//        Map<String, Object> params = new HashMap<>();
//        params.put(Config.REQUEST_PARAMETER_USER_ID, sharedPreferences.getString(Config.REQUEST_PARAMETER_USER_ID,null));//userid暂定为1
//        System.out.println("userid:"+sharedPreferences.getString(Config.REQUEST_PARAMETER_USER_ID,null));
//        OkHttpUtil.post(Config.URL_GET_ORDERS, params, handler);
//    }
//
//}
