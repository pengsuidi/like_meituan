package com.amaker.personalinfo.Fragment;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.os.Parcelable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.amaker.personalinfo.Activities.ShopActivity;
import com.amaker.personalinfo.Activities.ShoppingcarActivity;
import com.amaker.personalinfo.R;
import com.amaker.personalinfo.adapter.FoodTypeMenuAdapter;
import com.amaker.personalinfo.adapter.FoodsAdapter;
import com.amaker.personalinfo.entity.Food_Menu;
import com.amaker.personalinfo.entity.Result;
import com.amaker.personalinfo.entity.Student;
import com.amaker.personalinfo.util.CommonUtil;
import com.amaker.personalinfo.util.Config;
import com.amaker.personalinfo.util.OkHttpUtil;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class menuFragment extends Fragment implements View.OnClickListener {
    private String food_type;
    SharedPreferences sharedPreferences;
    private RelativeLayout content;
    private Button to_shoppingcar;
    private TextView tmp_sum;
    private RecyclerView recycler_foods, food_type_menu;
    private float sum = 0;
    private float deliver_price = 15.0f;//起送值
    private RecyclerView.Adapter adapter, food_type_menu_adapter;
    private ArrayList<Food_Menu> datas = new ArrayList<>();
    private ArrayList<Food_Menu> FoodDatas = new ArrayList<>();//作为总的食物链表,不变
    private List<String> FoodTypes = new ArrayList<>();
    //标记，当前操作是查看列表还是搜索结果
    private String method = Config.REQUEST_VALUE_METHOD_LIST;
    private String lastString = null;
    private ArrayList<Food_Menu> list = new ArrayList<>();
    private ArrayList<Food_Menu> OrderedFoodList = new ArrayList<>();//在获取adapter传来的食物id后,从总表FoodDatas中增减食物
    private ArrayList<String> food_name_list = new ArrayList<>();
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
                        ArrayList<Food_Menu> foodMenus = (ArrayList<Food_Menu>) jsonArray.toJavaList(Food_Menu.class);
                        System.out.println("个数:" + foodMenus.size());
                        FoodDatas.clear();
                        datas.clear();
                        FoodTypes.clear();
                        //获取总的食物链表
                        FoodDatas.addAll(foodMenus);
                        //获取类型
                        for (int i = 0; i < foodMenus.size(); i++) {
                            FoodTypes.add(foodMenus.get(i).getFood_type());
                        }
                        //去除重复的类型
                        FoodTypes = CommonUtil.removeDuplicate(FoodTypes);
                        System.out.println("去除重复后FoodType_size:" + FoodTypes.size());
                        FoodTypes.add("全部");
                        //根据foodtype的不同来改变datas
                        for (int i = 0; i < foodMenus.size(); i++) {
                            if (food_type.contentEquals("全部")) {
                                datas.addAll(foodMenus);
                                break;
                            }
                            if (foodMenus.get(i).getFood_type().contentEquals(food_type)) {
                                datas.add(foodMenus.get(i));
                            }
                        }
                        content.setVisibility(View.VISIBLE);


                        adapter.notifyDataSetChanged();
                        //添加一个"全部"按钮

                        food_type_menu_adapter.notifyDataSetChanged();
                        break;
                    }

                default://网络请求失败

                    break;
            }
        }
    };
    private Handler AdapterHandler = new Handler(Looper.myLooper()) {
        @RequiresApi(api = Build.VERSION_CODES.O)
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case Config.STATUS_OK:
                    food_type = msg.getData().getString(Config.FOOD_TYPE);
                    datas.clear();
                    for (int i = 0; i < FoodDatas.size(); i++) {
                        if (food_type.contentEquals("全部")) {
                            datas.addAll(FoodDatas);
                            break;
                        }
                        if (FoodDatas.get(i).getFood_type().contentEquals(food_type)) {
                            datas.add(FoodDatas.get(i));
                        }
                    }

                    adapter.notifyDataSetChanged();

                    break;
                default://网络请求失败

                    break;
            }
        }
    };
    //从adapter中获取被添加和删除的食物信息
    private Handler getOrderedFoodHandler = new Handler(Looper.myLooper()) {
        @RequiresApi(api = Build.VERSION_CODES.O)
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case Config.ADD_FOOD://网络请求成功，但响应需要根据实际来操作
                    System.out.println("收到的name:" + (msg.obj.toString()));
                    for (int i = 0; i < FoodDatas.size(); i++) {
                        if (FoodDatas.get(i).getFood_name() == msg.obj.toString()) {
                            OrderedFoodList.add(FoodDatas.get(i));
                            sum += Float.parseFloat(FoodDatas.get(i).getFood_price());
                            tmp_sum.setText("¥" + sum);
                            if_deliver(deliver_price);
                            break;
                        }
                    }
                    System.out.println("增加后OrderedFoodList大小:" + OrderedFoodList.size());
                    break;
                case Config.DEL_FOOD://网络请求成功，但响应需要根据实际来操作
                    System.out.println("收到的name:" + (msg.obj.toString()));
                    for (int i = 0; i < OrderedFoodList.size(); i++) {
                        if (OrderedFoodList.get(i).getFood_name() == msg.obj.toString()) {
                            sum -= Float.parseFloat(OrderedFoodList.get(i).getFood_price());
                            OrderedFoodList.remove(i);
                            tmp_sum.setText("¥" + sum);
                            if_deliver(deliver_price);
                            break;
                        }
                    }
                    System.out.println("减小后OrderedFoodList大小:" + OrderedFoodList.size());
                    break;
                default://网络请求失败

                    break;
            }
        }
    };

    @SuppressLint("ResourceAsColor")
    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    private void if_deliver(Float deliver_price)//判断是否可以起送
    {
        if (sum >= deliver_price) {
            to_shoppingcar.setBackground(getResources().getDrawable(R.drawable.right_rounded_orange));
            to_shoppingcar.setText("去结算");
            to_shoppingcar.setTextColor(R.color.black);
            to_shoppingcar.setClickable(true);
        } else {
            to_shoppingcar.setBackground(getResources().getDrawable(R.drawable.right_rounded_black));
            to_shoppingcar.setText("¥" + deliver_price + "起送");
            to_shoppingcar.setTextColor(R.color.gray);

            to_shoppingcar.setClickable(false);
        }
    }

    private String get_shopid,get_shopname;
    private View view;

    public static menuFragment newInstance() {
        return new menuFragment();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);


    }


    /**
     * 网络请求电影列表数据
     */
    private void initDatas(String str) {
        Map<String, Object> params = new HashMap<>();
        params.put(Config.REQUEST_PARAMETER_SHOP_ID, str);
        OkHttpUtil.post(Config.URL_GetFoodInfoServlet, params, handler);

        //TODO 在数据加载过程中，防止用户操作，做弹框提示且不允许取消
    }


    private void initview(View view) {
        recycler_foods = view.findViewById(R.id.recycler_foods);
        food_type_menu = view.findViewById(R.id.food_type_menu);
        to_shoppingcar = view.findViewById(R.id.to_shoppingcar);
        content = view.findViewById(R.id.content);
        content.setVisibility(View.INVISIBLE);
        tmp_sum = view.findViewById(R.id.tmp_sum);
        to_shoppingcar.setOnClickListener(this);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.menu_fragment, container, false);
        initview(view);
        //获得shopid
        Bundle bundle = getArguments();
        get_shopid = bundle.getString(Config.REQUEST_PARAMETER_SHOP_ID);
        get_shopname = bundle.getString(Config.REQUEST_PARAMETER_SHOPNAME);


        //为 RecyclerView 做显示设置
        RecyclerView.LayoutManager right_layoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);//在布局右侧
        RecyclerView.LayoutManager left_layoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
        recycler_foods.setLayoutManager(right_layoutManager);
        food_type_menu.setLayoutManager(left_layoutManager);
        sharedPreferences = getContext().getSharedPreferences("data", Context.MODE_PRIVATE);
        adapter = new FoodsAdapter(datas, sharedPreferences, getArguments().getString(Config.REQUEST_PARAMETER_SHOPNAME, ""), getOrderedFoodHandler);
        food_type_menu_adapter = new FoodTypeMenuAdapter(FoodTypes, AdapterHandler);
        recycler_foods.setAdapter(adapter);
        food_type_menu.setAdapter(food_type_menu_adapter);
        //获得全部食物及类型
        initDatas(get_shopid);//shopid
        food_type = "全部";

        return view;
    }


    @Override
    public void onClick(View view) {


        switch (view.getId()) {

            case R.id.to_shoppingcar:

                Intent intent1 = new Intent(getActivity(), ShoppingcarActivity.class);

                for (int i = 0; i < OrderedFoodList.size(); i++) {
                    food_name_list.add(OrderedFoodList.get(i).getFood_name());
                }
                intent1.putStringArrayListExtra(Config.FOOD_NAME_LIST, food_name_list);
                intent1.putExtra(Config.REQUEST_PARAMETER_SHOP_ID, get_shopid);
                intent1.putExtra(Config.REQUEST_PARAMETER_SHOPNAME, get_shopname);
                System.out.println("menuFrag list size---------:" + OrderedFoodList.size());
                getActivity().startActivity(intent1);
                break;

        }
    }

    public Bitmap getBitmapFromByte(byte[] temp) {   //将二进制转化为bitmap
        if (temp != null) {
            Bitmap bitmap = BitmapFactory.decodeByteArray(temp, 0, temp.length);
            return bitmap;
        } else {
            return null;
        }
    }
}


