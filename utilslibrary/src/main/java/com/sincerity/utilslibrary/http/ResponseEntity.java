package com.sincerity.utilslibrary.http;

/**
 * Created by Sincerity on 2019/4/26.
 * 描述：
 */
public interface ResponseEntity {
    void onSuccess(Object object);

    void onFail(String errorString);
}
