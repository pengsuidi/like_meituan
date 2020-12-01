package com.amaker.personalinfo.Activities;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.amaker.personalinfo.R;

import com.amaker.personalinfo.entity.Food_Menu;
import com.amaker.personalinfo.entity.Result;
import com.amaker.personalinfo.entity.ShoppingcarFood;
import com.amaker.personalinfo.entity.TotalOid;
import com.amaker.personalinfo.entity.User;
import com.amaker.personalinfo.util.CommonUtil;
import com.amaker.personalinfo.util.Config;
import com.amaker.personalinfo.util.OkHttpUtil;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import pl.droidsonroids.gif.GifImageView;

public class PayMoneyActivity extends AppCompatActivity {
    private String oid, shopid;
    private EditText edt_password;
    private Button pay;
    private GifImageView gif;
    private List<Food_Menu> OrderedFoodList = new ArrayList<>();//从ShoppingActivity中传递过来的食物列表
    private List<Food_Menu> FoodDatas = new ArrayList<>();
    private List<String> food_name_list = new ArrayList<>();
    private List<HashMap<String, Object>> food_count = new ArrayList<>();//纪录食物名称对应的数量
    private List<String> already_food_names = new ArrayList<>();//用来保存已经有了的食物名字
    private int count = 0;
    private Handler get_OrderedFoodList_Handler = new Handler(Looper.myLooper()) {
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
                        System.out.println("foodMenus.size个数:" + foodMenus.size());
                        FoodDatas.clear();
                        //获取总的食物链表
                        FoodDatas.addAll(foodMenus);
                        for (int j = 0; j < food_name_list.size(); j++)
                            for (int i = 0; i < FoodDatas.size(); i++) {
                                if (food_name_list.get(j).contentEquals(FoodDatas.get(i).getFood_name())) {
                                    OrderedFoodList.add(FoodDatas.get(i));
                                    break;
                                }
                            }
                        //获得数据后再初始化
                        init();
                        break;
                    }

                default://网络请求失败

