package com.sincerity.customview.splash;

/**
 * Created by Sincerity on 2019/12/16.
 * 描述：封装系统View的自定义属性
 */
public class ParallaxViewTag {
    protected int index;
    protected  float xIn;
    protected  float xOut;
    protected  float yIn;
    protected  float yOut;
    protected  float alphaIn;
    protected  float alphaOut;

    @Override
    public String toString() {
        return "ParallaxViewTag{" +
                "index=" + index +
                ", xIn=" + xIn +
                ", xOut=" + xOut +
                ", yIn=" + yIn +
                ", yOut=" + yOut +
                ", alphaIn=" + alphaIn +
                ", alphaOut=" + alphaOut +
                '}';
    }
}
