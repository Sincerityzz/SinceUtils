package com.sincerity.utilslibrary.view.RecycleView.adapter.itype;

import android.widget.ImageView;

/**
 * Created by Sincerity on 2019/10/16.
 * 描述：  图片加载器
 */
public abstract class ImageLoader {
    private String path;

    public String getPath() {
        return path;
    }

    public ImageLoader(String path) {
        this.path = path;
    }

    /**
     * 复写这个方法去加载图片
     *
     * @param view 需要加载图片的ImageView
     * @param path 图片的Url
     */
    public abstract void loadImage(ImageView view, String path);
}
