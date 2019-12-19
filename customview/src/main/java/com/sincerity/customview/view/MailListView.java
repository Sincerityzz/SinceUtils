package com.sincerity.customview.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.MotionEvent;
import android.view.View;

import androidx.annotation.Nullable;

/**
 * Created by Sincerity on 2019/12/11.
 * 描述： 字母索引 offline
 */
public class MailListView extends View {                                                                                                                                        /* */
    private Paint mPaint;
    private static String[] letters = {"#", "A", "B", "C", "D", "E", "F", "G", "H", "I",
            "J", "K", "L", "M", "N", "O", "P", "Q", "R", "S", "T", "U", "V",
            "W", "X", "Y", "Z"};
    //当前触摸的位置字母
    private String mCurrentTouchLetter;

    public MailListView(Context context) {
        this(context, null);
    }

    public MailListView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public MailListView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initPaint();
    }

    private void initPaint() {
        mPaint = new Paint();
        mPaint.setAntiAlias(true);
        mPaint.setTextSize(sp2px(12));
        mPaint.setColor(Color.RED);
    }

    private float sp2px(int sp) {
        return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, sp, getResources().getDisplayMetrics());
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        //用画笔去测量Text的宽高
        float textWidth = mPaint.measureText("A");
        int width = (int) (getPaddingLeft() + getPaddingRight() + textWidth);
        setMeasuredDimension(width, MeasureSpec.getSize(heightMeasureSpec));
    }

    @Override
    protected void onDraw(Canvas canvas) {
        int itemHeight = (getHeight() - getPaddingTop() - getPaddingBottom()) / letters.length;
        for (int i = 0; i < letters.length; i++) {
            //字母的中心位置
            int letterCenter = itemHeight * i + itemHeight / 2 + itemHeight + getPaddingTop();
            //知道中心位置 求基线
            Paint.FontMetrics metrics = mPaint.getFontMetrics();
            int dy = (int) ((metrics.bottom - metrics.top) / 2 - metrics.bottom);
            int baseLine = letterCenter + dy;
            float textWidth = mPaint.measureText(letters[i]);
            int x = (int) (getWidth() / 2 - textWidth / 2);
            //当前字母高亮
            if (letters[i].equals(mCurrentTouchLetter)) {
                mPaint.setColor(Color.BLUE);
                mPaint.setTextSize(sp2px(18));
                canvas.drawText(letters[i], x, baseLine, mPaint);
            } else {
                mPaint.setColor(Color.RED);
                mPaint.setTextSize(sp2px(16));
                canvas.drawText(letters[i], x, baseLine, mPaint);
            }

        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
            case MotionEvent.ACTION_MOVE:
                //计算当前触摸的字母,获取当前的位置
                float currentMoveY = event.getY();

                //通过位置获取字母
                int itemHeight = (getHeight() - getPaddingTop() - getPaddingBottom()) / letters.length;
                int mCurrentPosition = (int) (currentMoveY / itemHeight);
                //触摸View头部之外
                if (mCurrentPosition < 0) {
                    mCurrentPosition = 0;
                }
                //触摸View底部之外
                if (mCurrentPosition > letters.length - 1) {
                    mCurrentPosition = letters.length - 1;
                }
                mCurrentTouchLetter = letters[mCurrentPosition];
                if (mLetterTouchListener != null) {
                    mLetterTouchListener.touch(mCurrentTouchLetter, true);
                }
                invalidate();
                break;
            case MotionEvent.ACTION_UP:
                postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        if (mLetterTouchListener != null) {
                            mLetterTouchListener.touch(mCurrentTouchLetter, false);
                        }
                    }
                }, 2000);

                break;
        }
        return true;
    }

    interface LetterTouchListener {
        void touch(CharSequence letter, boolean isTouch);
    }

    private LetterTouchListener mLetterTouchListener;

    public void setLetterTouchListener(LetterTouchListener letterTouchListener) {
        this.mLetterTouchListener = letterTouchListener;
    }
}
