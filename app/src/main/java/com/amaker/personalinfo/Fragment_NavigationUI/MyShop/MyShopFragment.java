package com.amaker.personalinfo.Fragment_NavigationUI.MyShop;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Typeface;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;


import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.amaker.personalinfo.Activities.UploadShopInfoActivity;
import com.amaker.personalinfo.R;
import com.amaker.personalinfo.adapter.MyShopAdapter;
import com.amaker.personalinfo.entity.Result;
import com.amaker.personalinfo.entity.Shop_Info;
import com.amaker.personalinfo.util.Config;
import com.amaker.personalinfo.util.OkHttpUtil;
import androidx.appcompat.widget.Toolbar;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import pl.droidsonroids.gif.GifImageView;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link MyShopFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class MyShopFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private RecyclerView.Adapter adapter;
    private Typeface typeface;
    private Toolbar toolbar;
    private GifImageView gifView;
    private List<Shop_Info> datas = new ArrayList<>();
    private RecyclerView MyShopRecyclerView;
    public MyShopFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment MyShopFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static MyShopFragment newInstance(String param1, String param2) {
        MyShopFragment fragment = new MyShopFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_my_shop, container, false);
        toolbar=view.findViewById(R.id.my_shop_toolbar);
        MyShopRecyclerView=view.findViewById(R.id.MyShop_Recyclerview);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
        MyShopRecyclerView.setLayoutManager(layoutManager);
        //把数据,图片,字体传进去
        typeface=Typeface.createFromAsset(getContext().getAssets(), "fonts/ms_vista.ttf");

        adapter = new MyShopAdapter(datas,typeface);
        MyShopRecyclerView.setAdapter(adapter);
        new Thread(new Runnable() {
            @Override
            public void run() {
                Map<String, Object> param = new HashMap<>();
                param.put(Config.REQUEST_PARAMETER_USERID, getContext()
                        .getSharedPreferences("data", Context.MODE_PRIVATE)
                        .getString(Config.REQUEST_PARAMETER_USER_ID, ""));

                OkHttpUtil.post(Config.URL_GetShopInfoByUid, param, UpdateUIhandler);
            }
        }).start();//获得自己的商店信息
        toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem menuItem) {
                switch (menuItem.getItemId())
                {
                    case R.id.to_upload_shop_info:
                        startActivity(new Intent(getActivity(), UploadShopInfoActivity.class));
                        break;
                }
                return false;
            }
        });
        //设置动画
        gifView =  view.findViewById(R.id.gif_view);
        gifView.setImageResource(R.drawable.loading);
        gifView.setStateListAnimator(null);
        gifView.setVisibility(View.VISIBLE);
        return view;
    }


    private Handler UpdateUIhandler = new Handler(Looper.myLooper()) {
        @RequiresApi(api = Build.VERSION_CODES.O)
        @Override
        public void handleMessage(Message msg) {//构建RecyclerView,
            switch (msg.what) {
                case Config.STATUS_OK://网络请求成功，但响应需要根据实际来操作
                    Result result = JSONObject.parseObject(msg.obj.toString(), Result.class);
                    JSONArray object = (JSONArray) result.getData();
                    System.out.println("SHopINFO:"+result);
                    List<Shop_Info> shopInfoList = object.toJavaList(Shop_Info.class);
                    System.out.println("size:"+shopInfoList.size());

                    datas.clear();
                    String LastShopName="";//用来判断是否加入了重复的店铺
                    for(int i=0;i<shopInfoList.size();i++)
                    {
                        if(!shopInfoList.get(i).getShop_name().contentEquals(LastShopName))
                        {

                            datas.add(shopInfoList.get(i));
                            LastShopName=shopInfoList.get(i).getShop_name();
                        }
                    }
                    gifView.setVisibility(View.INVISIBLE);
                    adapter.notifyDataSetChanged();

                    break;


                default://登陆失败
                    break;
            }

        }
    };



}
