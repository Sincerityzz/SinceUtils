package com.sincerity.customview.animator;

import android.animation.FloatEvaluator;
import android.animation.TypeEvaluator;

import java.util.Arrays;
import java.util.List;

/**
 * Created by Sincerity on 2019/12/13.
 * 描述：关键帧管理类
 */
class MyKeyFarmedSet {
    private TypeEvaluator mEvaluator; //类型估值器
    private MyFloatKeyFrame mFirstKeyFrame; //关键帧
    private List<MyFloatKeyFrame> mKeyFrames;

    private MyKeyFarmedSet(MyFloatKeyFrame... keyFrames) {
        mKeyFrames = Arrays.asList(keyFrames);
        mFirstKeyFrame = keyFrames[0];//首帧
        mEvaluator = new FloatEvaluator();
    }

    static MyKeyFarmedSet ofFloat(float[] values) {
        int numKeyframe = values.length;
        MyFloatKeyFrame[] keyFrames = new MyFloatKeyFrame[numKeyframe];
        keyFrames[0] = new MyFloatKeyFrame(0F, values[0]);
        for (int i = 0; i < numKeyframe; i++) {
            keyFrames[i] = new MyFloatKeyFrame((float) i / (numKeyframe - 1), values[i]);
        }
        return new MyKeyFarmedSet(keyFrames);
    }

    Object getValue(float fraction) {
        MyFloatKeyFrame preKeyFrame = mFirstKeyFrame;
        for (int i = 0; i < mKeyFrames.size(); i++) {
            MyFloatKeyFrame nextKeyFrame = mKeyFrames.get(i);
            nextKeyFrame = mKeyFrames.get(i);
            if (fraction < nextKeyFrame.getmFraction()) {
                return mEvaluator.evaluate(fraction, preKeyFrame.getmValue(), nextKeyFrame.getmValue());
            }
            preKeyFrame = nextKeyFrame;
        }
        return null;
    }
}
