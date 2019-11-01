package com.sincerity.sinceutils;

import android.app.Application;

import com.sincerity.utilslibrary.ExceptionCrashHandler;
import com.sincerity.utilslibrary.bugsince.FixDexManager;

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
        FixDexManager manager=new FixDexManager(this);
        try {
            manager.loadFixDex();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
