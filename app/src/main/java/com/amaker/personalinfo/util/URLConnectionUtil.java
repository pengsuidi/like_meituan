package com.amaker.personalinfo.util;

import android.os.Handler;
import android.os.Message;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

/**
 * HttpURLConnection 访问工具类
 */
public class URLConnectionUtil {

    /**
     * 封装 HttpUrlConnection 的 GET 请求
     *
     * @param urlStr
     * @param handler
     */
    public static void httpGet(final String urlStr, final Handler handler) {
        execute(Config.HTTP_GET, urlStr, null, handler);
    }


    public static void httpPost(final String urlStr, final Handler handler) {
        httpPost(urlStr, new HashMap<String, Object>(), handler);
    }

    /**
     * 封装 HttpUrlConnection 的 POST 请求
     *
     * @param urlStr
     * @param params
     * @param handler
     */
    public static void httpPost(final String urlStr, final Map<String, Object> params, final Handler handler) {
        execute(Config.HTTP_POST, urlStr, params, handler);
    }


    /**
     * 公共执行的方法
     *
     * @param method
     * @param urlStr
     * @param params
     * @param handler
     */
    private static void execute(final String method, final String urlStr, final Map<String, Object> params, final Handler handler) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                //1、获取 HTTPURLConnection 对象
                try {
                    //1.1 获取 URL 对象
                    //https://mail.sina.com.cn/?
                    URL url = new URL(urlStr);
                    //1.2 获取原始的 HTTPURLConnection 对象
                    HttpURLConnection connection = (HttpURLConnection) url.openConnection();

                    //1.3 设置
                    //设置链接超时时间
                    connection.setConnectTimeout(15000);
                    //设置读取超时时间
                    connection.setReadTimeout(15000);
                    //设置请求参数
                    if ("GET".equals(method))
                        connection.setRequestMethod("GET");
                    else if ("POST".equals(method))
                        connection.setRequestMethod("POST");
                    //添加Header
                    //connection.setRequestProperty("Connection", "Keep-Alive");
                    //接收输入流
                    connection.setDoInput(true);
                    //传递参数时需要开启
                    connection.setDoOutput(true);


                    //2、传递参数
                    //2.1 获取输出流对象（往服务器传递数据）
                    BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(connection.getOutputStream(), "UTF-8"));
                    //2.2 获取输出流输出的参数
                    if (params != null && params.size() > 0) {
                        StringBuilder sb = new StringBuilder();
                        //from=mail&to=email
                        for (Map.Entry<String, Object> entry : params.entrySet()) {
                            sb.append(entry.getKey())
                                    .append("=")
                                    .append(entry.getValue())
                                    .append("&");
                        }

                        String paramsStr = sb.substring(0, sb.length() - 1);
                        bw.write(paramsStr);
                        bw.flush();
                        bw.close();
                    }

                    //3、发起请求，获取响应
                    connection.connect();
                    BufferedReader br = new BufferedReader(new InputStreamReader(connection.getInputStream(), "UTF-8"));

                    int code = connection.getResponseCode();
                    String message = connection.getResponseMessage();

                    Message msg = handler.obtainMessage();
                    switch (code) {
                        case Config.STATUS_OK:
                            //TODO 正常响应处理
                            msg.what = Config.STATUS_OK;
                            msg.obj = getStringFromBR(br);
                            handler.sendMessage(msg);
                            break;
                        default:
                            //TODO 非正常响应处理
                            msg.what = code;
                            msg.obj = message;
                            handler.sendMessage(msg);
                            break;
                    }
                } catch (final IOException e) {
                    e.printStackTrace();

                    //TODO 请求异常处理
                    Message msg = handler.obtainMessage();
                    msg.what = Config.STATUS_ERROR;
                    msg.obj = e.getMessage();
                    handler.sendMessage(msg);
                }
            }
        }).start();
    }


    /**
     * 将 BufferedReader 中的数据流转换为字符串输出
     *
     * @param br
     * @return
     * @throws IOException
     */
    private static String getStringFromBR(BufferedReader br) throws IOException {
        //TODO IO 操作，读取数据
        StringBuilder sb = new StringBuilder();
        String line = br.readLine();

        while (line != null && line.length() > 0) {
            sb.append(line).append("\n");

            line = br.readLine();
        }


        return sb.toString();
    }


}
