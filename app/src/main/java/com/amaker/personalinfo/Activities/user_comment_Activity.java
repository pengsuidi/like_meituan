package com.amaker.personalinfo.Activities;

import android.content.Context;
import android.database.Cursor;
import android.media.ThumbnailUtils;
import android.text.TextUtils;
import android.util.Base64;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.provider.MediaStore;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.alibaba.fastjson.JSONObject;
import com.amaker.personalinfo.R;
import com.amaker.personalinfo.entity.Result;
import com.amaker.personalinfo.entity.Shop_Info;
import com.amaker.personalinfo.util.Config;
import com.amaker.personalinfo.util.OkHttpUtil;

import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

public class user_comment_Activity extends AppCompatActivity {
    ImageButton back,to_photo_album;
    ImageView comment_img;
    private Uri uri;
    TextView user_id;
    RatingBar grade;
    EditText comment;
    private Bitmap bitmap;
    private String shop_id,image_64=null;
    private Toolbar comment_toolbar;
    private Handler handler = new Handler(Looper.myLooper()) {
        @Override

        public void handleMessage(Message msg) {
            switch (msg.what) {
                case Config.STATUS_OK://网络请求成功，但响应需要根据实际来操作
                    Result result = JSONObject.parseObject(msg.obj.toString(), Result.class);
                    System.out.println("result:"+result);
                    JSONObject object = (JSONObject) result.getData();
                   final Shop_Info shopInfobuy = object.toJavaObject(Shop_Info.class);
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    shop_id=shopInfobuy.getShop_id().toString();
                                    comment_toolbar.setTitle(shopInfobuy.getShop_name());
                                    SharedPreferences sharedPreferences= getSharedPreferences("data", Context.MODE_PRIVATE);
                                    user_id.setText(sharedPreferences.getString(Config.REQUEST_PARAMETER_USER_ID,null));
                                }
                            });

                        }
                    }).start();

                    break;
                default:
                    Toast.makeText(user_comment_Activity.this,"失败!!",Toast.LENGTH_SHORT).show();
                    break;
            }

        }
    };

    private Handler upload_handler = new Handler(Looper.myLooper()) {
        @Override

        public void handleMessage(Message msg) {
            switch (msg.what) {
                case Config.STATUS_OK://网络请求成功，但响应需要根据实际来操作
                    Toast.makeText(user_comment_Activity.this,"您已发布成功!!",Toast.LENGTH_SHORT).show();

                    break;
                default:
                    Toast.makeText(user_comment_Activity.this,"发布失败!!",Toast.LENGTH_SHORT).show();
                    break;
            }

        }
    };



    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.user_comment);
        initview();
        initEvent();
        
    }


    private void initEvent() {

        comment_toolbar.inflateMenu(R.menu.comment_toolbar_menu);
        comment_toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId())
                {
                    case R.id.upload_comment:
                        sendComment();
                        finish();
                        break;
                }
                return true;
            }
        });
        comment_toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        to_photo_album.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_PICK, null);
                intent.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*");
                startActivityForResult(intent, 2);
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
                System.out.println("uri:---------------------------"+uri);
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                comment_img.setImageURI(uri);
                            }
                        });
                    }
                }).start();
            }
            //                Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.t1, null);
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
            image_64=  Base64.encodeToString(compress_head_photo, Base64.DEFAULT);
        }
    }


    private void sendComment() {
        //要发送图片,文字,店铺名字,用户ID(图片先不管);
        new Thread(new Runnable() {
            @Override
            public void run() {
                System.out.println("img64:---------------"+image_64);
                Map<String, Object> param = new HashMap<>();
                SharedPreferences sharedPreferences= getSharedPreferences("data", Context.MODE_PRIVATE);
                param.put(Config.REQUEST_PARAMETER_SHOP_ID,shop_id);//
                System.out.println("shop_id:-----"+shop_id);
                param.put(Config.REQUEST_PARAMETER_COMMENT, comment.getText());//
                param.put(Config.REQUEST_PARAMETER_USER_ID,user_id.getText());//
                if(image_64==null)
                {
                    image_64="";
                }
                param.put(Config.REQUEST_PARAMETER_CommentIMG_BASE64, image_64);
                param.put(Config.REQUEST_PARAMETER_GRADE,String.valueOf(grade.getRating()));//
                OkHttpUtil.post(Config.URL_UPLOAD_COMMENT, param, upload_handler);
            }
        }).start();

    }

    private void initview() {
        user_id=findViewById(R.id.user_id);
        comment_toolbar=findViewById(R.id.comment_toolbar);
        to_photo_album=findViewById(R.id.to_photo_album);
        comment_img=findViewById(R.id.comment_img);
        SharedPreferences sharedPreferences= getSharedPreferences("data", Context.MODE_PRIVATE);
        user_id.setText(sharedPreferences.getString(Config.REQUEST_PARAMETER_USER_ID,null));
        System.out.println(sharedPreferences.getString(Config.REQUEST_PARAMETER_USER_ID,null));
        grade=findViewById(R.id.grade);
        comment=findViewById(R.id.comment);
        //获得店铺信息
        new Thread(new Runnable() {
            @Override
            public void run() {
                Map<String, Object> param = new HashMap<>();
                param.put(Config.REQUEST_PARAMETER_SHOP_ID, getIntent().getStringExtra(Config.REQUEST_PARAMETER_SHOP_ID));

                System.out.println("usercommentActivity----:"+ getIntent().getStringExtra(Config.REQUEST_PARAMETER_SHOP_ID));
                OkHttpUtil.post(Config.URL_GetShopInfoByShopidServlet, param, handler);

            }
        }).start();




    }
    public static String imageToBase64(String path){
        if(TextUtils.isEmpty(path)){
            System.out.println("path=nul!!!!!!!!!!!");
            return null;
        }
        InputStream is = null;
        byte[] data = null;
        String result = null;
        try{
            is = new FileInputStream(path);
            //创建一个字符流大小的数组。
            data = new byte[is.available()];
            //写入数组
            is.read(data);
            //用默认的编码格式进行编码
            result = Base64.encodeToString(data,Base64.NO_CLOSE);
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            if(null !=is){
                try {
                    is.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

        }
        return result;
    }

    private Bitmap getImageThumbnail(String imagePath, int width, int height) {
        Bitmap bitmap = null;
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        // 获取这个图片的宽和高，注意此处的bitmap为null
        bitmap = BitmapFactory.decodeFile(imagePath, options);
        options.inJustDecodeBounds = false; // 设为 false
        // 计算缩放比
        int h = options.outHeight;
        int w = options.outWidth;
        int beWidth = w / width;
        int beHeight = h / height;
        int be = 1;
        if (beWidth < beHeight) {
            be = beWidth;
        } else {
            be = beHeight;
        }
        if (be <= 0) {
            be = 1;
        }
        options.inSampleSize = be;
        // 重新读入图片，读取缩放后的bitmap，注意这次要把options.inJustDecodeBounds 设为 false
        bitmap = BitmapFactory.decodeFile(imagePath, options);
        // 利用ThumbnailUtils来创建缩略图，这里要指定要缩放哪个Bitmap对象
        bitmap = ThumbnailUtils.extractThumbnail(bitmap, width, height,
                ThumbnailUtils.OPTIONS_RECYCLE_INPUT);
        return bitmap;
    }
    public static String getRealPathFromURI(Context context, Uri contentURI) {
        String result;
        Cursor cursor = context.getContentResolver().query(contentURI,
                new String[]{MediaStore.Images.ImageColumns.DATA},//
                null, null, null);
        if (cursor == null) result = contentURI.getPath();
        else {
            cursor.moveToFirst();
            int index = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA);
            result = cursor.getString(index);
            cursor.close();
        }
        return result;
    }

}
