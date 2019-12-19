package com.sincerity.customview.animator;

/**
 * Created by Sincerity on 2019/12/13.
 * 描述：
 */
public class LinearInterpolator implements TimeInterpolator {
    @Override
    public float getInterpolator(float input) {
        return input;
    }
}
