package com.sincerity.utilslibrary.http;

import java.util.Map;

/**
 * Created by Sincerity on 2019/9/3.
 * 描述：引擎通用的请求方式
 */
public interface IHttpEngine {
    /**
     * post请求
     *
     * @param url
     * @param params
     * @param httpCallBack
     */
    void post(String url, Map<String, String> params, HttpCallBack httpCallBack);

    /**
     * get请求
     *
     * @param url
     * @param params
     * @param httpCallBack
     */
    void get(String url, Map<String, String> params, HttpCallBack httpCallBack);
}
