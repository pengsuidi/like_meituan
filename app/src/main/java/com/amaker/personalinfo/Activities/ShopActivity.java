package com.amaker.personalinfo.Activities;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.alibaba.fastjson.JSONObject;
import com.amaker.personalinfo.Fragment.commentFragment;
import com.amaker.personalinfo.Fragment.menuFragment;
import com.amaker.personalinfo.R;
import com.amaker.personalinfo.entity.Result;
import com.amaker.personalinfo.entity.Shop_Info;
import com.amaker.personalinfo.util.Config;
import com.amaker.personalinfo.util.OkHttpUtil;
import com.amaker.personalinfo.util.PreferenceUtil;

import java.util.HashMap;
import java.util.Map;
public class ShopActivity extends AppCompatActivity implements View.OnClickListener {

    private Button order_btn, comment_btn, favourite;
    private EditText editText;
    private ImageView image;
    private Boolean isfavourite=false;
    private Toolbar toolbar_shop;
    private String shop_id;
    private String arg_Shop_name;
    private String arg_Shop_id;
    private Handler handler = new Handler(Looper.myLooper()) {
        @RequiresApi(api = Build.VERSION_CODES.O)
        @Override

        public void handleMessage(Message msg) {
            switch (msg.what) {
                case Config.STATUS_OK://网络请求成功，但响应需要根据实际来操作
                    Result result = JSONObject.parseObject(msg.obj.toString(), Result.class);
                    JSONObject object = (JSONObject) result.getData();
                    Shop_Info shopInfobuy = object.toJavaObject(Shop_Info.class);
                    Bitmap bitmap = getBitmapFromByte(shopInfobuy.getShop_image());
                    shop_id=shopInfobuy.getShop_id().toString();
                    arg_Shop_id=shopInfobuy.getShop_id().toString();
                    arg_Shop_name=shopInfobuy.getShop_name();//传给碎片shopname
                    System.out.println("shop_id-------:"+shop_id);
                    SharedPreferences.Editor editor= getSharedPreferences("data",MODE_PRIVATE).edit();
                    editor.putString(Config.REQUEST_PARAMETER_SHOP_ID,shop_id);
                    editor.commit();
                    bitmap=bitmap.createScaledBitmap(bitmap,1200,450,true);
                    image.setImageBitmap(bitmap);
                    toolbar_shop.setTitle(shopInfobuy.getShop_name());
                    toolbar_shop.setTitleTextColor(Color.BLUE);
                    toolbar_shop.setNavigationOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            finish();
                        }
                    });
                    //获得收藏
                    Map<String, Object> param = new HashMap<>();
                    param.put(Config.REQUEST_PARAMETER_SHOP_ID, shop_id);//静态暂定shopid=1

