package com.amaker.personalinfo.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.amaker.personalinfo.Activities.ShopActivity;
import com.amaker.personalinfo.R;
import com.amaker.personalinfo.entity.Shop;

import java.util.List;


public class ShopSearchAdapter extends RecyclerView.Adapter<ShopSearchAdapter.ViewHolder> {
    private List<Shop> mShopSearchList;
    private Context context;

    static class ViewHolder extends RecyclerView.ViewHolder{
        View shopSearchView;
        ImageView shopSearchImage;
        TextView shopSearchName;
        TextView shopSearchIntroduction;
        public ViewHolder(@NonNull View view) {
            super(view);
            shopSearchView=view;
            shopSearchImage=(ImageView) view.findViewById(R.id.shop_image_search);
            shopSearchName=(TextView)view.findViewById(R.id.shop_name_search);
            shopSearchIntroduction=(TextView)view.findViewById(R.id.shop_introduction_search);
        }
    }

    public ShopSearchAdapter(List<Shop> ShopSearchList, Context context){
        this.context=context;
        mShopSearchList=ShopSearchList;
    }

    @NonNull
    @Override

    //TODO 跳转到商家
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view =LayoutInflater.from(parent.getContext()).inflate(R.layout.shop_search_item,parent,false);
        final ViewHolder holder=new ViewHolder(view);
        holder.shopSearchView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int position=holder.getAdapterPosition();
                Shop shop=mShopSearchList.get(position);
                Toast.makeText(v.getContext(),""+shop.getShop_name(),Toast.LENGTH_SHORT).show();
                Intent intent=new Intent(context, ShopActivity.class);
                intent.putExtra("shop_name",shop.getShop_name());
                context.startActivity(intent);
            }
        });
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Shop shop=mShopSearchList.get(position);
        holder.shopSearchImage.setImageResource(shop.getShop_imageId());
        holder.shopSearchIntroduction.setText(shop.getShop_introduction());
        holder.shopSearchName.setText(shop.getShop_name());
    }

    @Override
    public int getItemCount() {
        return mShopSearchList.size();
    }

}
