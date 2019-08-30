package com.sincerity.utilslibrary.view.BannerView;

import android.view.View;

/**
 * Created by Sincerity on 2019/8/29.
 * 描述：适配器模式
 */
public abstract class BannerAdapter {
    /**
     * 根据位置获取子View
     *
     * @param position
     * @return
     */
    public abstract View getView(int position,View mConvertView);

    //获取轮播的数量
    public abstract int getCount();

    //根据位置获取广告位的描述
    public String getBannerDesc(int position) {
        return "";
    }
}
