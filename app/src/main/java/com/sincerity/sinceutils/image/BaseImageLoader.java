package com.sincerity.sinceutils.image;

import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.BaseRequestOptions;
import com.bumptech.glide.request.RequestOptions;
import com.sincerity.sinceutils.R;
import com.sincerity.utilslibrary.view.RecycleView.adapter.itype.ImageLoader;

/**
 * Created by Sincerity on 2019/10/16.
 * 描述： 图片加载的引擎 然而这个图片需要根据自己实际需求去实现
 */
public class BaseImageLoader extends ImageLoader {
    public BaseImageLoader(String path) {
        super(path);
    }

    @Override
    public void loadImage(ImageView view, String path) {
        BaseRequestOptions options = new RequestOptions();
        options.error(R.mipmap.ic_launcher).placeholder(R.mipmap.ic_launcher);
        options.centerCrop();
        Glide.with(view.getContext()).load(path).apply(options).into(view);
    }
}
