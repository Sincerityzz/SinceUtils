package com.sincerity.utilslibrary.http;

import android.os.Handler;
import android.os.Looper;

import com.alibaba.fastjson.JSON;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

/**
 * Created by Sincerity on 2019/4/26.
 * 描述：
 */
public class HttpListener<T> implements IHttpListener {
    //响应实体
    Class<T> responseClazz;
    //线程切换
    Handler handler = new Handler(Looper.getMainLooper());

    private ResponseEntity entity;
    private Object t;

    public HttpListener(Class<T> responseClazz, ResponseEntity entity) {
        this.responseClazz = responseClazz;
        this.entity = entity;
    }

    @Override
    public void onSuccess(InputStream inputStream) {

        String contentStr = getContentStr(inputStream);

        if (responseClazz != null) {
            t = JSON.parseObject(contentStr, responseClazz);
        } else {
            t = contentStr;
        }
        handler.post(new Runnable() {
            @Override
            public void run() {
                if (entity != null)
                    entity.onSuccess(t);
            }
        });
    }

    private String getContentStr(InputStream inputStream) {
        try {
            BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
            StringBuilder builder = new StringBuilder();
            String line = null;
            try {
                while ((line = reader.readLine()) != null) {
                    builder.append(line + "\n");
                }
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                try {
                    inputStream.close();
                    reader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                return builder.toString();
            }
        } catch (Exception e) {
        }
        return null;
    }

    @Override
    public void onFail(final String errorString) {
//        final String errorString = getContentStr(inputStream);
        handler.post(new Runnable() {
            @Override
            public void run() {
                if (entity != null) {
                    entity.onFail(errorString);
                }
            }
        });
    }
}
