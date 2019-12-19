package com.sincerity.customview.view;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.DecelerateInterpolator;
import android.widget.LinearLayout;

import androidx.annotation.Nullable;

import com.sincerity.customview.R;

/**
 * Created by Sincerity on 2019/12/11.
 * 描述：自定义形状改变的加载动画
 */
public class LoadingView extends LinearLayout {
    private ShapeView mShapeView;
    private View mShadowView;
    private int mTranslationDistance;
    private long mAnimal_duration = 450;
    //是否停止动画
    private boolean mIsstopAnimal = false;

    public LoadingView(Context context) {
        this(context, null);
    }

    public LoadingView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public LoadingView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mTranslationDistance = dip2px(80);
        initLayout();
    }

    private int dip2px(int dip) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dip, getResources().getDisplayMetrics());
    }

    /**
     * 初始化加载布局
     */
    private void initLayout() {
        //加载写好的布局 loading_view
        inflate(getContext(), R.layout.view_loading, this);
        mShapeView = findViewById(R.id.shape_view);
        mShadowView = findViewById(R.id.shape_shadow);
        post(new Runnable() {
            @Override
            public void run() {
                //view绘制流程的onResume之后
                startFailAnimator();
            }
        });
        //对应Activity的OnCreate方法
    }

    private void startFailAnimator() {
        if (mIsstopAnimal) {
            return;
        }
        //动画的作用体 下落的位移动画
        ObjectAnimator translation = ObjectAnimator.ofFloat(mShapeView, "translationY", 0, mTranslationDistance);
        translation.setDuration(mAnimal_duration);
        //配合中间的位移动画缩小
        ObjectAnimator saleAnimator = ObjectAnimator.ofFloat(mShadowView, "scaleX", 1f, 0.3f);
        saleAnimator.setDuration(mAnimal_duration);
        AnimatorSet animatorSet = new AnimatorSet();
        //一起执行
        animatorSet.playTogether(translation, saleAnimator);
        //插值器 先减速 在加速
        animatorSet.setInterpolator(new AccelerateInterpolator());
        //按照序列来执行
//        animatorSet.playSequentially();
        animatorSet.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                //下落完成就上抛
                mShapeView.exchange();
                startUpAnimal();
            }

            @Override
            public void onAnimationStart(Animator animation) {
                startRotationAnimator();
            }
        });
        animatorSet.start();
    }

    private void startUpAnimal() {
        if (mIsstopAnimal) {
            return;
        }
        //动画的作用体 下落的位移动画
        ObjectAnimator translation = ObjectAnimator.ofFloat(mShapeView, "translationY", mTranslationDistance, 0);
        translation.setDuration(mAnimal_duration);
        //配合中间的位移动画缩小
        ObjectAnimator saleAnimator = ObjectAnimator.ofFloat(mShadowView, "scaleX", 0.3f, 1f);
        saleAnimator.setDuration(mAnimal_duration);
        AnimatorSet animatorSet = new AnimatorSet();
        //一起执行
        animatorSet.playTogether(translation, saleAnimator);
        //按照序列来执行
//        animatorSet.playSequentially();
        animatorSet.setInterpolator(new DecelerateInterpolator());
        animatorSet.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                //下落完成就上抛
                mShapeView.exchange();
                startFailAnimator();
            }

            @Override
            public void onAnimationStart(Animator animation) {
                //旋转
                startRotationAnimator();
            }
        });
        animatorSet.start();
    }

    @Override
    public void setVisibility(int visibility) {
        super.setVisibility(View.INVISIBLE); //visibility会重新计算页面
        mShapeView.clearAnimation();
        mShadowView.clearAnimation();
        //把动画View 从当前布局移除
        ViewGroup parent = (ViewGroup) getParent();
        if (parent != null) {
            parent.removeView(this);//从父布局移除
            removeAllViews(); //移除自己所有的VIew
        }
        mIsstopAnimal = true;
    }

    /**
     * 旋转动画
     */
    private void startRotationAnimator() {
        ObjectAnimator rotation = null;
        switch (mShapeView.getShape()) {
            case Circle:
            case Square:
                rotation = ObjectAnimator.ofFloat(mShapeView, "rotation", 0, 180);
                break;
            case Triangle:
                rotation = ObjectAnimator.ofFloat(mShapeView, "rotation", 0, -120);
                break;
        }
        rotation.setDuration(mAnimal_duration);
        rotation.setInterpolator(new AccelerateInterpolator());
        rotation.start();
    }
}
