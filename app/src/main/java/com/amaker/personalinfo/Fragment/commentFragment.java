package com.amaker.personalinfo.Fragment;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.amaker.personalinfo.R;
import com.amaker.personalinfo.adapter.ShopCommentsAdapter;
import com.amaker.personalinfo.entity.Result;
import com.amaker.personalinfo.entity.User;
import com.amaker.personalinfo.entity.UserComment;
import com.amaker.personalinfo.util.Config;
import com.amaker.personalinfo.util.OkHttpUtil;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * A simple {@link Fragment} subclass.
 * Use the {@link commentFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class commentFragment extends Fragment implements View.OnClickListener {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private Float good_comment = 4.0f, bad_comment = 2.0f;
    private String comment_type;
    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private Button all, good, bad;//全部,好评,差评按钮
    private TextView shop_grade;
    private List<UserComment> datas = new ArrayList<>();
    private List<UserComment> WholeDatas = new ArrayList<>();
    private RecyclerView.Adapter adapter;
    private RecyclerView commentView;
    private RatingBar shop_rating;

    private Handler handler = new Handler(Looper.myLooper()) {
        @RequiresApi(api = Build.VERSION_CODES.O)
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case Config.STATUS_OK://网络请求成功，但响应需要根据实际来操作
                    Result result = JSONObject.parseObject(msg.obj.toString(), Result.class);

                    if (result.getCode() == Config.STATUS_SUCCESS) {
                        //网络请求成功
                        System.out.println("data:" + result.getData());
                        System.out.println("result:" + result);
                        JSONArray jsonArray = (JSONArray) result.getData();
                        List<UserComment> comments = jsonArray.toJavaList(UserComment.class);
                        System.out.println("个数:" + comments.size());
                        //更新商家得分
                        Float sum = 0.0f;
                        for (int i = 0; i < comments.size(); i++) {
                            if (comments.get(i).getGrade() == null) {
                                break;
                            }
                            System.out.println("分数:" + Float.parseFloat(comments.get(i).getGrade()));
                            sum += Float.parseFloat(comments.get(i).getGrade());
                        }
                        if (comments.size() == 0) {

                        } else {
                            shop_grade.setText(String.valueOf(sum / comments.size()));
                            shop_rating.setRating(sum / comments.size());
                        }
                        shop_rating.setRating(sum / comments.size());
                        System.out.println("个数:" + comments.size());
                        datas.clear();
                        WholeDatas.clear();
                        //datas中只放构造Recyclerview需要的数据,即分别为"all","good","bad"时的数据,用以展示
                        datas.addAll(comments);//刚进入评价界面展示的数据为全部数据
                        WholeDatas.addAll(comments);//所有数据放入WholeDatas
                        //获取用户信息
                        for(int i=0;i<WholeDatas.size();i++)
                        {
                            Map<String, Object> param = new HashMap<>();
                            param.put(Config.REQUEST_PARAMETER_USERID, WholeDatas.get(i).getUser_id());

                            OkHttpUtil.post(Config.URL_GetUserInfo, param, GetUserHandler);
                        }

//                        adapter.notifyDataSetChanged();
                        break;
                    }

                default://网络请求失败
                    break;
            }
        }
    };
    private Handler GetUserHandler = new Handler(Looper.myLooper()) {
        @RequiresApi(api = Build.VERSION_CODES.O)
        @Override

        public void handleMessage(Message msg) {
            switch (msg.what) {
                case Config.STATUS_OK://网络请求成功，但响应需要根据实际来操作
                    Result result = JSONObject.parseObject(msg.obj.toString(), Result.class);
                    JSONObject object = (JSONObject) result.getData();
                    User user = object.toJavaObject(User.class);
                    for(int i=0;i<WholeDatas.size();i++){
                        if(WholeDatas.get(i).getUser_id().contentEquals(user.getUid().toString()))
                        {
                            WholeDatas.get(i).setUser_pic(user.getUser_img());
                        }
                    }
                    adapter.notifyDataSetChanged();
                    break;
                default:
                    break;
            }

        }
    };

    public commentFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment commentFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static commentFragment newInstance(String param1, String param2) {
        commentFragment fragment = new commentFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.all:
                comment_type = "all";
                datas.clear();
                datas.addAll(WholeDatas);
                adapter.notifyDataSetChanged();
                System.out.println("点击后大小:"+datas.size());

                all.setBackground(getResources().getDrawable(R.drawable.click_comment_style));
                good.setBackground(getResources().getDrawable(R.drawable.not_click_comment_style));
                bad.setBackground(getResources().getDrawable(R.drawable.not_click_comment_style));
                break;
            case R.id.good:
                comment_type = "good";
                datas.clear();
                for (int i = 0; i < WholeDatas.size(); i++) {
                    //datas中只放构造Recyclerview需要的数据,即分别为"all","good","bad"时的数据
                    if (Float.parseFloat(WholeDatas.get(i).getGrade()) >= good_comment) {
                        datas.add(WholeDatas.get(i));
                    }
                }
                System.out.println("点击后大小:"+datas.size());

                adapter.notifyDataSetChanged();
                good.setBackground(getResources().getDrawable(R.drawable.click_comment_style));
                all.setBackground(getResources().getDrawable(R.drawable.not_click_comment_style));
                bad.setBackground(getResources().getDrawable(R.drawable.not_click_comment_style));
                break;
            case R.id.bad:
                comment_type = "bad";
                datas.clear();
                for (int i = 0; i < WholeDatas.size(); i++) {
                    //datas中只放构造Recyclerview需要的数据,即分别为"all","good","bad"时的数据
                    if (Float.parseFloat(WholeDatas.get(i).getGrade()) <= bad_comment) {
                        datas.add(WholeDatas.get(i));
                    }
                }
                System.out.println("点击后大小:"+datas.size());
                adapter.notifyDataSetChanged();
                bad.setBackground(getResources().getDrawable(R.drawable.click_comment_style));
                good.setBackground(getResources().getDrawable(R.drawable.not_click_comment_style));
                all.setBackground(getResources().getDrawable(R.drawable.not_click_comment_style));
                break;
        }
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_comment, container, false);

        all = view.findViewById(R.id.all);
        good = view.findViewById(R.id.good);
        bad = view.findViewById(R.id.bad);
        shop_grade = view.findViewById(R.id.seller_grade);
        shop_rating = view.findViewById(R.id.seller_ratingBar);
        all.setOnClickListener(this);
        good.setOnClickListener(this);
        bad.setOnClickListener(this);
        commentView = view.findViewById(R.id.commentView);
        //为 RecyclerView 做显示设置
        @SuppressLint("WrongConstant")
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
        commentView.setLayoutManager(layoutManager);
        adapter = new ShopCommentsAdapter(datas);
        commentView.setAdapter(adapter);

        comment_type = "all";
        Map<String, Object> param = new HashMap<>();
        SharedPreferences sharedPreferences = getContext().getSharedPreferences("data", Context.MODE_PRIVATE);
        param.put(Config.REQUEST_PARAMETER_SHOP_ID, sharedPreferences.getString(Config.REQUEST_PARAMETER_SHOP_ID, ""));//暂时shopid固定为2
        OkHttpUtil.post(Config.URL_GET_COMMENT, param, handler);
        all.setBackground(getResources().getDrawable(R.drawable.click_comment_style));
        good.setBackground(getResources().getDrawable(R.drawable.not_click_comment_style));
        bad.setBackground(getResources().getDrawable(R.drawable.not_click_comment_style));
        return view;
    }


}
