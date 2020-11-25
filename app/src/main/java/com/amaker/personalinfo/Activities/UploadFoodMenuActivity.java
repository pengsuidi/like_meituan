package com.amaker.personalinfo.Activities;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.provider.MediaStore;
import android.util.ArrayMap;
import android.util.Base64;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.alibaba.fastjson.JSONObject;
import com.amaker.personalinfo.R;
import com.amaker.personalinfo.adapter.UploadFoodMenuAdapter;
import com.amaker.personalinfo.entity.Food_Menu;
import com.amaker.personalinfo.entity.Result;
import com.amaker.personalinfo.util.Config;
import com.amaker.personalinfo.util.OkHttpUtil;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class UploadFoodMenuActivity extends AppCompatActivity {
    private RecyclerView.Adapter adapter;
    private RecyclerView recyclerView_upload_foodmenu;
    private ImageButton add_new_food;
    private List<Uri> UriList = new ArrayList<>();
    private Uri uri;
    private String image_64;
    private Toolbar toolbar;
    private List<Food_Menu> foodMenuList = new ArrayList<>();
    private int[] FoodIdList;
    private int id = 0;

    private Handler handler = new Handler(Looper.myLooper()) {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case Config.Upload_food_menu://收到Adapter的数据
                    System.out.println("收到新food!");
                    ArrayList arr = msg.getData().getParcelableArrayList(Config.FOOD_MENU_INFO);

                    Food_Menu food_menu = (Food_Menu) arr.get(0);
                    System.out.println("food_menu_foodname:" + food_menu.getFood_name());
//                    food_menu.setShop_id(Integer.parseInt(getIntent().getStringExtra(Config.REQUEST_PARAMETER_SHOP_ID)));
                    food_menu.setShop_id(3);
                    System.out.println("需要更新的id:"+food_menu.getFood_id());
                    //此时food_menu已经包括了一个食品的全部信息(除了图片的地址和byte,无用)
                    for (int i = 0; i < foodMenuList.size(); i++) {
                        System.out.println("foodMenuList.get(i).getFood_id() :"+foodMenuList.get(i).getFood_id() );
                        if (foodMenuList.get(i).getFood_id() == food_menu.getFood_id()) {
                            //替换第i个:先插入第i个再删除i+1个
                            System.out.println("before:"+foodMenuList.size());
                            foodMenuList.add(i,food_menu);
                            foodMenuList.remove(i+1);
                            System.out.println("after:"+foodMenuList.size());
                            break;
                        }

                    }
                    break;
                case Config.Delete_food_menu:
                    int foodid = msg.getData().getInt(Config.DELETE_FOOD_ID);
                    System.out.println("position:" + foodid);
                    for (int i = 0; i < foodMenuList.size(); i++) {
                        System.out.println("foodMenuList.get(i).getFood_id() :"+foodMenuList.get(i).getFood_id() );
                        if (foodMenuList.get(i).getFood_id() == foodid) {
                            foodMenuList.remove(i);
                            break;
                        }

                    }

                    System.out.println("afterremov:foodMenuList_size:" + foodMenuList.size());
                    adapter.notifyDataSetChanged();
//                    adapter.notifyItemChanged(position);
                    break;

                default:
                    break;
            }

        }
    };
    private Handler upload_handler = new Handler(Looper.myLooper()) {
        @Override

        public void handleMessage(Message msg) {
            switch (msg.what) {
                case Config.STATUS_OK://网络请求成功，但响应需要根据实际来操作
                    Toast.makeText(UploadFoodMenuActivity.this, "您已经上传成功!!", Toast.LENGTH_SHORT).show();
                    Result result = JSONObject.parseObject(msg.obj.toString(), Result.class);
                    System.out.println("result:" + result);
                    break;
                default:
                    Toast.makeText(UploadFoodMenuActivity.this, "上传失败!!", Toast.LENGTH_SHORT).show();
                    break;
            }

        }
    };

    private Bitmap bitmap;
    private HashMap<String, Object> map = new HashMap<>();

    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload_food_menu);
        recyclerView_upload_foodmenu = findViewById(R.id.recyclerview_upload_foodmenu);
        toolbar = findViewById(R.id.toolbar_upload_food_menu);
        add_new_food = findViewById(R.id.add_new_food);//点击增加食物

        RecyclerView.LayoutManager linearLayoutManager = new LinearLayoutManager(this, RecyclerView.VERTICAL, false);
        recyclerView_upload_foodmenu.setLayoutManager(linearLayoutManager);
        adapter = new UploadFoodMenuAdapter(UploadFoodMenuActivity.this, handler, foodMenuList);
        recyclerView_upload_foodmenu.setAdapter(adapter);
        adapter.notifyDataSetChanged();
        toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.upload_NewFood_info:
                        for (int i = 0; i < foodMenuList.size(); i++) {
                            HashMap<String, Object> param = new HashMap<>();
                            param.put(Config.REQUEST_PARAMETER_FOOD_NAME, foodMenuList.get(i).getFood_name());
                            System.out.println("foodname:" + foodMenuList.get(i).getFood_name());
                            param.put(Config.REQUEST_PARAMETER_FOOD_DESCRIPTION, foodMenuList.get(i).getFood_description());
                            param.put(Config.REQUEST_PARAMETER_FOOD_PRICE, foodMenuList.get(i).getFood_price());
                            param.put(Config.REQUEST_PARAMETER_FOOD_TYPE, foodMenuList.get(i).getFood_type());
                            param.put(Config.REQUEST_PARAMETER_SHOP_ID, foodMenuList.get(i).getShop_id());
                            param.put(Config.REQUEST_PARAMETER_FOOD_IMG_64, foodMenuList.get(i).getImage_64());
                            OkHttpUtil.post(Config.URL_UPLOAD_FOOD_MENU, param, upload_handler);
                        }

                        break;
                }
                return false;
            }
        });
        add_new_food.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Food_Menu food_menu = new Food_Menu();
                food_menu.setFood_id(id);
                id++;
                foodMenuList.add(food_menu);
                System.out.println("addfood:size:"+foodMenuList.size());
                System.out.println("addfood:name:"+food_menu.getFood_name());
                adapter.notifyDataSetChanged();
            }
        });

    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        // 从相册返回的数据
        if (data != null) {
            // 得到图片的全路径
            uri = data.getData();
            System.out.println("UpLoadFoodMenuActivity:  uri:---------------------------" + uri);
        }
        try {
            bitmap = BitmapFactory.decodeStream(getContentResolver().openInputStream(uri));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

//                bitmap=getImageThumbnail(getRealPathFromURI(user_comment_Activity.this,uri),50,50);
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        //清空画图缓存否则下次获取图片时还是原图片
        if (null != bitmap) {
            //对图片进行压缩，100为不压缩，并写入字节流中
            bitmap.compress(Bitmap.CompressFormat.JPEG, 30, bos);
        }
        //获取图片的二进制
        byte[] compress_head_photo = bos.toByteArray();
        try {
            bos.close();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        //对二进制数组进行编码
        image_64 = Base64.encodeToString(compress_head_photo, Base64.DEFAULT);
        System.out.println("image_64:" + image_64);

        for (int i = 0; i < foodMenuList.size(); i++) {
            if (foodMenuList.get(i).getFood_id() == requestCode) {
                foodMenuList.get(i).setImage_64(image_64);
                foodMenuList.get(i).setUri(uri.toString());
                adapter.notifyDataSetChanged();
                break;
            }

        }


    }


}
