package com.sincerity.customview.view;

import android.app.Activity;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.os.Build;
import android.os.Bundle;
import android.util.AttributeSet;
import android.view.View;

import androidx.annotation.Nullable;

import com.sincerity.utilslibrary.view.BannerView.DefaultActivityLifecycleCallbacks;

import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by Sincerity on 2019/12/10.
 * 描述：自定义钟表
 */
public class WatchAndClockView extends View {
    private Paint mHoursPaint;
    private int mHoursBorderWidth = 15;
    private Rect mTextRect;
    private float mSecondDegree;//秒针的度数
    private float mMinDegree;//分针
    private float mHourDegree;//时针
    private boolean mIsNight;//是否是下午
    private Rect rect;
    private RectF rectF;
    private String mTimeStr;
    private Timer timer = new Timer();
    private Activity mActivity;
    TimerTask timerTask = new TimerTask() {
        @Override
        public void run() {
            if (mSecondDegree == 360) {
                mSecondDegree = 0;
            }
            if (mMinDegree == 360) {
                mMinDegree = 0;
            }
            if (mHourDegree == 360) {
                mHourDegree = 0;
                mIsNight = !mIsNight;
            }
            mSecondDegree = mSecondDegree + 6;
            mMinDegree = mMinDegree + .1f;
            mHourDegree = mHourDegree + 1.0f / 240;
            postInvalidate();
        }
    };

    public WatchAndClockView setTime(int hours, int min, int second) {
        if (24 < hours || hours < 0 || 61 < min || min <= 0 || 61 < second || second <= 0) {
            return null;
        }
        float time = hours + min * 1.0f / 60f + second * 1.0f / 3600f;
        if (hours < 12) {
            mTimeStr = "下午";
            mIsNight = true;//添加一个变量，用于记录是否为下午。
            mHourDegree = (time - 12) * 30f;
        } else {
            mTimeStr = "上午";
            mIsNight = false;
            mHourDegree = time * 30f;
        }
        mMinDegree = (min + second * 1.0f / 60f) * 6f;
        mSecondDegree = second * 6f;
        invalidate();//重绘控件
        return this;
    }

    public WatchAndClockView(Context context) {
        this(context, null);
    }

