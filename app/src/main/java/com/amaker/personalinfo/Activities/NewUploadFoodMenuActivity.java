package com.amaker.personalinfo.Activities;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.net.Uri;
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
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.Toast;
import com.amaker.personalinfo.util.Config;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.alibaba.fastjson.JSONObject;
import com.amaker.personalinfo.R;
import com.amaker.personalinfo.entity.Result;

import com.amaker.personalinfo.util.OkHttpUtil;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.util.HashMap;

public class NewUploadFoodMenuActivity extends AppCompatActivity {
    private EditText food_name, food_description, food_price, food_type;
    private ImageButton to_choose_FoodImg;
    private Uri uri;
    private String image_64;
    private Bitmap bitmap,tmp;
    private Toolbar toolbar;
    private Handler upload_handler = new Handler(Looper.myLooper()) {
        @Override

        public void handleMessage(Message msg) {
            switch (msg.what) {
                case Config.STATUS_OK://网络请求成功，但响应需要根据实际来操作
                    Toast.makeText(NewUploadFoodMenuActivity.this, "您已经上传成功!!", Toast.LENGTH_SHORT).show();
                    Result result = JSONObject.parseObject(msg.obj.toString(), Result.class);
                    System.out.println("result:" + result);
                    finish();
                    break;
                default:
                    Toast.makeText(NewUploadFoodMenuActivity.this, "上传失败!!", Toast.LENGTH_SHORT).show();
                    finish();
                    break;
            }

        }
    };
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_newuploadfoodmenu);
        initviews();
        initfunctions();
        UploadFoodInfo();
    }

    private void UploadFoodInfo() {

    }

    private void initfunctions() {
        to_choose_FoodImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_PICK, null);
                intent.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*");
                startActivityForResult(intent, 5);
            }
        });
        toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId())
                {
                    case R.id.upload_NewFood_info:
                        if(food_name.getText()!=null
                                &&food_description.getText()!=null
                                &&food_price.getText()!=null
                                &&food_type.getText()!=null)
                        {
                            HashMap<String, Object> param = new HashMap<>();
                            param.put(Config.REQUEST_PARAMETER_FOOD_NAME, food_name.getText().toString());
                            param.put(Config.REQUEST_PARAMETER_FOOD_DESCRIPTION, food_description.getText().toString());
                            param.put(Config.REQUEST_PARAMETER_FOOD_PRICE, food_price.getText().toString());
                            param.put(Config.REQUEST_PARAMETER_FOOD_TYPE, food_type.getText().toString());
                            param.put(Config.REQUEST_PARAMETER_SHOP_ID,getIntent().getStringExtra(Config.REQUEST_PARAMETER_SHOP_ID));//暂时为3
                            param.put(Config.REQUEST_PARAMETER_FOOD_IMG_64, image_64);
                            OkHttpUtil.post(Config.URL_UPLOAD_FOOD_MENU, param, upload_handler);
                        }else{
                            Toast.makeText(NewUploadFoodMenuActivity.this,"内容不能为空",Toast.LENGTH_SHORT).show();
                        }

                        break;
                }
                return false;
            }
        });
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 5) {
            // 从相册返回的数据
            if (data != null) {
                // 得到图片的全路径
                uri = data.getData();
                System.out.println("uri:---------------------------"+uri);
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                to_choose_FoodImg.setImageURI(uri);
                            }
                        });
                    }
                }).start();
            }
            //                Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.t1, null);
            try {
                bitmap = BitmapFactory.decodeStream(getContentResolver().openInputStream(uri));
                //精确缩放到指定大小
                tmp = Bitmap.createScaledBitmap(bitmap,270,240, true);
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                tmp=getRoundedCornerBitmap(tmp,10);
                                to_choose_FoodImg.setImageBitmap(tmp);
                            }
                        });
                    }
                }).start();
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
            image_64=  Base64.encodeToString(compress_head_photo, Base64.DEFAULT);
        }
    }


    private void initviews() {
        food_type = findViewById(R.id.food_type);
        food_name = findViewById(R.id.food_name);
        food_description = findViewById(R.id.food_description);
        food_price = findViewById(R.id.food_price);
        to_choose_FoodImg = findViewById(R.id.to_choose_FoodImg);
        toolbar = findViewById(R.id.toolbar_upload_food_menu);
    }
    public static Bitmap getRoundedCornerBitmap(Bitmap bitmap,float roundPx){

        Bitmap output = Bitmap.createBitmap(bitmap.getWidth(), bitmap
                .getHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(output);

        final int color = 0xff424242;
        final Paint paint = new Paint();
        final Rect rect = new Rect(0, 0, bitmap.getWidth(), bitmap.getHeight());
        final RectF rectF = new RectF(rect);

        paint.setAntiAlias(true);
        canvas.drawARGB(0, 0, 0, 0);
        paint.setColor(color);
        canvas.drawRoundRect(rectF, roundPx, roundPx, paint);

        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
        canvas.drawBitmap(bitmap, rect, rect, paint);

        return output;
    }
}
