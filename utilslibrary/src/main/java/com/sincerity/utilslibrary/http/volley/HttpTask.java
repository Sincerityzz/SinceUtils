package com.sincerity.utilslibrary.http.volley;

import com.alibaba.fastjson.JSON;

import java.nio.charset.StandardCharsets;

/**
 * Created by Sincerity on 2019/4/26.
 * 描述：
 */
public class HttpTask<T> implements Runnable {
    private IHttpListener listener;
    private IHttpRequest request;

    public HttpTask(String Url, T param, IHttpRequest request, IHttpListener listener) {
        this.listener = listener;
        this.request = request;
        request.setUrl(Url);
        request.HttpCall(listener);
        if (param != null) {
            String s = JSON.toJSONString(param);
            request.setParameters(s.getBytes(StandardCharsets.UTF_8));
        }
    }
    @Override
    public void run() {
        request.execute();
    }
}
