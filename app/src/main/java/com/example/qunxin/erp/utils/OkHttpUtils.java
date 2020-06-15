package com.example.qunxin.erp.utils;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * Author：  wls-gcc
 * Date on： 2019\1\9 15:16
 * Version： v1.0
 */
public class OkHttpUtils {

    /**
     * 获取类名
     */
    private final String TAG = OkHttpUtils.class.getSimpleName();

    /**
     * mdiatype 这个需要和服务端保持一致
     */
    private final MediaType MEDIA_TYPE_JSON = MediaType.parse("application/x-www-form-urlencoded; charset=utf-8");

    //  okHttp post异步请求

    public void post(String url, String jsonStr) {
        MediaType mediaType = MediaType.parse("application/json; charset=utf-8");//"类型,字节码"

        //1.创建OkHttpClient对象
        OkHttpClient okHttpClient = new OkHttpClient();
        //2.通过RequestBody.create 创建requestBody对象
        RequestBody requestBody = RequestBody.create(mediaType, jsonStr);
        //3.创建Request对象，设置URL地址，将RequestBody作为post方法的参数传入
        final Request request = new Request.Builder().url(url).post(requestBody).build();
        //4.创建一个call对象,参数就是Request请求对象
        Call call = okHttpClient.newCall(request);
        //5.请求加入调度,重写回调方法
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                System.out.println("请求错误");
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                System.out.println("请求成功");
                System.out.println(response.body().string());

            }
        });
    }
}
