package com.amaker.personalinfo.Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.alibaba.fastjson.JSONObject;
import com.amaker.personalinfo.R;
import com.amaker.personalinfo.entity.Result;
import com.amaker.personalinfo.util.Config;
import com.amaker.personalinfo.util.OkHttpUtil;

import java.util.HashMap;
import java.util.Map;

public class UNicknameActivity extends AppCompatActivity {
    private EditText edtNickname1, edtNickname2;
    private Button update;
    private Handler handler = new Handler(Looper.myLooper()) {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case Config.STATUS_OK://网络请求成功，但响应需要根据实际来操作
                    Result result = JSONObject.parseObject(msg.obj.toString(), Result.class);

                        UpdateSuccess();

                    break;
                default://登陆失败
                    UpdateFailure();
                    break;
            }

        }
    };


    private void UpdateSuccess() {
        Toast.makeText(UNicknameActivity.this, "修改成功", Toast.LENGTH_SHORT).show();
        finish();
    }


    private void UpdateFailure() {
        Toast.makeText(UNicknameActivity.this, "登陆失败", Toast.LENGTH_SHORT).show();
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_unickname);

        initView();
        initEvent();


    }
    /**
     * 控件初始化
     */
    private void initView() {
        edtNickname1 = findViewById(R.id.edtNickname1);
        edtNickname2 = findViewById(R.id.edtNickname2);

    }

    public void initEvent(){
        update=findViewById(R.id.updateNickname);

        update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String username = edtNickname1.getText().toString();
                String password = edtNickname2.getText().toString();

                if (username.trim().length() == 0) {
                    edtNickname1.setError("昵称不能为空");
                    return;
                }

                if (password.trim().length() == 0) {
                    edtNickname2.setError("昵称不能为空");
                    return;
                }
                //修改操作
                if(username.contentEquals(password))
                {
                    update();
                }
                else
                {
                    Toast.makeText(UNicknameActivity.this,"两次不一致!",Toast.LENGTH_SHORT).show();
                }


            }

        });

    }

    private void update(){
        Map<String, Object> params = new HashMap<>();
        params.put("uid", getSharedPreferences("data",MODE_PRIVATE).getString("userid",null));
        //        params.put("uid", getSharedPreferences("data",MODE_PRIVATE).getString(Config.REQUEST_PARAMETER_USER_ID,null));
        params.put("uname", edtNickname2.getText().toString());
        OkHttpUtil.post(Config.UPDATE_NICKNAME, params, handler);


    }
}
