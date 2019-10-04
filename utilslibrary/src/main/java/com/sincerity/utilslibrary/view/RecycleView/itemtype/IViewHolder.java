package com.sincerity.utilslibrary.view.RecycleView.itemtype;

import android.support.v7.widget.RecyclerView;

/**
 * Created by Sincerity on 2019/3/29.
 * 描述：
 */
public interface IViewHolder<V extends RecyclerView.ViewHolder> {

    V setText(int viewId, String text); //设置文字

    V setImgResource(int viewId, int resId); //设置图片

    V setVisible(int viewId, boolean isVisible);

    V setChecked(int viewId, boolean isChecked);
}