                    SharedPreferences sharedPreferences= getSharedPreferences("data", Context.MODE_PRIVATE);
                    param.put(Config.REQUEST_PARAMETER_USER_ID, sharedPreferences.getString(Config.REQUEST_PARAMETER_USER_ID,null));//1
                    System.out.println("user_id:"+sharedPreferences.getString(Config.REQUEST_PARAMETER_USER_ID,null));
                    OkHttpUtil.post(Config.URL_IF_FAVOURITE, param, Favourite_handler);
                    //先显示点菜Fragment
                    Bundle args = new Bundle();
                    args.putString(Config.REQUEST_PARAMETER_SHOPNAME, arg_Shop_name);
                    args.putString(Config.REQUEST_PARAMETER_SHOP_ID, arg_Shop_id);
                    Fragment fragment=new menuFragment();
                    fragment.setArguments(args);
                    replaceFragment(fragment,args);
                    break;
                default:
                    break;
            }

        }
    };
    private Handler Favourite_handler = new Handler(Looper.myLooper()) {
        @RequiresApi(api = Build.VERSION_CODES.O)
        @Override

        public void handleMessage(Message msg) {
            switch (msg.what) {
                case Config.STATUS_OK:
                    Result result = JSONObject.parseObject(msg.obj.toString(), Result.class);
                    if(result.getMessage().contentEquals("成功"))
                    {
                        isfavourite = true;
                        favourite.setBackgroundResource(R.drawable.favourite);
                    }
                    break;

                default://登陆失败
                    break;
            }

        }
    };
    private Handler delete_handler = new Handler(Looper.myLooper()) {
        @RequiresApi(api = Build.VERSION_CODES.O)
        @Override

        public void handleMessage(Message msg) {
            switch (msg.what) {
                case Config.STATUS_OK:

                    Toast.makeText(ShopActivity.this,"取消收藏!",Toast.LENGTH_SHORT).show();
                    break;

                default://登陆失败
                    break;
            }

        }
    };
    private Bundle bundle;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.shop);

        toolbar_shop = findViewById(R.id.toolbar_shop);
        image = findViewById(R.id.image);
        comment_btn = findViewById(R.id.btn_comment);
        favourite = findViewById(R.id.favourite);
        order_btn = findViewById(R.id.btn_order);
        comment_btn.setOnClickListener(this);
        order_btn.setOnClickListener(this);
        favourite.setOnClickListener(this);


        //使用shopid初始化
        System.out.println("ShopActivity:shopid:-------:"+getIntent().getStringExtra(Config.REQUEST_PARAMETER_SHOP_ID));
        Map<String, Object> param = new HashMap<>();
        param.put(Config.REQUEST_PARAMETER_SHOP_ID, getIntent().getStringExtra(Config.REQUEST_PARAMETER_SHOP_ID));
        OkHttpUtil.post(Config.URL_GetShopInfoByShopidServlet, param, handler);

        //把shopid写入bundler
        bundle = new Bundle();
        bundle.putString(Config.REQUEST_PARAMETER_SHOP_ID, getIntent().getStringExtra(Config.REQUEST_PARAMETER_SHOP_ID));



    }

    public Bitmap getBitmapFromByte(byte[] temp) {   //将二进制转化为bitmap
        if (temp != null) {
            Bitmap bitmap = BitmapFactory.decodeByteArray(temp, 0, temp.length);
            return bitmap;
        } else {
            return null;
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_comment:
                comment_btn.setTextColor(R.color.cutePink);
                order_btn.setTextColor(R.color.black);
                replaceFragment(new commentFragment(),bundle);
                break;
            case R.id.btn_order:
                comment_btn.setTextColor(R.color.black);
                order_btn.setTextColor(R.color.cutePink);
                Bundle args = new Bundle();
                args.putString(Config.REQUEST_PARAMETER_SHOPNAME, arg_Shop_name);
                args.putString(Config.REQUEST_PARAMETER_SHOP_ID, arg_Shop_id);
                Fragment fragment=new menuFragment();
                fragment.setArguments(args);
                replaceFragment(fragment,args);

                break;
            case R.id.favourite:
                if (isfavourite) {
                    //向服务器发送"不收藏"
                    favourite.setBackgroundResource(R.drawable.not_favorite);
                    isfavourite = false;
                    Map<String, Object> param = new HashMap<>();
                    param.put(Config.REQUEST_PARAMETER_SHOP_ID, shop_id);//静态暂定shopid=1
                    SharedPreferences sharedPreferences= getSharedPreferences("data", Context.MODE_PRIVATE);
                    param.put(Config.REQUEST_PARAMETER_USER_ID, sharedPreferences.getString(Config.REQUEST_PARAMETER_USER_ID,null));//1
                    OkHttpUtil.post(Config.URL_DELETE_FAVOURITE, param, delete_handler);
                    //
                } else {
                    favourite.setBackgroundResource(R.drawable.favourite);
                    isfavourite = true;
                    //向服务器发送"收藏"
                    Map<String, Object> param = new HashMap<>();
                    param.put(Config.REQUEST_PARAMETER_SHOP_ID, shop_id);//静态暂定shopid=1
                    SharedPreferences sharedPreferences= getSharedPreferences("data", Context.MODE_PRIVATE);
                    param.put(Config.REQUEST_PARAMETER_USER_ID, sharedPreferences.getString(Config.REQUEST_PARAMETER_USER_ID,null));//1
                    param.put(Config.REQUEST_PARAMETER_SHOPNAME, toolbar_shop.getTitle().toString());
                    OkHttpUtil.post(Config.URL_UPLOAD_FAVOURTIE, param, Favourite_handler);
                }
                break;
                // favourite.setBackgroundResource(R.drawable.favourite);
        }
    }

    private void replaceFragment(Fragment fragment,Bundle bundle) {
        fragment.setArguments(bundle);
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.order_or_comment_fragment, fragment);
        fragmentTransaction.commit();
    }
}
