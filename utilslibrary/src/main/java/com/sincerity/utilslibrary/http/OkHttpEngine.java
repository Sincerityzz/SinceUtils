package com.sincerity.utilslibrary.http;

import java.io.IOException;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.Iterator;
import java.util.Map;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by Sincerity on 2019/9/3.
 * 描述：OkHttp引擎
 */
public class OkHttpEngine implements IHttpEngine {
    @Override
    public void post(String url, Map<String, String> params, final HttpCallBack httpCallBack) {
        FormBody.Builder body = new FormBody.Builder();
        if (params != null) {
            Iterator<Map.Entry<String, String>> iterator = params.entrySet().iterator();
            while (iterator.hasNext()) {
                Map.Entry<String, String> entry = iterator.next();
                body.add(entry.getKey(), entry.getValue());
            }
        }

        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder()
                .url(url)
                .post(body.build())
                .build();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                httpCallBack.onFail(e);
            }

            @Override
            public void onResponse(Call call, final Response response) throws IOException {

                // 获取泛型的类型
                Type genType = httpCallBack.getClass().getGenericSuperclass();
                Type[] params = ((ParameterizedType) genType).getActualTypeArguments();
                final Class typeClass = (Class) params[0];

                final String result = response.body().string();

                HttpUtils.handler.post(new Runnable() {
                    @Override
                    public void run() {
                        // 把它弄到主线程中
                        httpCallBack.onSuccess(result, GsonUtils.jsonToObject(result, typeClass));
                    }
                });

            }
        });
    }

    @Override
    public void get(String url, Map<String, String> params, final HttpCallBack httpCallBack) {
        FormBody.Builder body = new FormBody.Builder();
        for (Map.Entry<String, String> entry : params.entrySet()) {
            body.add(entry.getKey(), entry.getValue());
        }
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder()
                .url(url)
                .get()
                .build();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                httpCallBack.onFail(e);
            }

            @Override
            public void onResponse(Call call, final Response response) throws IOException {

                // 获取泛型的类型
                Type genType = httpCallBack.getClass().getGenericSuperclass();
                Type[] params = ((ParameterizedType) genType).getActualTypeArguments();
                final Class typeClass = (Class) params[0];

                final String result = response.body().string();

                HttpUtils.handler.post(new Runnable() {
                    @Override
                    public void run() {
                        // 把它弄到主线程中
                        httpCallBack.onSuccess(result, GsonUtils.jsonToObject(result, typeClass));
                    }
                });
            }
        });
    }
}
