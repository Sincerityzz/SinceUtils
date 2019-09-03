package com.sincerity.utilslibrary.http;

import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.x;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.Iterator;
import java.util.Map;

/**
 * Created by Sincerity on 2019/9/3.
 * 描述：XUtils引擎
 */
public class XUtilsEngine implements IHttpEngine {
    /**
     * Post请求
     *
     * @param url          Url
     * @param params       参数
     * @param httpCallBack 回调
     */
    @Override
    public void post(String url, Map<String, String> params, final HttpCallBack httpCallBack) {
//  封装url参数
        RequestParams requestParams = new RequestParams(url);
        Iterator<Map.Entry<String, String>> iterator = params.entrySet().iterator();
        while (iterator.hasNext()) {
            Map.Entry<String, String> entry = iterator.next();
            requestParams.addParameter(entry.getKey(), entry.getValue());
        }
        x.http().post(requestParams, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                // 获取泛型的类型
                Type genType = httpCallBack.getClass().getGenericSuperclass();
                Type[] params = ((ParameterizedType) genType).getActualTypeArguments();
                Class typeClass = (Class) params[0];
                httpCallBack.onSuccess(result, GsonUtils.jsonToObject(result, typeClass));
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                httpCallBack.onFail(ex);
            }

            @Override
            public void onCancelled(CancelledException cex) {

            }

            @Override
            public void onFinished() {

            }
        });
    }

    /**
     * get请求
     *
     * @param url          Url
     * @param params       参数
     * @param httpCallBack 回调
     */
    @Override
    public void get(String url, Map<String, String> params, final HttpCallBack httpCallBack) {
        RequestParams requestParams = new RequestParams(url);
        Iterator<Map.Entry<String, String>> iterator = params.entrySet().iterator();
        while (iterator.hasNext()) {
            Map.Entry<String, String> entry = (Map.Entry) iterator.next();
            requestParams.addParameter(entry.getKey(), entry.getValue());
        }
        x.http().get(requestParams, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                Type genType = httpCallBack.getClass().getGenericSuperclass();
                Type[] params = ((ParameterizedType) genType).getActualTypeArguments();
                Class typeClass = (Class) params[0];
                httpCallBack.onSuccess(result, GsonUtils.jsonToObject(result, typeClass));

            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                httpCallBack.onFail(ex);
            }

            @Override
            public void onCancelled(CancelledException cex) {

            }

            @Override
            public void onFinished() {

            }
        });
    }
}
