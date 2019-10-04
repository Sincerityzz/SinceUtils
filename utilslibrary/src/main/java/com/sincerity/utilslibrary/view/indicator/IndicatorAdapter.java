package com.sincerity.utilslibrary.view.indicator;

import android.view.View;
import android.view.ViewGroup;

/**
 * Created by Sincerity on 2019/9/24.
 * 描述：
 */
public abstract class IndeicatorAdapter {
    public abstract int getCount(); //获取总条数

    /**
     * 获取当前的位置View
     * @param position 当前下标
     * @param convertView  view
     * @return View
     */
    public abstract View getView(int position, ViewGroup convertView);
}
