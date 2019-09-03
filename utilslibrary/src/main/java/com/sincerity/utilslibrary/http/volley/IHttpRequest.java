package com.sincerity.utilslibrary.http.volley;

import java.io.InputStream;

/**
 * Created by Sincerity on 2019/4/26.
 * 描述：
 */
public interface IHttpRequest {

    /*设置请求地址*/
    void setUrl(String url);

    /*设置请求参数*/
    void setParameters(byte[] bytes);

    /*执行请求*/
    void execute();

    /*添加请求的监听*/
    void HttpCall(IHttpListener listener);

}

interface IHttpListener {
    void onSuccess(InputStream inputStream);

    void onFail(String errorString);
}
