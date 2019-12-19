package com.sincerity.customview.view;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.LinearInterpolator;
import android.view.animation.OvershootInterpolator;

import androidx.annotation.Nullable;

import com.sincerity.customview.R;

/**
 * Created by Sincerity on 2019/12/12.
 * 描述: 开场动画
 */
public class SplashView extends View {
    private Paint mPaint; //旋转圆的画笔
    private Paint mHolePaint;//扩散圆的画笔
    private ValueAnimator mValueAnimator;//属性动画
    private int mBackgroundColor = Color.WHITE; //背景颜色
    private int[] mCircleColors; //小球的颜色数组
    private float mCenterX;//旋转圆的中心X坐标
    private float mCenterY;//旋转圆的中心Y坐标
    private float mDistance;//扩散圆的最大半径
    private float mCircleRadius = 18;//小球半径
    private float mRotateRadius = 98;//旋转大圆的半径
    private float mCurrentRotaAngle = 0F;//当前大圆的旋转角度
    private float mCurrentRotateRadius = mRotateRadius; //当前大圆的半径
    private float mCurrentHoleRadius = 0f;//扩散圆的半径
    private int mRotaDuration = 1200; //表示动画的时长
    private SplashState mState;

    public SplashView(Context context) {
        this(context, null);
    }

    public SplashView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public SplashView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context context) {
        mPaint = new Paint();
        mHolePaint = new Paint();
        mHolePaint.setStyle(Paint.Style.STROKE);
        mHolePaint.setColor(mBackgroundColor);
        mCircleColors = context.getResources().getIntArray(R.array.splash_circle_colors);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        mCenterX = w * 1f / 2;
        mCenterY = h * 1f / 2;
        mDistance = (float) (Math.hypot(w, h) / 2);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (mState == null) {
            mState = new RotateState();
        }
        mState.drawState(canvas);
    }

    private abstract class SplashState {
        abstract void drawState(Canvas canvas);
    }

    //旋转阶段
    private class RotateState extends SplashState {
        private RotateState() {
            mValueAnimator = ValueAnimator.ofFloat(0, (float) (Math.PI * 2));
            mValueAnimator.setRepeatCount(2);
            mValueAnimator.setDuration(mRotaDuration);
            mValueAnimator.setInterpolator(new LinearInterpolator());
            mValueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                @Override
                public void onAnimationUpdate(ValueAnimator animation) {
                    mCurrentRotaAngle = (float) animation.getAnimatedValue();
                    invalidate();
                }
            });
            mValueAnimator.addListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    super.onAnimationEnd(animation);
                    mState = new SpreadState();
                }
            });
            mValueAnimator.start();
        }

        @Override
        void drawState(Canvas canvas) {
            //绘制当前背景
            drawBackground(canvas);
            //绘制6个小球
            drawCircles(canvas);
            Rect rect = new Rect();
            String s = "正在拼命加载中...";
            mPaint.getTextBounds(s, 0, s.length(), rect);
            mPaint.setTextSize(24);
            float v = mPaint.measureText(s);
            float cx = (float) ((Math.cos(90) * mCurrentRotateRadius + mCenterX) - (v / 2));
            float cy = (float) (Math.sin(90) * mCurrentRotateRadius + mCenterY) + 150;
            canvas.drawText(s, cx, cy, mPaint);
        }
    }

    private void drawCircles(Canvas canvas) {
        //二个小球的角度
        float rotateAngle = (float) (Math.PI * 2 / mCircleColors.length);
        for (int i = 0; i < mCircleColors.length; i++) {
            float angle = i * rotateAngle + mCurrentRotaAngle;
            float cx = (float) (Math.cos(angle) * mCurrentRotateRadius + mCenterX);
            float cy = (float) (Math.sin(angle) * mCurrentRotateRadius + mCenterY);
            mPaint.setColor(mCircleColors[i]);
            canvas.drawCircle(cx, cy, mCircleRadius, mPaint);
        }
    }

    private void drawBackground(Canvas canvas) {
        if (mCurrentHoleRadius > 0) {
            float strokeWidth = mDistance - mCurrentHoleRadius;
            float radius = strokeWidth / 2 + mCurrentHoleRadius;
            mHolePaint.setStrokeWidth(strokeWidth);
            canvas.drawCircle(mCenterX, mCenterY, radius, mHolePaint);
        } else {
            canvas.drawColor(mBackgroundColor);
        }
    }

    //扩散聚合
    private class SpreadState extends SplashState {
        private SpreadState() {
            mValueAnimator = ValueAnimator.ofFloat(mCircleRadius, mRotateRadius);
            mValueAnimator.setDuration(mRotaDuration);
            mValueAnimator.setInterpolator(new OvershootInterpolator(10f));
            mValueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                @Override
                public void onAnimationUpdate(ValueAnimator animation) {
                    mCurrentRotateRadius = (float) animation.getAnimatedValue();
                    invalidate();
                }
            });
            mValueAnimator.addListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    super.onAnimationEnd(animation);
                    mState = new ExpandState();
                }
            });
            mValueAnimator.reverse();
        }

        @Override
        void drawState(Canvas canvas) {
            drawBackground(canvas);
            drawCircles(canvas);
        }
    }

    //水波纹效果
    private class ExpandState extends SplashState {
        private ExpandState() {
            mValueAnimator = ValueAnimator.ofFloat(mCircleRadius, mDistance);
            mValueAnimator.setDuration(mRotaDuration);
            mValueAnimator.setInterpolator(new LinearInterpolator());
            mValueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                @Override
                public void onAnimationUpdate(ValueAnimator animation) {
                    mCurrentHoleRadius = (float) animation.getAnimatedValue();
                    invalidate();
                }
            });
            mValueAnimator.start();
        }

        @Override
        void drawState(Canvas canvas) {
            drawBackground(canvas);
        }
    }
}
