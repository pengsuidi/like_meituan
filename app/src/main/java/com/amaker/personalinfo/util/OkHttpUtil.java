package com.amaker.personalinfo.util;

import android.os.Handler;
import android.os.Message;

import com.alibaba.fastjson.JSONObject;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class OkHttpUtil {

    /**
     * 封装 OkHttp 的 GET 请求
     *
     * @param urlStr
     * @param handler
     */
    public static void get(final String urlStr, final Handler handler) {
        enqueue(Config.HTTP_GET, urlStr, null, handler);
    }


    public static void post(final String urlStr, final Handler handler) {
        post(urlStr, new HashMap<String, Object>(), handler);
    }

    /**
     * 封装 OkHttp 的 POST 请求
     *
     * @param urlStr
     * @param params
     * @param handler
     */
    public static void post(final String urlStr, final Map<String, Object> params, final Handler handler) {
        enqueue(Config.HTTP_POST, urlStr, params == null ? new HashMap<String, Object>() : params, handler);
    }


    private static void enqueue(final String method, final String urlStr, final Map<String, Object> params, final Handler handler) {
        //使用默认模式构建 OkHttpClient
        OkHttpClient client = new OkHttpClient();

        Request.Builder builder = new Request.Builder()
                .url(urlStr);


        switch (method) {
            case Config.HTTP_GET:
                builder.get();
                break;
            case Config.HTTP_POST:
                //TODO 自己将此处的参数传递，使用 FormBody 包裹封装，Servlet 就可以接受了
                //JSON 格式数据传递，更多的是为 SpringMVC 准备的
               // builder.post(RequestBody.create(new JSONObject(params).toJSONString(), Config.MEDIA_TYPE_JSON));
                FormBody.Builder formBodyBuilder=new FormBody.Builder();
                for(Map.Entry<String,Object>entry:params.entrySet()){
                    formBodyBuilder.add(entry.getKey(),entry.getValue().toString());
                }
                builder.post(formBodyBuilder.build());

                break;
        }

        Request request = builder.build();

        client.newCall(request).enqueue(new Callback() {
            //网络请求异常处理，比如 IOException
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                Message msg = handler.obtainMessage();
                msg.what = Config.STATUS_ERROR;
                msg.obj = e.getMessage();
                handler.sendMessage(msg);
            }

            //服务器正常响应，无论响应码是 200 还是 302 或者 404 及 500
            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                //这是网络操作的响应码
                int code = response.code();
                Message msg = handler.obtainMessage();
                switch (code) {
                    case Config.STATUS_OK:
                        //TODO 正常响应处理
                        msg.what = Config.STATUS_OK;
                        msg.obj = response.body().string();
                        handler.sendMessage(msg);
                        break;
                    default:
                        //TODO 非正常响应处理
                        msg.what = code;
                        msg.obj = response.message();
                        handler.sendMessage(msg);
                        break;
                }
            }
        });
    }
}
