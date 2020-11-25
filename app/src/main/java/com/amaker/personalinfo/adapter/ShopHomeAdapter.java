package com.amaker.personalinfo.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.amaker.personalinfo.Activities.ShopActivity;
import com.amaker.personalinfo.R;
import com.amaker.personalinfo.entity.Shop;

import java.util.List;


public class ShopHomeAdapter extends RecyclerView.Adapter<ShopHomeAdapter.ViewHolder> {
    private List<Shop> mShopHomeList;
    private boolean isCalHeight=false;
    private View mItemView;
    private RecyclerView mRv;
    private Context context;

    static class ViewHolder extends RecyclerView.ViewHolder{
        View shopHomeView;
        ImageView shopHomeImage;
        TextView shopHomeName;
        TextView shopHomeIntroduction;
        public ViewHolder(@NonNull View view) {
            super(view);
            shopHomeView=view;
            shopHomeImage=(ImageView) view.findViewById(R.id.shop_image_home);
            shopHomeName=(TextView)view.findViewById(R.id.shop_name_home);
            shopHomeIntroduction=(TextView)view.findViewById(R.id.shop_introduction_home);
        }
    }

    public ShopHomeAdapter(List<Shop> ShopHomeList, RecyclerView recyclerView, Context context){
        mShopHomeList=ShopHomeList;
        mRv=recyclerView;
        this.context=context;
    }

    //TODO 跳转到商家
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        mItemView=LayoutInflater.from(parent.getContext()).inflate(R.layout.shop_home_item,parent,false);
        final ViewHolder holder=new ViewHolder(mItemView);
        holder.shopHomeView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int position=holder.getAdapterPosition();
                Shop shop=mShopHomeList.get(position);
                //Toast.makeText(v.getContext(),""+shop.getShop_id(),Toast.LENGTH_SHORT).show();
                Intent intent=new Intent(context, ShopActivity.class);
//                intent.putExtra(shop.getShop_name(),"shop_name");
                intent.putExtra("shop_name",shop.getShop_name());
                context.startActivity(intent);
            }
        });
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Shop shop=mShopHomeList.get(position);
        holder.shopHomeImage.setImageResource(shop.getShop_imageId());
        holder.shopHomeIntroduction.setText(shop.getShop_introduction());
        holder.shopHomeName.setText(shop.getShop_name());
        setRecyclerViewHeight();
    }

    @Override
    public int getItemCount() {
        return mShopHomeList.size();
    }

    //计算高度
    private void setRecyclerViewHeight(){
        //避免多次计算
        if(isCalHeight){
            return;
        }
        isCalHeight = true;
        //获取ItemView的高度
        RecyclerView.LayoutParams itemview_layoutParams = (RecyclerView.LayoutParams)mItemView.getLayoutParams();

        //获取ItemView的数量  除以2取整
        int itemCount = getItemCount()/2+getItemCount()%2;

        //两者相乘得出RecyclerView的高度
        int recyclerViewHeight = itemCount * itemview_layoutParams.height;

        //设置RecyclerView的高度
        LinearLayout.LayoutParams rvmview_ayoutParams = (LinearLayout.LayoutParams) mRv.getLayoutParams();
        rvmview_ayoutParams.height = recyclerViewHeight;
        mRv.setLayoutParams(rvmview_ayoutParams);
    }
}