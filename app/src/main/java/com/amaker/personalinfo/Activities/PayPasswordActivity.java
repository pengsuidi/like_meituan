package com.amaker.personalinfo.Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSONObject;
import com.amaker.personalinfo.R;
import com.amaker.personalinfo.entity.NicknameResult;
import com.amaker.personalinfo.entity.OldPasswordResult;
import com.amaker.personalinfo.util.Config;
import com.amaker.personalinfo.util.OkHttpUtil;

import java.util.HashMap;
import java.util.Map;

public class PayPasswordActivity extends AppCompatActivity {

    private EditText oldPayPassword, newPayPassword;
    private Button confirm;
    public boolean flag=true;
    public Handler handler = new Handler(Looper.myLooper()) {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case Config.STATUS_OK://网络请求成功，但响应需要根据实际来操作
                    // Result result = JSONObject.parseObject(msg.obj.toString(), Result.class);
                    OldPasswordResult oldPasswordResult=JSONObject.parseObject(msg.obj.toString(), OldPasswordResult.class);

                    //取出oldpassword作比较

                    if(oldPayPassword.getText().toString().equals(oldPasswordResult.getMessage())
                            &&!(newPayPassword.getText().toString().equals(oldPasswordResult.getMessage()))){
                        if (oldPasswordResult.getCode() == Config.STATUS_SUCCESS) {

                            //修改成功
                            oldPayPassword.setText(newPayPassword.getText().toString());
                            update();
                            UpdateSuccess();

                        } else {
                            //修改失败
                            UpdateFailure();
                        }

                    }else if(!oldPayPassword.getText().toString().equals(oldPasswordResult.getMessage())){
                        Toast.makeText(PayPasswordActivity.this, "原支付密码错误", Toast.LENGTH_SHORT).show();

                        // UpdateFailure();

                    }

                    break;
                default://登陆失败
                    UpdateFailure();
                    break;
            }

        }
    };


    private void UpdateSuccess() {
        Toast.makeText(PayPasswordActivity.this, "修改成功", Toast.LENGTH_SHORT).show();
        // startActivity(new Intent(PasswordActivity.this, BirthdayActivity.class));

        finish();
    }

    private void UpdateFailure() {
        Toast.makeText(PayPasswordActivity.this, "修改失败", Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_paypassword);


        initView();
        initEvent();


    }
    private void initView() {
        oldPayPassword = findViewById(R.id.OldPaypassword);
        newPayPassword = findViewById(R.id.NewPaypassword);

    }

    public void initEvent(){
        confirm=findViewById(R.id.updatePaypassword);

        confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //flag=true;
                String oldPaypassword = oldPayPassword.getText().toString();
                String newPaypassword = newPayPassword.getText().toString();

                if (oldPaypassword.trim().length() == 0) {
                    oldPayPassword.setError("密码不能为空");
                    return;
                }

                if (newPaypassword.trim().length() == 0) {
                    newPayPassword.setError("密码不能为空");
                    return;
                }
                //修改操作
                isUser();

             /*   if(!flag){
                update();
                }*/

            }

        });

    }


    private void isUser(){
        Map<String, Object> params = new HashMap<>();
        params.put("user_id", getSharedPreferences("data",MODE_PRIVATE).getString("userid",null));
        OkHttpUtil.post(Config.URL_SELECT_PAYPASSWORD, params, handler);

    }


    private void update(){
        Map<String, Object> params = new HashMap<>();
        params.put("user_id", getSharedPreferences("data",MODE_PRIVATE).getString("userid",null));
        params.put("pay_password", newPayPassword.getText().toString());

        OkHttpUtil.post(Config.URL_UPDATE_PAYPASSWORD, params, handler);


    }


    }
