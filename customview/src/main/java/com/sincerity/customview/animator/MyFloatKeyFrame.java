package com.sincerity.customview.animator;

/**
 * Created by Sincerity on 2019/12/13.
 * 描述：关键帧 保存着某一时刻的具体状态
 */
public class MyFloatKeyFrame {
    float mFraction;
    Class mValueType;
    float mValue;

    public MyFloatKeyFrame(float fraction, float value) {
        mFraction = fraction;
        mValue = value;
        mValueType = float.class;
    }

    public float getmValue() {
        return mValue;
    }

    public void setmValue(float mValue) {
        this.mValue = mValue;
    }

    public float getmFraction() {
        return mFraction;
    }
}
