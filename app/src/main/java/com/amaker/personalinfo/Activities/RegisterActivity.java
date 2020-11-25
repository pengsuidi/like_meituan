package com.amaker.personalinfo.Activities;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Base64;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.alibaba.fastjson.JSONObject;
import com.amaker.personalinfo.R;
import com.amaker.personalinfo.entity.Result;
import com.amaker.personalinfo.util.Config;
import com.amaker.personalinfo.util.OkHttpUtil;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.util.HashMap;
import java.util.Map;

public class RegisterActivity extends AppCompatActivity {

    private EditText edtUsername, edtPassword;
    private String image_64;
    private Handler handler = new Handler(Looper.myLooper()) {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case Config.STATUS_OK://网络请求成功，但响应需要根据实际来操作
                    Result result = JSONObject.parseObject(msg.obj.toString(), Result.class);

                    if (result.getCode() == Config.STATUS_SUCCESS) {
                        //登陆成功
                        registerSuccess();
                    } else {
                        //登陆失败
                        registerFailure();
                    }
                    break;
                default://登陆失败
                    registerFailure();
                    break;
            }

        }
    };


    private void registerSuccess() {
        Toast.makeText(RegisterActivity.this, "注册成功", Toast.LENGTH_SHORT).show();
//        Map<String, Object> param = new HashMap<>();
//        param.put(Config.REQUEST_PARAMETER_USER_ID,String.valueOf(1+Integer.parseInt(getSharedPreferences("data",MODE_PRIVATE).getString("userid",null))));
//        param.put("paypassword","123");
//
//        OkHttpUtil.post(Config.URL_INSERT_BUYER, param, newhandler);

        startActivity(new Intent(RegisterActivity.this, LoginActivity.class));

        finish();
    }

    /**
     * 登陆失败提示
     */
    private void registerFailure() {
        Toast.makeText(RegisterActivity.this, "注册失败", Toast.LENGTH_SHORT).show();
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        initView();
        initEvent();
    }

    /**
     * 控件初始化
     */
    private void initView() {
        edtUsername = findViewById(R.id.edt_username);
        edtPassword = findViewById(R.id.edt_password);
    }

    /**
     * 事件监听
     */
    private void initEvent() {
        findViewById(R.id.btn_register).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //验证数据有效性
                String username = edtUsername.getText().toString();
                String password = edtPassword.getText().toString();

                if (username.trim().length() == 0) {
                    edtUsername.setError("用户名不能为空");
                    return;
                }

                if (password.trim().length() == 0) {
                    edtPassword.setError("密码不能为空");
                    return;
                }

                register();
            }
        });

    }

    /**
     * 用户注册
     */
    private void register() {

        new Thread(new Runnable() {
            @Override
            public void run() {

                Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.default_user_img, null);
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
                System.out.println("img64:---------------" + image_64);


                Map<String, Object> param = new HashMap<>();
                //传给服务器用户的初始化数据---用户名,密码,初始头像头像
                param.put(Config.REQUEST_PARAMETER_USERNAME, edtUsername.getText().toString());
                param.put(Config.REQUEST_PARAMETER_PASSWORD, edtPassword.getText().toString());
                param.put(Config.REQUEST_PARAMETER_UserIMG_BASE64, image_64);
                OkHttpUtil.post(Config.URL_REGISTER, param, handler);
            }
        }).start();
    }

}
