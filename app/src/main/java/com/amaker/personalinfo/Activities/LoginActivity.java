package com.amaker.personalinfo.Activities;

import android.animation.Animator;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.view.View;
import android.view.ViewAnimationUtils;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.alibaba.fastjson.JSONObject;
import com.amaker.personalinfo.MyUI.JumpButton;
import com.amaker.personalinfo.R;
import com.amaker.personalinfo.entity.Result;
import com.amaker.personalinfo.util.Config;
import com.amaker.personalinfo.util.OkHttpUtil;
import com.amaker.personalinfo.util.PreferenceUtil;

import java.util.HashMap;
import java.util.Map;

@RequiresApi(api = Build.VERSION_CODES.M)

public class LoginActivity extends AppCompatActivity {

    private EditText edtUsername, edtPassword;
    private CheckBox checkRemember, checkAuoLogin;
    private JumpButton loginBtn;
    private LinearLayout content;
    private Animator animator;
    private Handler postHandler=new Handler();
    private Toolbar toolbar;
    private Handler handler = new Handler(Looper.myLooper()) {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case Config.STATUS_OK://网络请求成功，但响应需要根据实际来操作
                    Result result = JSONObject.parseObject(msg.obj.toString(), Result.class);

                    if (result.getCode() == Config.STATUS_SUCCESS) {
                        SharedPreferences mySharedPreferences = getSharedPreferences("data",
                                LoginActivity.MODE_PRIVATE);
                        SharedPreferences.Editor user_id = mySharedPreferences.edit();
                        System.out.println(result.getMessage());
                        user_id.putString(Config.REQUEST_PARAMETER_USERID, result.getMessage());
                        user_id.commit();
                        //查看是否为商家
                        Map<String, Object> params = new HashMap<>();
                        params.put(Config.REQUEST_PARAMETER_USERID, result.getMessage());
                        OkHttpUtil.post(Config.URL_IfSeller, params, judge_seller_handler);
                        //登陆成功

                    } else {
                        //登陆失败
                        loginFailure();
                    }
                    break;
                default://登陆失败
                    loginFailure();
                    break;
            }

        }
    };
    private Handler judge_seller_handler = new Handler(Looper.myLooper()) {//判断是否是商家
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case Config.STATUS_OK://是商家
                    Result result = JSONObject.parseObject(msg.obj.toString(), Result.class);
                    if (result.getCode() == 200) {
                        Intent intent1 = new Intent(LoginActivity.this, MainNavigationActivity.class);
                        intent1.putExtra(Config.IF_Seller, true);
                        System.out.println("是商家!!");
                        loginSuccess(intent1);
                        break;
                    } else  //不是商家
                    {
                        Intent intent2 = new Intent(LoginActivity.this, MainNavigationActivity.class);
                        intent2.putExtra(Config.IF_Seller, false);
                        System.out.println("不是商家!!");
                        loginSuccess(intent2);
                        break;
                    }


                default:


                    break;
            }

        }
    };

    private void loginSuccess(Intent intent) {
        Toast.makeText(LoginActivity.this, "登陆成功", Toast.LENGTH_SHORT).show();
        //执行动画部分的按钮放大以及跳转
        gotoNew(intent);
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    private void gotoNew(final Intent intent) {
        loginBtn.gotoNew();

        int xc = (loginBtn.getLeft() + loginBtn.getRight()) / 2;
        int yc = (loginBtn.getTop() + loginBtn.getBottom()) / 2;
        animator = ViewAnimationUtils.createCircularReveal(content, xc, yc, 0, 1111);
        animator.setDuration(300);
        animator.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        startActivity(intent);
                        overridePendingTransition(R.anim.anim_in, R.anim.anim_out);

                    }
                }, 200);
            }

            @Override
            public void onAnimationEnd(Animator animation) {

            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        });
        animator.start();
//        content.getBackground().setAlpha(255);
    }

    private void loginFailure() {
        Toast.makeText(LoginActivity.this, "登陆失败", Toast.LENGTH_SHORT).show();
        loginBtn.regainBackground();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        initView();
        initEvent();
        initBind();
    }

    private void initView() {
        edtUsername = findViewById(R.id.edt_username);
        edtPassword = findViewById(R.id.edt_password);

        checkRemember = findViewById(R.id.check_remember);
        checkAuoLogin = findViewById(R.id.check_auto_login);
        toolbar = findViewById(R.id.toolbar);

        loginBtn = findViewById(R.id.btn_login);
        content = findViewById(R.id.content);
//        content.getBackground().setAlpha(0);//将背景设置为透明

    }

    /**
     * 事件监听
     */
    private void initEvent() {
        checkRemember.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (!b) {
                    checkAuoLogin.setChecked(false);
                }
            }
        });

        checkAuoLogin.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b) {
                    checkRemember.setChecked(true);
                }
            }
        });

        loginBtn.setOnClickListener(new View.OnClickListener() {
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

                if (checkRemember.isChecked()) {
                    PreferenceUtil.putBoolean(LoginActivity.this, Config.SETTTING_REMEMBER, true);
                    PreferenceUtil.putString(LoginActivity.this, Config.DATA_USERNAME, username);
                    PreferenceUtil.putString(LoginActivity.this, Config.DATA_PASSWORD, password);

                    if (checkAuoLogin.isChecked()) {
                        PreferenceUtil.putBoolean(LoginActivity.this, Config.SETTTING_AUTO_LOGIN, true);
                    } else {
                        PreferenceUtil.putBoolean(LoginActivity.this, Config.SETTTING_AUTO_LOGIN, false);
                    }
                } else {
                    PreferenceUtil.putBoolean(LoginActivity.this, Config.SETTTING_REMEMBER, false);
                    PreferenceUtil.putBoolean(LoginActivity.this, Config.SETTTING_AUTO_LOGIN, false);
                }

                //登录操作
                login();
            }
        });

        findViewById(R.id.btn_to_register).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(LoginActivity.this, RegisterActivity.class));
            }
        });
    }

    /*
    登陆
     */
    @RequiresApi(api = Build.VERSION_CODES.M)
    private void initBind() {
        if (PreferenceUtil.getBoolean(this, Config.SETTTING_REMEMBER)) {
            checkRemember.setChecked(true);

            edtUsername.setText(PreferenceUtil.getString(this, Config.DATA_USERNAME));
            edtPassword.setText(PreferenceUtil.getString(this, Config.DATA_PASSWORD));

            if (PreferenceUtil.getBoolean(this, Config.SETTTING_AUTO_LOGIN)) {
                checkAuoLogin.setChecked(true);

                login();
            }
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    private void login() {
        //执行动画----矩形收缩以及圆线转动(表示等待)
        loginBtn.startAnim();
        postHandler.postDelayed(new Runnable() {
                    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
                    @Override
                    public void run() {

                        //跳转
                        Map<String, Object> param = new HashMap<>();
                        param.put(Config.REQUEST_PARAMETER_USERNAME, edtUsername.getText().toString());
                        param.put(Config.REQUEST_PARAMETER_PASSWORD, edtPassword.getText().toString());

                        OkHttpUtil.post(Config.URL_LOGIN, param, handler);
                    }
                },3000);



    }

    @Override
    protected void onStop() {
        super.onStop();
//        animator.cancel();
//        content.getBackground().setAlpha(0);
        loginBtn.regainBackground();

    }
}
