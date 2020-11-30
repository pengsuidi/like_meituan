package com.amaker.personalinfo.Activities;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.provider.MediaStore;
import android.util.Base64;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.amaker.personalinfo.Fragment_NavigationUI.MainPage.MainPageFragment;
import com.amaker.personalinfo.R;
import com.amaker.personalinfo.entity.Result;
import com.amaker.personalinfo.entity.Shop_Info;
import com.amaker.personalinfo.util.CommonUtil;
import com.amaker.personalinfo.util.Config;
import com.amaker.personalinfo.util.OkHttpUtil;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class UploadShopInfoActivity extends AppCompatActivity {
    private ImageButton upload_shop_img;
    private EditText edit_shop_name,edit_shop_type;
    private Uri uri;
    private Bitmap bitmap;
    private String image_64;
    private String shopid="1";
    private Toolbar toolbar;
    private Handler handler = new Handler(Looper.myLooper()) {
        @RequiresApi(api = Build.VERSION_CODES.O)
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case Config.STATUS_OK://网络请求成功
                    Result result = JSONObject.parseObject(msg.obj.toString(), Result.class);
                    if(result.getCode()==Config.STATUS_OK)
                    {
                        Intent intent=new Intent(UploadShopInfoActivity.this, MainPageFragment.class);
                        startActivity(intent);
                    }

                    break;
                default://登陆失败
                    break;
            }

        }
    };

    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload_shopinfo);
        upload_shop_img=findViewById(R.id.upload_shop_img);
        edit_shop_type=findViewById(R.id.edit_shop_type);
        edit_shop_name=findViewById(R.id.edit_shop_name);
        toolbar=findViewById(R.id.toolbar_upload_shop_info);
        upload_shop_img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_PICK, null);
                intent.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*");
                startActivityForResult(intent, 2);
            }
        });
        toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId())
                {
                    case R.id.upload_shop_info:
                        Map<String, Object> param = new HashMap<>();
                        param.put(Config.REQUEST_PARAMETER_SHOPNAME,edit_shop_name.getText());
                        param.put(Config.REQUEST_PARAMETER_SHOPTYPE,edit_shop_type.getText());
                        param.put(Config.REQUEST_PARAMETER_USERID,getSharedPreferences("data", Context.MODE_PRIVATE)
                                .getString(Config.REQUEST_PARAMETER_USER_ID, ""));
                        param.put(Config.REQUEST_PARAMETER_SHOPIMG_BASE64,image_64);
                        System.out.println("UploadShopinfoActivity:-------------"+image_64);
                        OkHttpUtil.post(Config.URL_UPLOAD_ShopInfo, param, handler);

                        break;
                }
                return false;
            }
        });
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 2) {
            // 从相册返回的数据
            if (data != null) {
                // 得到图片的全路径
                uri = data.getData();
                System.out.println("uri:---------------------------" + uri);
            }
            //                Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.t1, null);
            try {
                bitmap = BitmapFactory.decodeStream(getContentResolver().openInputStream(uri));
                bitmap=Bitmap.createScaledBitmap(bitmap,750,750,true);
                bitmap= CommonUtil.getRoundedCornerBitmap(bitmap,20);
                upload_shop_img.setImageBitmap(bitmap);
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
        }
    }
}

