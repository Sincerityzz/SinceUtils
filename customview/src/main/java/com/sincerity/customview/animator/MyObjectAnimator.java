package com.sincerity.customview.animator;

import android.view.View;

import java.lang.ref.WeakReference;

/**
 * Created by Sincerity on 2019/12/13.
 * 描述：
 */
public class MyObjectAnimator implements VSYNCManager.AnimatorFrameCallBack {
    private MyFloatProperValuesHolder myFloatProperValuesHolder;
    private long mStartTime = -1;
    private long mDuration = 0;
    private WeakReference<View> target;//当前的View
    private float index = 0;
    private TimeInterpolator interpolator;

    public void setInterpolator(TimeInterpolator interpolator) {
        this.interpolator = interpolator;
    }

    public void setmDuration(long mDuration) {
        this.mDuration = mDuration;
    }

    private MyObjectAnimator(View view, String propertyName, float... values) {
        target = new WeakReference<View>(view);
        myFloatProperValuesHolder = new MyFloatProperValuesHolder(propertyName, values);
    }

    public static MyObjectAnimator ofFloat(View view, String propertyName, float... values) {
        MyObjectAnimator animator = new MyObjectAnimator(view, propertyName, values);
        return animator;
    }

    //回调
    @Override
    public boolean doAnimatorFrame(long time) {
        float total = mDuration / 16;
        //计算当前的执行百分比
        float fraction = index++ / total;
        if (interpolator != null) {
            fraction = interpolator.getInterpolator(fraction);
        }
        if (index >= total) {
            index = 0;
        }
        myFloatProperValuesHolder.setAnimatedValue(target.get(), fraction);
        return false;
    }

    public void start() {
        myFloatProperValuesHolder.setUpSetter(target);
        mStartTime = System.currentTimeMillis();
        VSYNCManager.getInstance().add(this);
    }
}
