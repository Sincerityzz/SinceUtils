package com.sincerity.sinceutils.common;

import android.app.Application;

import com.sincerity.framelibrary.http.OkHttpEngine;
import com.sincerity.framelibrary.skin.SkinManager;
import com.sincerity.utilslibrary.bug.FixDexManager;
import com.sincerity.utilslibrary.exception.ExceptionCrashHandler;
import com.sincerity.utilslibrary.httputils.HttpUtils;

/**
 * Created by Sincerity on 2019/8/30.
 * 描述：
 */
public class BaseApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        //设置全局异常捕获类
        ExceptionCrashHandler.getInstance().init(this);
        FixDexManager manager = new FixDexManager(this);
        new HttpUtils(this).init(new OkHttpEngine());
        SkinManager.Companion.getInstance().init(this);
        try {
            manager.loadFixDex();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
