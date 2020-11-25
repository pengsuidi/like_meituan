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
import android.widget.TextView;

import com.alibaba.fastjson.JSONObject;
import com.amaker.personalinfo.R;
import com.amaker.personalinfo.entity.BirthdayResult;
import com.amaker.personalinfo.util.Config;
import com.amaker.personalinfo.util.OkHttpUtil;

import java.util.HashMap;
import java.util.Map;

public class BirthdayActivity extends AppCompatActivity {

    private TextView show_birthday;
    private Button ubutton;
    private Handler handler = new Handler(Looper.myLooper()) {

        @Override
        public void handleMessage(Message msg) {

            //TODO 根据 msg.what 指令不同，执行不同的响应
            switch (msg.what) {
                case Config.STATUS_ERROR:
                 //   Log.e(Config.LOG_TAG_ERROR, tvContent.getText().toString());

                    show_birthday.setTextColor(getResources().getColor(R.color.colorAccent));
                    break;
                case Config.STATUS_OK:
                    Log.i(Config.LOG_TAG_INFO, show_birthday.getText().toString());

                    //将 JSON 格式字符串反转为 Result 响应规范对象
//                    Result result = (Result) JSONObject.parse((String) msg.obj);
                    BirthdayResult birthdayResult = JSONObject.parseObject(msg.obj.toString(), BirthdayResult.class);

                  //  String s=(String） result.getData().toString();
                    show_birthday.setText(birthdayResult.toString());
//                    result.getCode();
//                    result.getMessage();
//                    result.getData();


                    break;
                default:
                   // Log.w(Config.LOG_TAG_WARN, show_birthday.getText().toString());

                    show_birthday.setTextColor(getResources().getColor(R.color.colorAccent));
                    break;
            }
        }
    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_birthday);

        show_birthday=findViewById(R.id.show_birthday);
        ubutton=findViewById(R.id.ubirthday);

        Map<String, Object> params = new HashMap<>();
        params.put("uid", getSharedPreferences("data",MODE_PRIVATE).getString("userid",null));
//        params.put("uid", getSharedPreferences("data",MODE_PRIVATE).getString(Config.REQUEST_PARAMETER_USER_ID,null));

        //  params.put("password", "123456");

        OkHttpUtil.post(Config.SELECT_BIRTHDAY, params, handler);
        //  show_birthday.setText();

        ubutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(BirthdayActivity.this, UBirthdayActivity.class);
                startActivity(intent);
            }
        });


    }

    @Override
    protected void onRestart() {
        super.onRestart();
        Map<String, Object> params = new HashMap<>();
        params.put("uid", getSharedPreferences("data",MODE_PRIVATE).getString("userid",null));
//        params.put("uid", getSharedPreferences("data",MODE_PRIVATE).getString(Config.REQUEST_PARAMETER_USER_ID,null));

        //  params.put("password", "123456");

        OkHttpUtil.post(Config.SELECT_BIRTHDAY, params, handler);
    }
}
