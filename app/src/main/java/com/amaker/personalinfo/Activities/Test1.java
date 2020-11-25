package com.amaker.personalinfo.Activities;

import android.annotation.SuppressLint;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import com.amaker.personalinfo.R;
import com.amaker.personalinfo.adapter.MyPagerAdapter;

import java.util.ArrayList;

public class Test1 extends AppCompatActivity {

    private ViewPager view_pager;
    private ArrayList<View> aList;
    private MyPagerAdapter mAdapter;
    private ImageView image1,image2,image3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);
        view_pager =  findViewById(R.id.view_pager);
        image1 =  findViewById(R.id.image1);
        image2 =  findViewById(R.id.image2);
        image3 =  findViewById(R.id.image3);
        image1.setBackgroundResource(R.drawable.rounded_orange);
        image2.setBackgroundResource(R.drawable.rounded_grey);
        image3.setBackgroundResource(R.drawable.rounded_grey);
        aList = new ArrayList<View>();
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
}