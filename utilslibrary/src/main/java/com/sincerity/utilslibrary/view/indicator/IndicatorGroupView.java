package com.sincerity.utilslibrary.view.indicator;

import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.DecelerateInterpolator;
import android.widget.FrameLayout;
import android.widget.LinearLayout;

/**
 * Created by Sincerity on 2019/9/26.
 * 描述：包含ItemView和底部指示器
 */
public class IndicatorGroupView extends FrameLayout {
    private LinearLayout mIndicatorGroup;//指示器容器
    private View mBottomTrackView;
    private int mItemWidth;
    // 底部指示器的LayoutParams
    LayoutParams layoutParams;
    private int mIndicatorLeftMargin;

    public IndicatorGroupView(Context context) {
        this(context, null);
    }

    public IndicatorGroupView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public IndicatorGroupView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mIndicatorGroup = new LinearLayout(context);
        addView(mIndicatorGroup);
    }

    /**
     * 添加ItemView
     *
     * @param itemView View
     */
    public void addItemView(View itemView) {
        mIndicatorGroup.addView(itemView);
    }

    /**
     * 获取当前位置的View
     *
     * @param i 当前位置
     * @return View
     */
    public View getItem(int i) {

        return mIndicatorGroup.getChildAt(i);
    }

    /**
     * 添加底部跟踪指示器
     *
     * @param trackView 指示器View
     * @param itemWidth 指示器的宽度
     */
    public void addBottomTrackView(View trackView, int itemWidth) {
        if (trackView == null) {
            return;
        }
        this.mItemWidth = itemWidth;
        this.mBottomTrackView = trackView;
        //添加底部跟踪View
        addView(mBottomTrackView);
        //确定位置 底部
        layoutParams = (LayoutParams) mBottomTrackView.getLayoutParams();
        layoutParams.gravity = Gravity.BOTTOM;
        // 宽度 -> 一个条目的宽度  如果用户设置死了 那么就用用户的值如 88 如果没有那么就用mItemWidth
        int trackWidth = layoutParams.width;
        if (layoutParams.width == ViewGroup.LayoutParams.MATCH_PARENT) {
            trackWidth = mItemWidth;
        }
        // 设置的宽度过大
        if (trackWidth > mItemWidth) {
            trackWidth = mItemWidth;
        }
        // 最后确定宽度
        layoutParams.width = trackWidth;
        // 确保在最中间
        mIndicatorLeftMargin = (mItemWidth - trackWidth) / 2;
        layoutParams.leftMargin = mIndicatorLeftMargin;
    }

    //滚动底部的指示器
    public void scrollBottomTrack(int position, float offset) {
        if (mBottomTrackView == null) {
            return;
        }
        //左边的距离
        int leftMarin = (int) ((position + offset) * mItemWidth);
        //控制左边距离去移动
        layoutParams.leftMargin = leftMarin + mIndicatorLeftMargin;
        mBottomTrackView.setLayoutParams(layoutParams);
    }

    public void scrollBottomTrack(int i) {
        if (mBottomTrackView == null) {
            return;
        }
        int finalLeftMargin = i * mItemWidth + mIndicatorLeftMargin;
        //当前的位置
        final int currentLeftMargin = layoutParams.leftMargin;
        int distance = finalLeftMargin - currentLeftMargin;
        //动画
        ValueAnimator animator = ObjectAnimator.ofFloat(currentLeftMargin, finalLeftMargin)
                .setDuration((long) (Math.abs(distance) * 0.5f));
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                //不断回调这个方法
                float currentLeftMargin = (float) animation.getAnimatedValue();
                layoutParams.leftMargin = (int) currentLeftMargin;
                mBottomTrackView.setLayoutParams(layoutParams);
            }
        });
        //动画插值器 速度越来越慢
        animator.setInterpolator(new DecelerateInterpolator());
        animator.start();
    }
}