    public WatchAndClockView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }


    public WatchAndClockView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mActivity= (Activity) context;
        initPaint();
    }

    private void initPaint() {
        mHoursPaint = new Paint();
        mHoursPaint.setAntiAlias(true);
        mHoursPaint.setStrokeWidth(mHoursBorderWidth);
        mHoursPaint.setColor(Color.BLACK);
        mHoursPaint.setStyle(Paint.Style.STROKE);
        mTextRect = new Rect();
        rect = new Rect();
        rectF = new RectF();
    }

    public void start() {
        timer.schedule(timerTask, 0, 1000);
        mActivity.getApplication().registerActivityLifecycleCallbacks(callbacks);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);
        int widthSize = MeasureSpec.getSize(widthMeasureSpec);
        int heightSize = MeasureSpec.getSize(heightMeasureSpec);
        int mDefaultSize = 280;
        if (widthMode == MeasureSpec.AT_MOST) {
            widthSize = mDefaultSize;
        } else if (heightMode == MeasureSpec.AT_MOST) {
            heightSize = mDefaultSize;
        }
        setMeasuredDimension(widthSize > heightSize ? heightSize : widthSize, widthSize > heightSize ? heightSize : widthSize);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        mHoursPaint.setAntiAlias(true);
        mHoursPaint.setStrokeWidth(mHoursBorderWidth);
        mHoursPaint.setColor(Color.BLACK);
        mHoursPaint.setStyle(Paint.Style.STROKE);
        int radius = (getWidth()) / 2 - mHoursBorderWidth;
        int dx = getWidth() / 2;
        int dy = getHeight() / 2;
        //钟表的外边
        canvas.drawCircle(dx, dy, radius, mHoursPaint);
        //钟表的圆心
        mHoursPaint.setStrokeWidth(15);
        canvas.drawPoint(dx, dy, mHoursPaint);
        canvas.translate(dx, dy);
        for (int i = 0; i < 360; i++) {
            //12个小时s
            if (i % 30 == 0) {
                mHoursPaint.setStrokeWidth(2);
                canvas.drawLine(radius - mHoursBorderWidth - 25, 0, radius - mHoursBorderWidth, 0, mHoursPaint);
            } else if (i % 6 == 0) {
                mHoursPaint.setStrokeWidth(1);
                canvas.drawLine(radius - mHoursBorderWidth - 15, 0, radius - mHoursBorderWidth, 0, mHoursPaint);
            } else {
                //秒针
                mHoursPaint.setStrokeWidth(1);
                canvas.drawLine(radius - mHoursBorderWidth - 6, 0, radius - mHoursBorderWidth, 0, mHoursPaint);
            }
            canvas.rotate(1);
        }
        //画数字
        mHoursPaint.setTextSize(25);
        String mDefaultText;
        for (int i = 0; i < 12; i++) {
            if (i == 0) {
                mDefaultText = String.valueOf(12);
                setClockText(canvas, 0, mDefaultText, mHoursPaint);
            } else {
                mDefaultText = String.valueOf(i);
                setClockText(canvas, i * 30, mDefaultText, mHoursPaint);
            }
        }
        mHoursPaint.setStrokeWidth(12);
        mHoursPaint.setTextSize(68);
        mHoursPaint.setStyle(Paint.Style.FILL_AND_STROKE);
        mHoursPaint.getTextBounds(mTimeStr, 0, mTimeStr.length(), rect);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            mHoursPaint.setLetterSpacing(0.3F);
        }
        int baseLine = radius / 3;
        canvas.drawText(mTimeStr, -rect.width() / 2, -baseLine, mHoursPaint);
        rectF.set(rect.left - 20, rect.top - 20, (float) ((rect.right * 1.3) + 20), rect.bottom + 20);
        mHoursPaint.setStrokeWidth(1);
        mHoursPaint.setTextSize(15);
        mHoursPaint.setStyle(Paint.Style.STROKE);
        canvas.translate(-rect.width() / 2, -baseLine);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            mHoursPaint.setLetterSpacing(0F);
        }
        canvas.drawRoundRect(rectF, 10, 10, mHoursPaint);
        //秒针
        canvas.translate(rect.width() / 2, baseLine);
        canvas.save();
        mHoursPaint.setColor(Color.RED);
        mHoursPaint.setStrokeWidth(5);
        mHoursPaint.setTextSize(16);
        mHoursPaint.setStrokeCap(Paint.Cap.ROUND);
        canvas.rotate(mSecondDegree);
        canvas.drawLine(0, 0, 0, -dx + 60, mHoursPaint);
        canvas.restore();
        //分针
        canvas.save();
        mHoursPaint.setColor(Color.BLACK);
        mHoursPaint.setStrokeWidth(8);
        mHoursPaint.setStrokeCap(Paint.Cap.ROUND);
        canvas.rotate(mMinDegree);
        canvas.drawLine(0, 0, 0, -(getWidth() - mHoursBorderWidth) / 3 - 60, mHoursPaint);
        canvas.restore();
        //时针
        canvas.save();
        mHoursPaint.setColor(Color.BLACK);
        mHoursPaint.setStrokeWidth(12);
        mHoursPaint.setStrokeCap(Paint.Cap.ROUND);
        canvas.rotate(mHourDegree);
        canvas.drawLine(0, 0, 0, -(getWidth() - mHoursBorderWidth) / 4 - 60, mHoursPaint);
        canvas.restore();
    }

    public void release() {
        timerTask.cancel();
        timer.cancel();
        mActivity.getApplication().unregisterActivityLifecycleCallbacks(callbacks);
    }
    private void setClockText(Canvas canvas, int degree, String text, Paint mPaint) {
        mPaint.getTextBounds(text, 0, text.length(), mTextRect);//得到textBound的值
        canvas.rotate(degree);//旋转的度数
        canvas.translate(0, 75 - (getWidth() / 2));//这里的50是坐标中心距离时钟最外边框的距离，当然你可以根据需要适当调节
        canvas.rotate(-degree);
        canvas.drawText(text, -mTextRect.width() / 2, mTextRect.height() / 2, mPaint);
        canvas.rotate(degree);
        canvas.translate(0, (getWidth() / 2) - 75);
        canvas.rotate(-degree);
    }

    DefaultActivityLifecycleCallbacks callbacks = new DefaultActivityLifecycleCallbacks() {
        @Override
        public void onActivityCreated(Activity activity, Bundle savedInstanceState) {
            super.onActivityCreated(activity, savedInstanceState);
        }

        @Override
        public void onActivityStopped(Activity activity) {
            super.onActivityStopped(activity);
        }
    };
}
