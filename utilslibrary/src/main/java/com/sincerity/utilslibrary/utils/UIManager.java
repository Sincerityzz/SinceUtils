package com.sincerity.utilslibrary.utils;

import android.content.Context;
import android.util.DisplayMetrics;
import android.view.WindowManager;

import java.lang.reflect.Field;

/**
 * Created by Sincerity on 2019/12/19.
 * 描述： 屏幕适配方案
 */
public class UIManager {
    private static UIManager instance;
    private static final float STANDARD_WIDTH = 1080f;
    private static final float STANDARD_HEIGHT = 1920f;
    //实际设备的信息
    private static float disPlayMetricsWidth;
    private static float disPlayMetricsHeight;
    //状态栏高度
    private static float systemBarHeight;

    private UIManager(Context context) {
        //计算缩放系数
        WindowManager manager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics metrics = new DisplayMetrics();
        if (disPlayMetricsHeight == 0.0f || disPlayMetricsWidth == 0.0f) {
            manager.getDefaultDisplay().getMetrics(metrics);
            //横屏
            systemBarHeight = getSystemBarHeight(context);
            if (metrics.widthPixels > metrics.heightPixels) {
                disPlayMetricsWidth = (float) metrics.heightPixels;
                disPlayMetricsHeight = metrics.widthPixels - systemBarHeight;
            } else {
                //竖屏
                disPlayMetricsWidth = (float) (metrics.widthPixels);
                disPlayMetricsHeight = metrics.heightPixels - systemBarHeight;
            }

        }
    }

    //横向缩放系数
    public float getHorizonScaleValue() {
        return disPlayMetricsWidth / STANDARD_WIDTH;
    }

    //竖直缩放系数
    public float getVerticalScaleVale() {
        return disPlayMetricsHeight / (STANDARD_HEIGHT - systemBarHeight);
    }

    public static UIManager getInstance(Context context) {
        if (instance == null) {
            instance = new UIManager(context);
        }
        return instance;
    }

    //生命周期发生改变后去重新实例化
    public static UIManager notifyInstance(Context context) {
        instance = new UIManager(context);
        return instance;
    }

    public static UIManager getInstance() {
        if (instance == null) {
            throw new RuntimeException("Not found this instance");
        }
        return instance;
    }


    /**
     * 获取状态栏的高度
     *
     * @param context
     * @param dimenClass
     * @param system_bar_height 状态栏高度
     * @param defaultValue
     * @return
     */
    private int getValue(Context context, String dimenClass, String system_bar_height, int defaultValue) {
        try {
            Class<?> aClass = Class.forName(dimenClass);
            Object instance = aClass.newInstance();
            Field field = aClass.getField(system_bar_height);
            int id = Integer.parseInt(field.get(instance).toString());
            return context.getResources().getDimensionPixelOffset(id);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return defaultValue;
    }

    private int getSystemBarHeight(Context context) {
        return getValue(context, "com.android.internal.R$dimen", "system_bar_height", 48);
    }

    public int getWidth(int width) {
        return Math.round((float) width * disPlayMetricsWidth / STANDARD_WIDTH);
    }

    public int getHeight(int height) {
        return Math.round((float) height * disPlayMetricsHeight / STANDARD_HEIGHT);
    }
}