                    break;
            }
        }
    };


    private Handler handler = new Handler(Looper.myLooper()) {
        @RequiresApi(api = Build.VERSION_CODES.O)
        @Override

        public void handleMessage(Message msg) {
            switch (msg.what) {
                case Config.STATUS_OK://网络请求成功，但响应需要根据实际来操作
                    Result result = JSONObject.parseObject(msg.obj.toString(), Result.class);
                    System.out.println("result:" + result);
                    JSONObject jsonObject = (JSONObject) result.getData();
                    User buyer = jsonObject.toJavaObject(User.class);
                    if (edt_password.getText().toString().contentEquals(buyer.getPay_password())) {
                        Toast.makeText(PayMoneyActivity.this, "支付成功!!", Toast.LENGTH_SHORT).show();
                        //获取OID
                        Map<String, Object> param = new HashMap<>();
                        OkHttpUtil.post(Config.URL_GET_OID, param, getOIDhandler);
                    } else {
                        edt_password.setText("");
                        Toast.makeText(PayMoneyActivity.this, "支付失败!!", Toast.LENGTH_SHORT).show();
                    }
                    break;
                default:
                    break;
            }

        }
    };
    private Handler getOIDhandler = new Handler(Looper.myLooper()) {
        @RequiresApi(api = Build.VERSION_CODES.O)
        @Override

        public void handleMessage(Message msg) {
            switch (msg.what) {
                case Config.STATUS_OK://网络请求成功，但响应需要根据实际来操作
                    Result result = JSONObject.parseObject(msg.obj.toString(), Result.class);
                    JSONObject jsonObject = (JSONObject) result.getData();
                    TotalOid totalOid = jsonObject.toJavaObject(TotalOid.class);
                    System.out.println("result1:" + result);
                    oid = String.valueOf(totalOid.getOid());//获得oid
                    //更新oid,oid++
                    Map<String, Object> param = new HashMap<>();
                    param.put(Config.REQUEST_PARAMETER_OLDOID, oid);
                    System.out.println("oldoid:" + oid);
                    oid = String.valueOf(totalOid.getOid() + 1);
                    param.put(Config.REQUEST_PARAMETER_NEWOID, oid);
                    System.out.println("newoid:" + oid);
                    OkHttpUtil.post(Config.URL_UPLOAD_OID, param, getBoughtFoodhandler);


                    break;
                default://登陆失败
                    break;
            }

        }
    };

    private Handler getBoughtFoodhandler = new Handler(Looper.myLooper()) {
        @RequiresApi(api = Build.VERSION_CODES.O)
        @Override

        public void handleMessage(Message msg) {
            switch (msg.what) {
                case Config.STATUS_OK://网络请求成功，但响应需要根据实际来操作

                    Result result = JSONObject.parseObject(msg.obj.toString(), Result.class);
                    System.out.println("getBoughtFoodhandler:result:" + result);
                    System.out.println("-------------OrderFoodList size:" + OrderedFoodList.size());

                    for (int i = 0; i < OrderedFoodList.size(); i++) {
                        Map<String, Object> param = new HashMap<>();
                        param.put(Config.REQUEST_PARAMETER_USER_ID, getSharedPreferences("data", MODE_PRIVATE).getString(Config.REQUEST_PARAMETER_USER_ID, null));
                        param.put(Config.REQUEST_PARAMETER_FOOD_NAME, OrderedFoodList.get(i).getFood_name());
                        System.out.println("上传已购买食物名字:" + OrderedFoodList.get(i).getFood_name());
                        param.put(Config.REQUEST_PARAMETER_FOOD_PRICE, OrderedFoodList.get(i).getFood_price());
                        param.put(Config.REQUEST_PARAMETER_TOTAL_PRICE, getIntent().getStringExtra("sum"));
                        param.put(Config.REQUEST_PARAMETER_OID, oid);
                        param.put(Config.REQUEST_PARAMETER_SHOP_ID, getIntent().getStringExtra(Config.REQUEST_PARAMETER_SHOP_ID));
                        param.put(Config.REQUEST_PARAMETER_SHOPNAME, getIntent().getStringExtra(Config.REQUEST_PARAMETER_SHOPNAME));
                        System.out.println("PayMoneyAc:userid:" + getSharedPreferences("data", MODE_PRIVATE).getString(Config.REQUEST_PARAMETER_USER_ID, null));
                        OkHttpUtil.post(Config.URL_UPDATE_PAYMENT, param, exitHandler);
                    }

                    break;
                default://登陆失败
                    break;
            }

        }
    };


    private Handler exitHandler = new Handler(Looper.myLooper()) {
        @RequiresApi(api = Build.VERSION_CODES.O)
        @Override

        public void handleMessage(Message msg) {
            switch (msg.what) {
                case Config.STATUS_OK://网络请求成功，但响应需要根据实际来操作

                    //
                    Result result = JSONObject.parseObject(msg.obj.toString(), Result.class);
                    System.out.println("收到消息:"+result.getMessage());

                    //退出

                    NotificationManager manager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
                    //高版本需要渠道
                    if (Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
                        //只在Android O之上需要渠道，这里的第一个参数要和下面的channelId一样
                        NotificationChannel notificationChannel = new NotificationChannel("1", "name", NotificationManager.IMPORTANCE_HIGH);
                        //如果这里用IMPORTANCE_NOENE就需要在系统的设置里面开启渠道，通知才能正常弹出
                        if (manager != null) {
                            manager.createNotificationChannel(notificationChannel);
                        }
                    }
                    Intent intent = new Intent(PayMoneyActivity.this, MainNavigationActivity.class);
                    PendingIntent pi = PendingIntent.getActivity(PayMoneyActivity.this, 0, intent, 0);
                    //这里的第二个参数要和上面的id一样
                    Notification notification = new NotificationCompat.Builder(PayMoneyActivity.this, "1")
                            .setContentTitle("购买提示")
                            .setContentText("购买成功!!")
                            .setWhen(System.currentTimeMillis())
                            .setSmallIcon(R.mipmap.ic_launcher)
                            .setLargeIcon(BitmapFactory.decodeResource(getResources(), R.mipmap.ic_launcher))
                            .setContentIntent(pi)
                            .build();
                    if (manager != null) {
                        manager.notify(1, notification);
                    }

                    Map<String, Object> params = new HashMap<>();
                    params.put(Config.REQUEST_PARAMETER_USERID, getSharedPreferences("data", MODE_PRIVATE).getString(Config.REQUEST_PARAMETER_USERID, ""));
                    OkHttpUtil.post(Config.URL_IfSeller, params, judge_seller_handler);

                    break;
                default://登陆失败
                    break;
            }

        }
    };
    private Handler judge_seller_handler = new Handler(Looper.myLooper()) {//判断是否是商家
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case Config.STATUS_OK://是商家
                    Result result = JSONObject.parseObject(msg.obj.toString(), Result.class);
                    count++;
                    if (count == OrderedFoodList.size()) {
                        if (result.getCode() == 200) {
                            Intent intent1 = new Intent(PayMoneyActivity.this, MainNavigationActivity.class);
                            intent1.putExtra(Config.IF_Seller, true);
                            startActivity(intent1);
                            break;
                        } else  //不是商家
                        {
                            Intent intent2 = new Intent(PayMoneyActivity.this, MainNavigationActivity.class);
                            intent2.putExtra(Config.IF_Seller, false);
                            startActivity(intent2);

                            break;
                        }
                    }
                default:


                    break;
            }

        }
    };


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.waiting);
        gif = findViewById(R.id.gif);
        gif.setStateListAnimator(null);
        //获得food_name_list
        if (getIntent().getStringExtra(Config.REQUEST_PARAMETER_SHOP_ID) == null) {
            System.out.println("123123123123123");
            food_name_list.add("锅巴土豆");
            shopid = "1";
        } else {
            System.out.println("aaaaaa");
            food_name_list = getIntent().getStringArrayListExtra(Config.FOOD_NAME_LIST);
            shopid = getIntent().getStringExtra(Config.REQUEST_PARAMETER_SHOP_ID);

        }

        //获取OrderedFoodList
        System.out.println("PayMoneyAc   shopid:" + shopid);
        Map<String, Object> params = new HashMap<>();
        params.put(Config.REQUEST_PARAMETER_SHOP_ID, shopid);
        OkHttpUtil.post(Config.URL_GetFoodInfoServlet, params, get_OrderedFoodList_Handler);

    }

    private void init() {
        setContentView(R.layout.activity_paymoney);
        edt_password = findViewById(R.id.edt_password);
        pay = findViewById(R.id.pay);
        pay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Map<String, Object> param = new HashMap<>();
                param.put(Config.REQUEST_PARAMETER_USER_ID, getSharedPreferences("data", MODE_PRIVATE).getString(Config.REQUEST_PARAMETER_USER_ID, null));
                System.out.println("userid:" + getSharedPreferences("data", MODE_PRIVATE).getString(Config.REQUEST_PARAMETER_USER_ID, null));
                OkHttpUtil.post(Config.URL_GET_PAYPASSWORD, param, handler);
            }
        });
    }


}
