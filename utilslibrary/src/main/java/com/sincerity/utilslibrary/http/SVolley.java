package com.sincerity.utilslibrary.http;

/**
 * Created by Sincerity on 2019/4/26.
 * 描述：
 */
public class SVolley {
    /**
     * @param url          地址
     * @param requestInfo  请求参数
     * @param response     返回实体
     * @param dataListener 监听
     * @param <T>          参数的类型
     */
    public static <T> void sendJsonRequest(String url, T requestInfo, Class response, ResponseEntity /*回调响应数据接口*/dataListener) {
        sendJsonRequest(url, requestInfo, null, response, dataListener);
    }

    public static <T> void sendJsonRequest(String url, T requestInfo, ResponseEntity dataListener) {
        sendJsonRequest(url, requestInfo, null, null, dataListener);
    }

    public static <T> void sendJsonRequest(String url, T requestInfo, String method, ResponseEntity dataListener) {
        sendJsonRequest(url, requestInfo, method, null, dataListener);
    }

    public static <T> void sendJsonRequest(String url, T requestInfo, String method, Class response, ResponseEntity dataListener) {
        if (url == null) {
            new Throwable(new RuntimeException() + "URL can`t null");
        }
        if (response == null) {
            new Throwable(new Exception("请添加需要解析的实体类"));
        }
        ParseHttpRequest httpService = new ParseHttpRequest();
        IHttpListener httpListener = new HttpListener(response, dataListener);
        HttpTask<T> httpTask = new HttpTask(url, requestInfo, httpService, httpListener);
        ThreadPoolManager.Instance().execute(httpTask);
    }
}
