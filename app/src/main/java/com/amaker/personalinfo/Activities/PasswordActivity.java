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
import com.amaker.personalinfo.entity.BirthdayResult;
import com.amaker.personalinfo.entity.NicknameResult;
import com.amaker.personalinfo.entity.OldPasswordResult;
import com.amaker.personalinfo.entity.Result;
import com.amaker.personalinfo.util.Config;
import com.amaker.personalinfo.util.OkHttpUtil;

import java.util.HashMap;
import java.util.Map;

public class PasswordActivity extends AppCompatActivity {

    private EditText oldPassword, newPassword;
    private Button confirm;
    public boolean flag=true;
    public Handler handler = new Handler(Looper.myLooper()) {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case Config.STATUS_OK://网络请求成功，但响应需要根据实际来操作
                   // Result result = JSONObject.parseObject(msg.obj.toString(), Result.class);
                    OldPasswordResult oldPasswordResult=JSONObject.parseObject(msg.obj.toString(), OldPasswordResult.class);
                 if(oldPassword.getText().toString().equals(oldPasswordResult.getMessage())
                         &&!(newPassword.getText().toString().equals(oldPasswordResult.getMessage()))){
                        if (oldPasswordResult.getCode() == Config.STATUS_SUCCESS) {

                            //修改成功
                            oldPassword.setText(newPassword.getText().toString());
                            update();
                            UpdateSuccess();

                        } else {
                            //登陆失败
                            UpdateFailure();
                        }

                    }else if(!oldPassword.getText().toString().equals(oldPasswordResult.getMessage())){
                        Toast.makeText(PasswordActivity.this, "原密码错误", Toast.LENGTH_SHORT).show();

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
        Toast.makeText(PasswordActivity.this, "修改成功", Toast.LENGTH_SHORT).show();
       // startActivity(new Intent(PasswordActivity.this, BirthdayActivity.class));

        finish();
    }

    private void UpdateFailure() {
        Toast.makeText(PasswordActivity.this, "修改失败", Toast.LENGTH_SHORT).show();
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_password);

        initView();
        initEvent();


    }
    /**
     * 控件初始化
     */
    private void initView() {
       oldPassword = findViewById(R.id.oldPassword);
       newPassword = findViewById(R.id.newPassword);

    }

    public void initEvent(){
        confirm=findViewById(R.id.updatePassword);

        confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //flag=true;
                String oldpassword = oldPassword.getText().toString();
                String newpassword = newPassword.getText().toString();

                if (oldpassword.trim().length() == 0) {
                    oldPassword.setError("密码不能为空");
                    return;
                }

                if (newpassword.trim().length() == 0) {
                    newPassword.setError("密码不能为空");
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
        params.put("uid", getSharedPreferences("data",MODE_PRIVATE).getString("userid",null));
        //        params.put("uid", getSharedPreferences("data",MODE_PRIVATE).getString(Config.REQUEST_PARAMETER_USER_ID,null));

        OkHttpUtil.post(Config.SELECT_PASSWORD, params, handler);

    }


    private void update(){
        Map<String, Object> params = new HashMap<>();
        params.put("uid", getSharedPreferences("data",MODE_PRIVATE).getString("userid",null));
       // params.put("uid", getSharedPreferences("data",MODE_PRIVATE).getString(Config.REQUEST_PARAMETER_USER_ID,null));
        params.put("upassword", newPassword.getText().toString());

        OkHttpUtil.post(Config.UPDATE_PASSWORD, params, handler);


    }

}
