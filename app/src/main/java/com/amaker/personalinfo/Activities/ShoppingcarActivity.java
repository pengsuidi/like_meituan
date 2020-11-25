package com.amaker.personalinfo.Activities;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.amaker.personalinfo.R;
import com.amaker.personalinfo.adapter.ShoppingcarFoodAdapter;
import com.amaker.personalinfo.entity.Food_Menu;
import com.amaker.personalinfo.entity.Result;
import com.amaker.personalinfo.entity.Shop;
import com.amaker.personalinfo.entity.ShoppingcarFood;
import com.amaker.personalinfo.util.CommonUtil;
import com.amaker.personalinfo.util.Config;
import com.amaker.personalinfo.util.OkHttpUtil;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import pl.droidsonroids.gif.GifImageView;

public class ShoppingcarActivity extends AppCompatActivity {

    Boolean isfavourite = false;
    private Button buy_btn;
    private androidx.appcompat.widget.Toolbar toolbar;
    private TextView total;
    private RecyclerView recyclerView;
    private List<Food_Menu> OrderedFoodList = new ArrayList<>();//对FoodDatas操作后的列表
    private List<Food_Menu> FoodDatas = new ArrayList<>();//从menuFragment中获取的全部食物列表
    private RecyclerView.Adapter adapter;
    private RecyclerView.LayoutManager layoutManager;
    private ArrayList<String> food_name_list = new ArrayList<>();
    private Float sum = 0.0f;
    private GifImageView gif;
    private Handler handler = new Handler(Looper.myLooper()) {
        @RequiresApi(api = Build.VERSION_CODES.O)
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case Config.STATUS_OK://网络请求成功，但响应需要根据实际来操作
                    Result result = JSONObject.parseObject(msg.obj.toString(), Result.class);

                    if (result.getCode() == Config.STATUS_SUCCESS) {
                        //网络请求成功
                        System.out.println("data:" + result.getData());
                        System.out.println("result:" + result);
                        JSONArray jsonArray = (JSONArray) result.getData();
                        List<Food_Menu> foodMenus = jsonArray.toJavaList(Food_Menu.class);
                        System.out.println("个数:" + foodMenus.size());
                        FoodDatas.clear();
                        //获取总的食物链表
                        FoodDatas.addAll(foodMenus);
                        System.out.println("购买的数量:" + food_name_list.size());
                        for (int j = 0; j < food_name_list.size(); j++)
                            for (int i = 0; i < FoodDatas.size(); i++) {
                                if (food_name_list.get(j).contentEquals(FoodDatas.get(i).getFood_name())) {
                                    OrderedFoodList.add(FoodDatas.get(i));
                                    break;

                                }
                            }
                        //获得数据后显示界面
                        init();

                        //计算sum
                        for (int i = 0; i < OrderedFoodList.size(); i++) {
                            sum += Float.parseFloat(OrderedFoodList.get(i).getFood_price());
                        }
                        total.setText(sum.toString());
                        //计算每个食物的数量
                        int count = 1;
                        List<Food_Menu> tmp = OrderedFoodList;
                        for (int i = 0; i < OrderedFoodList.size(); i++) {
                            count = 1;
                            OrderedFoodList.get(i).setCount(count);
                            for (int j = 0; j < tmp.size(); j++) {
                                if (j == i)
                                    //不重复比较自己
                                    continue;
                                if (OrderedFoodList.get(i).getFood_name().contentEquals(tmp.get(j).getFood_name())) {
                                    OrderedFoodList.get(i).setCount(++count);
                                }
                            }
                        }
                        //去除重复的食物
                        OrderedFoodList = CommonUtil.removeDuplicateWithOrder(OrderedFoodList);
                        for (int i = 0; i < OrderedFoodList.size(); i++) {
                            System.out.println("购物车:食物数量:" + OrderedFoodList.get(i).getFood_name() + "----:" + OrderedFoodList.get(i).getCount());
                        }
                        System.out.println("购物车:FoodList.size:" + OrderedFoodList.size());
                        //recyclerview
                        recyclerView = findViewById(R.id.shoppingcar_recyclerview);
                        layoutManager = new LinearLayoutManager(ShoppingcarActivity.this, LinearLayoutManager.VERTICAL, false);
                        adapter = new ShoppingcarFoodAdapter(OrderedFoodList);
                        recyclerView.setLayoutManager(layoutManager);
                        recyclerView.setAdapter(adapter);

                        break;
                    }

                default://网络请求失败

                    break;
            }
        }
    };


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.waiting);
        gif=findViewById(R.id.gif);
        gif.setVisibility(View.VISIBLE);
        gif.setStateListAnimator(null);
        //获得OrderedFoodList
        System.out.println("shoppingcar   shopid:" + getIntent().getStringExtra(Config.REQUEST_PARAMETER_SHOP_ID));
        Map<String, Object> params = new HashMap<>();
        params.put(Config.REQUEST_PARAMETER_SHOP_ID, getIntent().getStringExtra(Config.REQUEST_PARAMETER_SHOP_ID));

        OkHttpUtil.post(Config.URL_GetFoodInfoServlet, params, handler);
        //获取Food_name_list
        food_name_list = getIntent().getStringArrayListExtra(Config.FOOD_NAME_LIST);





    }
    private void init(){
        setContentView(R.layout.activity_shoppingcar);
        buy_btn = findViewById(R.id.buy_btn);
        toolbar = findViewById(R.id.toolbar);
        total = findViewById(R.id.sum);
        buy_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ShoppingcarActivity.this, PayMoneyActivity.class);
                intent.putStringArrayListExtra(Config.FOOD_NAME_LIST,food_name_list);
                intent.putExtra("sum", sum.toString());
                intent.putExtra(Config.REQUEST_PARAMETER_SHOP_ID, getIntent().getStringExtra(Config.REQUEST_PARAMETER_SHOP_ID));
                startActivity(intent);
            }
        });
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    public Bitmap getBitmapFromByte(byte[] temp) {   //将二进制转化为bitmap
        if (temp != null) {
            Bitmap bitmap = BitmapFactory.decodeByteArray(temp, 0, temp.length);
            return bitmap;
        } else {
            return null;
        }
    }

    public String getNumber(String str) {
        str = str.trim();
        String str2 = "";
        if (str != null && !"".equals(str)) {
            for (int i = 0; i < str.length(); i++) {
                if (str.charAt(i) >= 48 && str.charAt(i) <= 57) {
                    str2 += str.charAt(i);
                }
            }
        }
        return str2;
    }
}
