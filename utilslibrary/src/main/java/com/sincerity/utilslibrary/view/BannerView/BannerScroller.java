package com.sincerity.utilslibrary.view.BannerView;

import android.content.Context;
import android.view.animation.Interpolator;
import android.widget.Scroller;

/**
 * Created by Sincerity on 2019/8/30.
 * 描述：改变Viewpager的页面切换的持续时间速率
 */
public class BannerScroller extends Scroller {
    private int mScrollerDuration = 500;

    /**
     * 设置滚动速率
     * @param mScrollerDuration
     */
    public void setScrollerDuration(int mScrollerDuration) {
        this.mScrollerDuration = mScrollerDuration;
    }

    public BannerScroller(Context context) {
        super(context);
    }

    public BannerScroller(Context context, Interpolator interpolator) {
        super(context, interpolator);
    }

    public BannerScroller(Context context, Interpolator interpolator, boolean flywheel) {
        super(context, interpolator, flywheel);
    }
    //设置当前的页面切换速率
    @Override
    public void startScroll(int startX, int startY, int dx, int dy, int duration) {
        super.startScroll(startX, startY, dx, dy, mScrollerDuration);
    }
}
