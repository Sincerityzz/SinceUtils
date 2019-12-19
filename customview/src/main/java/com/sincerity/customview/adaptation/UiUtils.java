package com.sincerity.customview.adaptation;

/**
 * Created by Sincerity on 2019/12/17.
 * 描述：UI适配
 */
public class UiUtils {
    private static UiUtils instance;

    public static UiUtils getInstance() {
        if (instance == null) {
            synchronized (UiUtils.class) {
                if (instance == null) {
                    instance = new UiUtils();
                }
            }
        }
        return instance;
    }
}
