package com.amaker.personalinfo.Fragment_NavigationUI.MainPage;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.amaker.personalinfo.Activities.SearchActivity;
import com.amaker.personalinfo.Activities.ShopActivity;
import com.amaker.personalinfo.Activities.Test1;
import com.amaker.personalinfo.Listener.AutoLoadListener;
import com.amaker.personalinfo.R;
import com.amaker.personalinfo.adapter.MainShopListAdapter;
import com.amaker.personalinfo.adapter.MyPagerAdapter;
import com.amaker.personalinfo.entity.Result;
import com.amaker.personalinfo.entity.Shop_Info;
import com.amaker.personalinfo.util.Config;
import com.amaker.personalinfo.util.OkHttpUtil;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import pl.droidsonroids.gif.GifImageView;


public class MainPageFragment extends Fragment {

//    private DashboardViewModel dashboardViewModel;
private List<Shop_Info> ShopInfoList=new ArrayList<>();
    private GridView mGridView;
    private MainShopListAdapter adapter;
    private GifImageView gifView;
    private Button to_search_btn;
    private int shopid=0;
    private Handler handler=new Handler(Looper.myLooper()){
        @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
        @Override
        public void handleMessage(@NonNull Message msg) {
            super.handleMessage(msg);
            switch (msg.what){
                case Config.STATUS_OK:
                    Result result = JSONObject.parseObject(msg.obj.toString(), Result.class);
                    System.out.println("result:"+result);
                    JSONArray jsonArray=(JSONArray) result.getData();
                    List<Shop_Info> datas=jsonArray.toJavaList(Shop_Info.class);
                    ShopInfoList.addAll(datas);
                    ShopInfoList.addAll(datas);
                    ShopInfoList.addAll(datas);
                    ShopInfoList.addAll(datas);//多弄几个不然太空了
                    System.out.println("shoplist:"+ShopInfoList.size());
                    adapter.notifyDataSetChanged();
                    gifView.setVisibility(View.INVISIBLE);
                    break;
                default:
                    break;

            }
        }
    };
    private Handler newhandler=new Handler(Looper.myLooper()){
        @Override
        public void handleMessage(@NonNull Message msg) {
            super.handleMessage(msg);
            switch (msg.what){
                case Config.STATUS_OK:
                    Result result = JSONObject.parseObject(msg.obj.toString(), Result.class);
                    System.out.println("result:"+result);
                    JSONArray jsonArray=(JSONArray) result.getData();
                    List<Shop_Info> datas=jsonArray.toJavaList(Shop_Info.class);
                    ShopInfoList.addAll(datas);
                    System.out.println("shoplist:"+ShopInfoList.size());
                    adapter.notifyDataSetChanged();
                    gifView.setVisibility(View.INVISIBLE);
                    break;
                default:
                    break;

            }
        }
    };
    private AutoLoadListener autoLoadListener;
    private View root,loading;
    private ViewPager view_pager;
    private ArrayList<View> aList;
    private MyPagerAdapter mAdapter;
    private ImageView image1,image2,image3;
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        root = inflater.inflate(R.layout.fragment_main_page, container, false);
        initView(root);
        Map<String, Object> param = new HashMap<>();
        OkHttpUtil.post(Config.URL_GetRandomShopServlet, param, handler);
        return root;
    }
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    private void initView(View view) {
        mGridView =  view.findViewById(R.id.gridview_shop);
        to_search_btn =  view.findViewById(R.id.to_search_btn);
        gifView =  view.findViewById(R.id.gif_view);
        gifView.setImageResource(R.drawable.loading);
        gifView.setStateListAnimator(null);
        gifView.setVisibility(View.VISIBLE);

        adapter = new MainShopListAdapter(getActivity(),ShopInfoList);
        autoLoadListener= new AutoLoadListener(callBack);
        mGridView.setOnScrollListener(autoLoadListener);
        mGridView.setAdapter(adapter);
        //设置搜索跳转
        to_search_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(), SearchActivity.class));
            }
        });
        //设置ViewPager
        view_pager =  view.findViewById(R.id.view_pager);
        image1 =  view.findViewById(R.id.image1);
        image2 =  view.findViewById(R.id.image2);
        image3 =  view.findViewById(R.id.image3);
        image1.setBackgroundResource(R.drawable.rounded_orange);
        image2.setBackgroundResource(R.drawable.rounded_grey);
        image3.setBackgroundResource(R.drawable.rounded_grey);
        aList = new ArrayList<>();
        LayoutInflater li = getLayoutInflater();
        aList.add(li.inflate(R.layout.view_one, null, false));
        aList.add(li.inflate(R.layout.view_two, null, false));
        aList.add(li.inflate(R.layout.view_three, null, false));
        mAdapter = new MyPagerAdapter(aList);
        view_pager.setAdapter(mAdapter);
        view_pager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
            @SuppressLint("ResourceAsColor")
            @Override
            public void onPageSelected(int position) {
                System.out.println("position:"+position);
                switch (position) {
                    case 0:
                        image1.setBackgroundResource(R.drawable.rounded_orange);
                        image2.setBackgroundResource(R.drawable.rounded_grey);
                        image3.setBackgroundResource(R.drawable.rounded_grey);
                        System.out.println("nowposition:0");
                        break;
                    case 1:
                        image1.setBackgroundResource(R.drawable.rounded_grey);
                        image2.setBackgroundResource(R.drawable.rounded_orange);
                        image3.setBackgroundResource(R.drawable.rounded_grey);

                        System.out.println("nowposition:1");
                        break;
                    case 2:
                        image1.setBackgroundResource(R.drawable.rounded_grey);
                        image2.setBackgroundResource(R.drawable.rounded_grey);
                        image3.setBackgroundResource(R.drawable.rounded_orange);

                        System.out.println("nowposition:2");
                        break;
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    private void update()//上拉时执行加载
    {
        Map<String, Object> param = new HashMap<>();
        OkHttpUtil.post(Config.URL_GetRandomShopServlet, param, newhandler);
        gifView.setVisibility(View.VISIBLE);
        //防止再次滑动,注销监听器
    }
    AutoLoadListener.AutoLoadCallBack callBack = new AutoLoadListener.AutoLoadCallBack() {

        public void execute() {
            System.out.println("到底部");
            Toast.makeText(getActivity(),"已经拖动至底部",Toast.LENGTH_SHORT).show();
            update();
        }

    };

}
