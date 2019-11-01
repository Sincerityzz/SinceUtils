package com.sincerity.utilslibrary.view.RecycleView.adapter.itype;

import androidx.recyclerview.widget.RecyclerView;

/**
 * Created by Sincerity on 2019/3/29.
 * 描述：
 */
public interface IViewHolder<V extends RecyclerView.ViewHolder> {
    /**
     * 设置文字
     *
     * @param viewId id
     * @param text   文本
     * @return 当前ViewHolder
     */

    V setText(int viewId, String text); //设置文字

    /**
     * 加载本地图片
     *
     * @param viewId id
     * @param resId  本地资源id
     * @return 当前ViewHolder
     */

    V setImgResource(int viewId, int resId); //设置图片

    /**
     * 加载网络图片
     *
     * @param viewId id
     * @param loader 图片加载器
     * @return 当前ViewHolder
     */

    V setImgFromPath(int viewId, ImageLoader loader);

}
