//package com.amaker.personalinfo.Activities;
//
//import android.content.Context;
//import android.content.SharedPreferences;
//import android.graphics.Bitmap;
//import android.graphics.BitmapFactory;
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
//import com.amaker.personalinfo.entity.Shop_info_buy;
//import com.amaker.personalinfo.entity.order_shop;
//import com.amaker.personalinfo.util.Config;
//import com.amaker.personalinfo.util.OkHttpUtil;
//
//import java.util.ArrayList;
//import java.util.HashMap;
//import java.util.List;
//import java.util.Map;
//
//public class HistoryOrderActivity extends AppCompatActivity {
//    private RecyclerView.Adapter adapter;
//    SharedPreferences sharedPreferences;
//    private RecyclerView recyclerView;
//    private List<order_shop> datas = new ArrayList<>();
//    private List<Bitmap> bitmapList=new ArrayList<>();
//    private int inoid=-1;
//
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
//                        bitmapList.clear();
//                        for (int i = 0; i < order_shops.size(); i++) {
//                            if(order_shops.get(i).getOid()!=inoid)
//                            {
//                                System.out.println("--------inoid:"+inoid);
//                                inoid=order_shops.get(i).getOid();
//                                datas.add(order_shops.get(i));
//                                //根据ShopId获取其图片
//                                Map<String, Object> param = new HashMap<>();
//                                param.put(Config.REQUEST_PARAMETER_SHOP_ID,order_shops.get(i).getShop_id());
//                                OkHttpUtil.post(Config.URL_GetShopInfoByShopidServlet, param, GetBitmapHandler);
//                            }
//
//                        }
//                       // adapter.notifyDataSetChanged();
//
//                        break;
//                    }
//                default://网络请求失败
//
//                    break;
//            }
//        }
//    };
//
//
//    private Handler GetBitmapHandler = new Handler(Looper.myLooper()) {
//        @RequiresApi(api = Build.VERSION_CODES.O)
//        @Override
//
//        public void handleMessage(Message msg) {
//            switch (msg.what) {
//                case Config.STATUS_OK://网络请求成功，但响应需要根据实际来操作
//                    Result result = JSONObject.parseObject(msg.obj.toString(), Result.class);
//                    JSONObject object = (JSONObject) result.getData();
//                    Shop_info_buy shopInfobuy = object.toJavaObject(Shop_info_buy.class);
//                    Bitmap bitmap = getBitmapFromByte(shopInfobuy.getShop_image());
//                    bitmapList.add(bitmap);
//                    if(bitmapList.size()==datas.size())
//                    {
//                        System.out.println("每一个的图片已经接收");
//                        adapter.notifyDataSetChanged();
//                    }
//                    break;
//                default:
//                    break;
//            }
//
//        }
//    };
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_history_order);
//        recyclerView =findViewById(R.id.recycler_orders);
//        sharedPreferences=getSharedPreferences("data", Context.MODE_PRIVATE);
//
//
//        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
//        recyclerView.setLayoutManager(layoutManager);
//        adapter = new order_shop_Adapter(datas,sharedPreferences, HistoryOrderActivity.this,bitmapList);
//        recyclerView.setAdapter(adapter);
//
//        Map<String, Object> params = new HashMap<>();
//        params.put(Config.REQUEST_PARAMETER_USER_ID, sharedPreferences.getString(Config.REQUEST_PARAMETER_USER_ID,null));//userid暂定为1
//        System.out.println("userid:"+sharedPreferences.getString(Config.REQUEST_PARAMETER_USER_ID,null));
//        OkHttpUtil.post(Config.URL_GET_ORDERS, params, handler);
//    }
//    public Bitmap getBitmapFromByte(byte[] temp) {   //将二进制转化为bitmap
//        if (temp != null) {
//            Bitmap bitmap = BitmapFactory.decodeByteArray(temp, 0, temp.length);
//            return bitmap;
//        } else {
//            return null;
//        }
//    }
//}
