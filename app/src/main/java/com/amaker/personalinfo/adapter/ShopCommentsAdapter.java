package com.amaker.personalinfo.adapter;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.amaker.personalinfo.R;

import com.amaker.personalinfo.entity.UserComment;
import com.amaker.personalinfo.util.CommonUtil;

import java.util.List;

public class ShopCommentsAdapter extends RecyclerView.Adapter<ShopCommentsAdapter.CommentsViewHolder> {

    private List<UserComment> datas;


    public ShopCommentsAdapter(List<UserComment> datas) {
        this.datas = datas;
    }

    /**
     * 加载 ViewHolder 对象
     */
    @NonNull
    @Override
    public CommentsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        //此处的 布局文件.xml 是每条数据的展示效果布局样式图
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_shop_comment, parent, false);
        return new CommentsViewHolder(view);
    }

    /**
     * TODO 绑定数据及事件
     */
    @Override
    public void onBindViewHolder(@NonNull final CommentsViewHolder holder, int position) {
        final UserComment comment = datas.get(position);
        holder.user_id.setText(comment.getUser_id());
        System.out.println("user_id:"+comment.getUser_id());
        holder.user_comment.setText(comment.getNew_comment());
        if(comment.getComment_img()!=null)
        {
            Bitmap tmp=getBitmapFromByte(comment.getComment_img());
            if(tmp!=null)
            {
                tmp=Bitmap.createScaledBitmap(tmp,1100,600,true);
                holder.comment_img.setImageBitmap(tmp);
            }


        }
        if(comment.getUser_pic()!=null)
        {
            Bitmap tmp=getBitmapFromByte(comment.getUser_pic());
            if(tmp!=null)
            {
                tmp=Bitmap.createScaledBitmap(tmp,120,120,true);
                tmp= CommonUtil.getRoundedCornerBitmap(tmp,100);
                holder.user_img.setImageBitmap(tmp);
            }


        }
        if(comment.getGrade()==null)
            holder.stars.setRating(0);
        else
            holder.stars.setRating(Float.parseFloat(comment.getGrade()));
    }

    /**
     * 告知 RecyclerView 子项（item） 的数量
     */
    @Override
    public int getItemCount() {
        return datas == null ? 0 : datas.size();
    }

    /**
     * ViewHolder 对象，是 RecyclerView 中对 ITEM 循环利用的一个机制
     */
    class CommentsViewHolder extends RecyclerView.ViewHolder {

        private TextView user_id, user_comment;
        private RatingBar stars;
        private ImageView comment_img,user_img;
        public CommentsViewHolder(@NonNull View itemView) {
            super(itemView);

            user_id = itemView.findViewById(R.id.user_id);
            user_comment = itemView.findViewById(R.id.comment);
            user_img = itemView.findViewById(R.id.user_img);
            stars = itemView.findViewById(R.id.stars);
            comment_img = itemView.findViewById(R.id.comment_img);


        }
    }
    public Bitmap getBitmapFromByte(byte[] temp){   //将二进制转化为bitmap
        if(temp != null){
            Bitmap bitmap = BitmapFactory.decodeByteArray(temp, 0, temp.length);
            return bitmap;
        }else{
            return null;
        }
    }

}
