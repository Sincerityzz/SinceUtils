package com.sincerity.utilslibrary.view.indicator;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.support.v7.widget.AppCompatTextView;
import android.util.AttributeSet;

/**
 * Created by Sincerity on 2019/9/6.
 * 描述：颜色可以改变的文字
 */
public class ColorTrackTextView extends AppCompatTextView {
    private Paint mOriginPaint; //改变颜色前的画笔
    private Paint mChangePaint;//需要变色的画笔
    private String mText; //当前TextView的文本
    private float mCurrentPosition = 0.0f;//设置默认的颜色改变位置
    // 当前朝向
    private Direction mDirection = Direction.DIRECTION_LEFT;

    // 绘制的朝向枚举
    public enum Direction {
        DIRECTION_LEFT, DIRECTION_RIGHT
    }

    public ColorTrackTextView(Context context) {
        this(context, null);
    }

    public ColorTrackTextView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ColorTrackTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initPaint();
    }

    /**
     * 设置为改变之前的颜色
     *
     * @param mFontColor 颜色
     */
    public void setFontColor(int mFontColor) {
        mOriginPaint.setColor(mFontColor);
    }

    /**
     * 改变后的颜色
     *
     * @param mBehindColor 颜色
     */
    public void setBehindColor(int mBehindColor) {
        mChangePaint.setColor(mBehindColor);
    }

    /**
     * 初始化画笔
     */
    private void initPaint() {
        int mFontColor = Color.BLACK;
        mOriginPaint = getPaintColor(mFontColor);
        int mBehindColor = Color.RED;
        mChangePaint = getPaintColor(mBehindColor);
    }

    /**
     * 初始化画笔
     *
     * @param color 给画笔设置的颜色
     * @return Paint
     */
    private Paint getPaintColor(int color) {
        Paint mPaint = new Paint();
        mPaint.setAntiAlias(true);//抗锯齿
        mPaint.setDither(true);//防抖动
        mPaint.setColor(color);//设置颜色
        mPaint.setTextSize(getTextSize());//字体大小 跟随TextView字体大小
        return mPaint;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        mText = getText().toString();//获取当前TextView的文字
        int middle = (int) (getWidth() * mCurrentPosition);//得到需要改变颜色的位置
        if (mDirection == Direction.DIRECTION_LEFT) {  // 左边是红色右边是黑色
            // 绘制变色
//            drawText(canvas, mChangePaint, 0, middle);
            drawChangeDirectionLeft(canvas,middle);
//            drawText(canvas, mOriginPaint, middle, getWidth());
            drawOriginDirectionLeft(canvas,middle);
        } else {
            // 右边是红色左边是黑色
            drawChangeDirectionRight(canvas, middle);
//            drawText(canvas, mChangePaint, getWidth() - middle, getWidth());
            // 绘制变色
            drawOriginDirectionRight(canvas, middle);
//            drawText(canvas, mOriginPaint, 0, getWidth() - middle);
        }
    }

    /**
     * 画朝向右边变色字体
     */
    private void drawChangeDirectionRight(Canvas canvas, int middle) {
        drawText(canvas, mChangePaint, getWidth() - middle, getWidth());
    }

    /**
     * 画朝向左边默认色字体
     */
    private void drawOriginDirectionRight(Canvas canvas, int middle) {
        drawText(canvas, mOriginPaint, 0, getWidth() - middle);
    }

    /**
     * 画朝向左边变色字体
     */
    private void drawChangeDirectionLeft(Canvas canvas, int middle) {
        drawText(canvas, mChangePaint, 0, middle);
    }

    /**
     * 画朝向左边默认色字体
     */
    private void drawOriginDirectionLeft(Canvas canvas, int middle) {
        drawText(canvas, mOriginPaint, middle, getWidth());
    }

    /**
     * 设置当前的进度
     *
     * @param currentProgress 当前进度
     */
    public void setCurrentProgress(float currentProgress) {
        this.mCurrentPosition = currentProgress;
        // 重新绘制
        invalidate();
    }

    /**
     * 设置绘制方向，从右到左或者从左到右
     *
     * @param direction 绘制方向
     */
    public void setDirection(Direction direction) {
        this.mDirection = direction;
    }

    /**
     * 根据指定位置绘制文本
     *
     * @param canvas 画布
     * @param startX 开始位置
     * @param endX   结束位置
     * @param mPaint 画笔
     */
    private void drawText(Canvas canvas, Paint mPaint, int startX, int endX) {
        // 保存画笔状态
        canvas.save();
        // 截取绘制的内容，待会就只会绘制clipRect设置的参数部分
        canvas.clipRect(startX, 0, endX, getHeight());
        // 获取文字的范围
        Rect bounds = new Rect();
        mOriginPaint.getTextBounds(mText, 0, mText.length(), bounds);
        // 获取文字的Metrics 用来计算基线
        Paint.FontMetricsInt fontMetrics = mOriginPaint.getFontMetricsInt();
        // 获取文字的宽高
        int fontTotalHeight = fontMetrics.bottom - fontMetrics.top;
        // 计算基线到中心点的位置
        int offY = fontTotalHeight / 2 - fontMetrics.bottom;
        // 计算基线位置
        int baseline = (getMeasuredHeight() + fontTotalHeight) / 2 - offY;
        canvas.drawText(mText, (getMeasuredWidth() >> 1) - (bounds.width() >> 1), baseline, mPaint);
        // 释放画笔状态
        canvas.restore();
    }
}
