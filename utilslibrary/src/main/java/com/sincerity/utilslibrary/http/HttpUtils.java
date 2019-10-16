package com.sincerity.utilslibrary.http;

import android.os.Handler;

import java.util.Map;

/**
 * Created by Sincerity on 2019/9/3.
 * 描述：成功失败的回调
 */

public class HttpUtils {

    private IHttpEngine httpEngine;

    public static Handler handler = new Handler();

    public HttpUtils() {

        //这里用来切换引擎
        httpEngine = new XUtilsEngine();
//        httpEngine = new OkHttpEngine();
    }


    /**
     * post请求
     * @param url
     * @param params
     * @param httpCallBack
     */
    public void post(String url , Map<String,String> params , HttpCallBack httpCallBack){
        httpEngine.post(url , params ,httpCallBack);
    }

    /**
     * get请求
     * @param url
     * @param params
     * @param httpCallBack
     */
    public void get(String url , Map<String,String> params , HttpCallBack httpCallBack){
        httpEngine.get(url , params ,httpCallBack);
    }
}
