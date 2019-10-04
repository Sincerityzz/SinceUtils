package com.sincerity.utilslibrary.view.indicator;

import android.view.View;
import android.view.ViewGroup;

/**
 * Created by Sincerity on 2019/9/24.
 * 描述：
 */
public abstract class IndicatorAdapter<T extends View> {
    public abstract int getCount(); //获取总条数

    /**
     * 获取当前的位置View
     *
     * @param position 当前下标
     * @param parent   view
     * @return View
     */
    public abstract T getView(int position, ViewGroup parent);

    public void highlightTheCurrent(T view) {
    }

    public void resetColor(T view) {
    }

    public View getTrackView() {
        return null;
    }
}
