package com.amaker.personalinfo.adapter;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.RecyclerView;

import com.alibaba.fastjson.JSONObject;
import com.amaker.personalinfo.Activities.MyCommentActivity;
import com.amaker.personalinfo.Activities.ShopActivity;
import com.amaker.personalinfo.R;

import com.amaker.personalinfo.entity.Result;
import com.amaker.personalinfo.entity.Shop_Info;
import com.amaker.personalinfo.entity.User;
import com.amaker.personalinfo.entity.UserComment;
import com.amaker.personalinfo.util.CommonUtil;
import com.amaker.personalinfo.util.Config;
import com.amaker.personalinfo.util.OkHttpUtil;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.zip.Inflater;

public class UserCommentsAdapter extends RecyclerView.Adapter<UserCommentsAdapter.CommentsViewHolder> {
    private int choose_position;
    private List<UserComment> datas;
    private List<Shop_Info> ShopInfoList;
    private byte[] userimgbyte;
    private Context context;
    private Handler msg_handler=new Handler(Looper.myLooper());//用以与活动传消息
    private Handler handler = new Handler(Looper.myLooper()) {
        @RequiresApi(api = Build.VERSION_CODES.O)
        @Override

        public void handleMessage(Message msg) {
            switch (msg.what) {
                case Config.STATUS_OK://网络请求成功，但响应需要根据实际来操作
                    Result result = JSONObject.parseObject(msg.obj.toString(), Result.class);
                    System.out.println("adapter result:"+result);
                    if(result.getCode()==Config.STATUS_OK)
                    {
                        Toast.makeText(context,"删除成功!",Toast.LENGTH_SHORT).show();
                        //给MyCommentActivity发送消息,更新recyclerview
                        Message message = new Message();
                        message.what = Config.DELETE;
                        message.obj=choose_position;
                        msg_handler.sendMessage(message);

                    }
                    break;
                default:
                    break;
            }
        }
    };
    private Dialog bottomDialog;
    private Drawable drawable;

    public UserCommentsAdapter(List<UserComment> datas, List<Shop_Info> ShopInfoList,byte[] userimgbyte,Context context,Handler msg_handler) {
        this.datas = datas;
        this.ShopInfoList = ShopInfoList;
        this.userimgbyte = userimgbyte;
        this.context = context;
        this.msg_handler = msg_handler;
    }

    /**
     * 加载 ViewHolder 对象
     */
    @NonNull
    @Override
    public CommentsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        //此处的 布局文件.xml 是每条数据的展示效果布局样式图
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_user_comment, parent, false);
        return new CommentsViewHolder(view);
    }

    /**
     * TODO 绑定数据及事件
     */
    @Override
    public void onBindViewHolder(@NonNull final CommentsViewHolder holder, final int position) {
        final UserComment comment = datas.get(position);
        final Shop_Info shop_info = ShopInfoList.get(position);
        holder.user_id.setText(comment.getUser_id());
        System.out.println("user_id:" + comment.getUser_id());
        if (comment.getNew_comment() != null)
            holder.user_comment.setText(comment.getNew_comment());
        //设置店铺图片
            //左侧的商家图片
        Bitmap bitmap = CommonUtil.getBitmapFromByte(shop_info.getShop_image());
        if(bitmap!=null)
        {
            bitmap = Bitmap.createScaledBitmap(bitmap, 640, 640, true);
            bitmap=CommonUtil.getRoundedCornerBitmap(bitmap,20);
            drawable = new BitmapDrawable(bitmap);
            drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
        }else {
            drawable=null;
        }

            //右侧的>图片
        Drawable drawable1 = context.getResources().getDrawable(R.drawable.ic_jump);
        drawable1.setBounds(0, 0, drawable1.getMinimumWidth(), drawable1.getMinimumHeight());
        holder.to_shop.setCompoundDrawables(drawable, null, drawable1, null);
        holder.to_shop.setText(shop_info.getShop_name());
        holder.to_shop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(v.getContext(), ShopActivity.class);
                intent.putExtra(Config.REQUEST_PARAMETER_SHOP_ID,shop_info.getShop_id());
                v.getContext().startActivity(intent);
            }
        });
        if(userimgbyte!=null){
            Bitmap userbitmap=CommonUtil.getBitmapFromByte(userimgbyte);
            userbitmap=Bitmap.createScaledBitmap(userbitmap,180,180,false);
            userbitmap=CommonUtil.getRoundedCornerBitmap(userbitmap,40);
            holder.user_img.setImageBitmap(userbitmap);
        }
        //删除评论
        holder.delete_comment.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                choose_position=position;
                ShowDialog(v.getContext());
            }
        });
//        if(comment.getComment_img()!=null)
//        {
//            Bitmap tmp=getBitmapFromByte(comment.getComment_img());
//            if(tmp!=null)
//            {
//                tmp=Bitmap.createScaledBitmap(tmp,1100,600,true);
//                holder.comment_img.setImageBitmap(tmp);
//            }
//        }

        if (comment.getGrade() == null)
            holder.ratingBar.setRating(0);
        else
            holder.ratingBar.setRating(Float.parseFloat(comment.getGrade()));
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
        private ImageButton delete_comment;
        private Button to_shop;
        private TextView user_id, user_comment;
        private RatingBar ratingBar;
        private ImageView user_img;

        public CommentsViewHolder(@NonNull View itemView) {
            super(itemView);

            user_id = itemView.findViewById(R.id.user_id);
            user_comment = itemView.findViewById(R.id.user_comment);
            user_img = itemView.findViewById(R.id.user_img);
            ratingBar = itemView.findViewById(R.id.ratingbar);
            delete_comment = itemView.findViewById(R.id.delete_comment);
            to_shop = itemView.findViewById(R.id.to_shop);
//            comment_img = itemView.findViewById(R.id.comment_img);


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
    private void ShowDialog(Context context) {
        bottomDialog = new Dialog(context, R.style.BottomDialog);
        View contentView = LayoutInflater.from(context).inflate(R.layout.dialog_layout, null);
        bottomDialog.setContentView(contentView);
        ViewGroup.LayoutParams layoutParams = contentView.getLayoutParams();
        layoutParams.width = context.getResources().getDisplayMetrics().widthPixels;
        contentView.setLayoutParams(layoutParams);
        bottomDialog.getWindow().setGravity(Gravity.BOTTOM);
        bottomDialog.getWindow().setWindowAnimations(R.style.BottomDialog_Animation);
        bottomDialog.show();
        Button delete=contentView.findViewById(R.id.delete);
        Button cancel=contentView.findViewById(R.id.cancel);
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bottomDialog.dismiss();
            }
        });
        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //删除图片
                Map<String, Object> param = new HashMap<>();
                param.put(Config.REQUEST_PARAMETER_COMMENT_ADDR, datas.get(choose_position).getComment_img_addr());
                OkHttpUtil.post(Config.URL_DeleteComment, param, handler);
                bottomDialog.dismiss();
            }
        });
    }


}
