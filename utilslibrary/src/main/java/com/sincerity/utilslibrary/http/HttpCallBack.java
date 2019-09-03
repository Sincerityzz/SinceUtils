package com.sincerity.utilslibrary.http;

/**
 * Created by Sincerity on 2019/9/3.
 * 描述：成功失败的回调
 */
public abstract class HttpCallBack<T> {
    /**
     * 成功回调
     *
     * @param resultJson 返回的Json数据
     * @param result     实体类
     */
    public abstract void onSuccess(String resultJson, T result);

    /**
     * 失败回调
     *
     * @param ex 异常
     */
    public abstract void onFail(Throwable ex);
}
