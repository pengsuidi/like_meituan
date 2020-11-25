package com.amaker.personalinfo.Fragment_NavigationUI.PersonCenter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.provider.MediaStore;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;


import com.alibaba.fastjson.JSONObject;
import com.amaker.personalinfo.Activities.FavouriteActivity;
import com.amaker.personalinfo.Activities.MyCommentActivity;
import com.amaker.personalinfo.R;
import com.amaker.personalinfo.entity.Result;
import com.amaker.personalinfo.entity.User;
import com.amaker.personalinfo.util.CommonUtil;
import com.amaker.personalinfo.util.Config;
import com.amaker.personalinfo.util.OkHttpUtil;
import com.amaker.personalinfo.util.ScreenUtils;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.util.HashMap;
import java.util.Map;

public class PersonCenterFragment extends Fragment {
    private ImageButton user_img;
    private Button become_seller,favourite,comment,history_orders;
    private Uri uri;
    private Bitmap bitmap,userBitmap,tmp;
    private String image_64;
    private TextView user_id;
    private byte[] datas;


    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_person_center, container, false);
        user_img = root.findViewById(R.id.user_img);
        become_seller = root.findViewById(R.id.become_seller);
        user_id = root.findViewById(R.id.user_id);
        favourite = root.findViewById(R.id.favourite);
        history_orders = root.findViewById(R.id.history_orders);
        comment = root.findViewById(R.id.comment);
        InitUI();
        setUserImg();


        return root;
    }

    private void setUserImg() {
        user_img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_PICK, null);
                intent.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*");
                startActivityForResult(intent, 2);
            }
        });

    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 2) {
            // 从相册返回的数据
            if (data != null) {
                // 得到图片的全路径
                uri = data.getData();
                System.out.println("uri:---------------------------" + uri);

            }
            //                Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.t1, null);
            try {
                bitmap = BitmapFactory.decodeStream(getActivity().getContentResolver().openInputStream(uri));
                tmp=bitmap;
                userBitmap=bitmap.createScaledBitmap(bitmap,150,150,true);
                userBitmap= CommonUtil.getRoundedCornerBitmap(userBitmap,30);
                user_img.setImageBitmap(userBitmap);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }

//                bitmap=getImageThumbnail(getRealPathFromURI(user_comment_Activity.this,uri),50,50);
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            //清空画图缓存否则下次获取图片时还是原图片
            if (null != bitmap) {
                //对图片进行压缩，100为不压缩，并写入字节流中
                bitmap.compress(Bitmap.CompressFormat.JPEG, 30, bos);
            }
            //获取图片的二进制
            byte[] compress_head_photo = bos.toByteArray();
            try {
                bos.close();
            } catch (Exception ex) {
                ex.printStackTrace();
            }
            //对二进制数组进行编码
            image_64 = Base64.encodeToString(compress_head_photo, Base64.DEFAULT);
            uploadUserImg(image_64,getActivity().getSharedPreferences("data",Context.MODE_PRIVATE).getString(Config.REQUEST_PARAMETER_USERID,""));
        }
    }

    private void uploadUserImg(String image_64,String userid) {
        Map<String,Object> map=new HashMap<>();
        map.put(Config.REQUEST_PARAMETER_UserIMG_BASE64,image_64);
        map.put(Config.REQUEST_PARAMETER_USERID,userid);
        OkHttpUtil.post(Config.URL_Upload_User_Img,map,newhandler);

    }


    private void InitUI() {
        //获取用户图片
        new Thread(new Runnable() {
            @Override
            public void run() {
                Map<String, Object> param = new HashMap<>();
                param.put(Config.REQUEST_PARAMETER_USERID, getContext()
                        .getSharedPreferences("data", Context.MODE_PRIVATE)
                        .getString(Config.REQUEST_PARAMETER_USER_ID, ""));

                OkHttpUtil.post(Config.URL_GetUserInfo, param, InitUserHandler);

            }
        }).start();
        comment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(getActivity(), MyCommentActivity.class);

                intent.putExtra("userimgbyte", datas);
                startActivity(intent);
            }
        });
        favourite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(getActivity(), FavouriteActivity.class);

                startActivity(intent);
            }
        });
        become_seller.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDialog();
            }
        });

    }

    private void showDialog() {
        View view = LayoutInflater.from(getActivity()).inflate(R.layout.dialog_become_seller,null,false);
        final AlertDialog dialog = new AlertDialog.Builder(getActivity()).setView(view).create();
        dialog.setCanceledOnTouchOutside(false);//调用这个方法时，按对话框以外的地方不起作用。按返回键还起作用
        Button btn_cancel_high_opion = view.findViewById(R.id.btn_cancel_opion);
        Button btn_agree_high_opion = view.findViewById(R.id.btn_agree_opion);

        btn_cancel_high_opion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                dialog.dismiss();
            }
        });

        btn_agree_high_opion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                                Map<String, Object> param = new HashMap<>();
                                param.put(Config.REQUEST_PARAMETER_USERID, getContext()
                                        .getSharedPreferences("data", Context.MODE_PRIVATE)
                                        .getString(Config.REQUEST_PARAMETER_USER_ID, ""));

                                OkHttpUtil.post(Config.URL_BecomeSeller, param, handler);
                dialog.dismiss();
            }
        });

        dialog.show();
        //此处设置位置窗体大小，我这里设置为了手机屏幕宽度的3/4  注意一定要在show方法调用后再写设置窗口大小的代码，否则不起效果会
        dialog.getWindow().setLayout((ScreenUtils.getScreenWidth(getActivity())/4*3), LinearLayout.LayoutParams.WRAP_CONTENT);
    }


    private Handler handler = new Handler(Looper.myLooper()) {
        @RequiresApi(api = Build.VERSION_CODES.O)
        @Override

        public void handleMessage(Message msg) {
            switch (msg.what) {
                case Config.STATUS_OK://网络请求成功，但响应需要根据实际来操作
                    Result result = JSONObject.parseObject(msg.obj.toString(), Result.class);
                    JSONObject object = (JSONObject) result.getData();
                    if (result.getCode() == 200) {
                        Toast.makeText(getActivity(), result.getMessage(), Toast.LENGTH_SHORT).show();
                    }


                    break;
                default:
                    break;
            }

        }
    };
    private Handler newhandler = new Handler(Looper.myLooper()) {
        @RequiresApi(api = Build.VERSION_CODES.O)
        @Override

        public void handleMessage(Message msg) {
            switch (msg.what) {
                case Config.STATUS_OK://网络请求成功，但响应需要根据实际来操作
                    Result result = JSONObject.parseObject(msg.obj.toString(), Result.class);
                    JSONObject object = (JSONObject) result.getData();
                    if (result.getCode() == 200) {
                        Toast.makeText(getActivity(), "已上传成功!", Toast.LENGTH_SHORT).show();
                    }


                    break;
                default:
                    break;
            }

        }
    };
    private Handler InitUserHandler = new Handler(Looper.myLooper()) {
        @RequiresApi(api = Build.VERSION_CODES.O)
        @Override

        public void handleMessage(Message msg) {
            switch (msg.what) {
                case Config.STATUS_OK://网络请求成功，但响应需要根据实际来操作
                    Result result = JSONObject.parseObject(msg.obj.toString(), Result.class);
                    JSONObject object = (JSONObject) result.getData();
                    User user = object.toJavaObject(User.class);
                    tmp = getBitmapFromByte(user.getUser_img());
                    datas=user.getUser_img();
                    userBitmap= Bitmap.createScaledBitmap(tmp,150,150,true);
                    userBitmap= CommonUtil.getRoundedCornerBitmap(userBitmap,30);
                    user_img.setImageBitmap(userBitmap);
                    user_id.setText(user.getUid().toString());

                    break;
                default:
                    break;
            }

        }
    };

    public Bitmap getBitmapFromByte(byte[] temp) {   //将二进制转化为bitmap
        if (temp != null) {
            Bitmap bitmap = BitmapFactory.decodeByteArray(temp, 0, temp.length);
            return bitmap;
        } else {
            return null;
        }
    }
}
