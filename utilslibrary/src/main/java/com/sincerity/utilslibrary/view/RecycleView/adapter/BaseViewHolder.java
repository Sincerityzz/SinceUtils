package com.sincerity.utilslibrary.view.RecycleView.adapter;

import android.util.SparseArray;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.sincerity.utilslibrary.view.RecycleView.adapter.itype.IViewHolder;
import com.sincerity.utilslibrary.view.RecycleView.adapter.itype.ImageLoader;

/**
 * Created by Sincerity on 2019/10/8.
 * 描述：
 */
public class BaseViewHolder extends RecyclerView.ViewHolder implements IViewHolder<BaseViewHolder> {
    private SparseArray<View> array; //弱应用

    /**
     * 查找View
     *
     * @param viewId id
     * @param <T>    View的类型
     * @return
     */
    public <T extends View> T getView(int viewId) {
        //多次进行findById 对已有的ViewId进行缓存
        View view = array.get(viewId);
        //缓存去哪View
        if (view == null) {
            view = itemView.findViewById(viewId);
            array.put(viewId, view);
        }
        return (T) view;
    }

    /**
     * 构造方法
     *
     * @param itemView itemView
     */
    public BaseViewHolder(@NonNull View itemView) {
        super(itemView);
        array = new SparseArray<>();
    }

    /**
     * 设置文本
     *
     * @param viewId id
     * @param text   设置的文本内容
     * @return 当前的ViewHolder
     */
    @Override
    public BaseViewHolder setText(int viewId, String text) {
        View view = getView(viewId);
        if (view instanceof TextView) {
            ((TextView) view).setText(text);
        }
        if (view instanceof Button) {
            ((Button) view).setText(text);
        }
        return this;
    }

    /**
     * 设置本地资源图片
     *
     * @param viewId id
     * @param resId  资源Id
     * @return 当前的ViewHolder
     */
    @Override
    public BaseViewHolder setImgResource(int viewId, int resId) {
        ImageView view = getView(viewId);
        view.setImageResource(resId);
        return this;
    }

    /**
     * 设置网络图片 这里的图片加载方式不固定
     *
     * @param viewId id
     * @param loader 图片地址
     * @return 当前的ViewHolder
     */
    @Override
    public BaseViewHolder setImgFromPath(int viewId, ImageLoader loader) {
        ImageView view = getView(viewId);
        loader.loadImage(view, loader.getPath());
        return this;
    }

    @Override
    public BaseViewHolder setViewVisibility(int viewId, int visibility) {
        View view = getView(viewId);
        view.setVisibility(visibility);
        return this;
    }

}

