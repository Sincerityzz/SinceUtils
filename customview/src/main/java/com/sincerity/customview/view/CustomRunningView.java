package com.sincerity.customview.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.SweepGradient;
import android.util.AttributeSet;
import android.view.View;

import androidx.annotation.Nullable;

import com.sincerity.customview.R;

/**
 * Created by Sincerity on 2019/12/9.
 * 描述：运动步数
 */
public class CustomRunningView extends View {
    private int mInnerColor = Color.RED; //默认颜色值
    private int mOuterColor = Color.BLUE;
    private int mTextSize = 16;
    private float mBorderWidth = 6;
    private Paint mOutPaint, mInnerPaint, mTextPaint;
    private RectF mOutRectF;
    private Rect mTextRect;
    private int mInnerStep = 0;
    private int mInnerMaxStep = 0;
    private int[] colors = {Color.GREEN, Color.RED};
    private float[] position = {0.5f, 0.75f};

    public CustomRunningView(Context context) {
        this(context, null);
    }

    public CustomRunningView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CustomRunningView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initAttrs(context, attrs);
        initPaint();
    }

    private void initPaint() {
        mOutPaint = new Paint();
        mOutPaint.setAntiAlias(true);//抗锯齿
        mOutPaint.setStrokeWidth(mBorderWidth);
        mOutPaint.setColor(mOuterColor);
        mOutPaint.setStyle(Paint.Style.STROKE);
        mOutPaint.setStrokeCap(Paint.Cap.ROUND);
        mOutRectF = new RectF();
        /***************************************/
        mInnerPaint = new Paint();
        mInnerPaint.setAntiAlias(true);//抗锯齿
        mInnerPaint.setStrokeWidth(mBorderWidth);
        mInnerPaint.setColor(mInnerColor);
        mInnerPaint.setStyle(Paint.Style.STROKE);
        mInnerPaint.setStrokeCap(Paint.Cap.ROUND);
        /*****************************************/
        mTextPaint = new Paint();
        mTextPaint.setAntiAlias(true);
        mTextPaint.setTextSize(mTextSize);
        mTextPaint.setColor(mInnerColor);
        mTextRect = new Rect();
    }

    private void initAttrs(Context mContext, AttributeSet attrs) {
        TypedArray array = mContext.obtainStyledAttributes(attrs, R.styleable.CustomRunningView);
        mInnerColor = array.getColor(R.styleable.CustomRunningView_innerColor, mInnerColor);
        mOuterColor = array.getColor(R.styleable.CustomRunningView_outColor, mOuterColor);
        mTextSize = array.getDimensionPixelSize(R.styleable.CustomRunningView_stepTextSize, mTextSize);
        mBorderWidth = array.getDimension(R.styleable.CustomRunningView_borderWidth, mBorderWidth);
        array.recycle();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);
        int widthSize = MeasureSpec.getSize(widthMeasureSpec);
        int heightSize = MeasureSpec.getSize(heightMeasureSpec);
        if (widthMode == MeasureSpec.AT_MOST) {
            widthSize = 140;
        } else if (heightMode == MeasureSpec.AT_MOST) {
            heightSize = 140;
        }
        setMeasuredDimension(widthSize > heightSize ? heightSize : widthSize, widthSize > heightSize ? heightSize : widthSize);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        //外圆弧 描边有重复问题
        int center = getWidth() / 2;
        int radius = (int) (getWidth() / 2 - mBorderWidth / 2);
        mOutRectF.set(center - radius, center - radius, center + radius, center + radius);
        canvas.drawArc(mOutRectF, 135, 270, false, mOutPaint);
        //内圆弧 动态去设置 百分比
        if (mInnerMaxStep == 0) return;
        float sweepAngle = (float) mInnerStep / mInnerMaxStep;
        mInnerPaint.setShader(new SweepGradient(center, radius, colors, position));
        canvas.drawArc(mOutRectF, 135, sweepAngle * 270, false, mInnerPaint);
        //画文字
        String stepText = mInnerStep + "";
        mTextPaint.getTextBounds(stepText, 0, stepText.length(), mTextRect);
        int dx = getWidth() / 2 - mTextRect.width() / 2;
        //基线
        Paint.FontMetricsInt metrics = mTextPaint.getFontMetricsInt();
        int dy = (metrics.bottom - metrics.top) / 2 - metrics.bottom;
        int baseLine = getHeight() / 2 + dy;
        canvas.drawText(stepText, dx, baseLine, mTextPaint);
    }

    //设置最大值
    public synchronized void setStepMax(int max) {
        this.mInnerMaxStep = max;
    }

    //设置当前进度
    public synchronized void setCurrentStep(int currentStep) {
        this.mInnerStep = currentStep;
        //系统重绘
        invalidate();
    }

}
