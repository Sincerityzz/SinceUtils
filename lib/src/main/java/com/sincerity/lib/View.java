package com.sincerity.lib;

/**
 * Created by Sincerity on 2019/12/13.
 * 描述：手写View事件分发
 */
public class View {
    private int left;
    private int right;
    private int top;
    private int bottom;
    private OnTouchListener mOnTouchListener;
    private OnClickListener mOnClickListener;

    View(int left, int top, int right, int bottom) {
        this.left = left;
        this.right = right;
        this.top = top;
        this.bottom = bottom;
    }

    public void setmOnTouchListener(OnTouchListener mOnTouchListener) {
        this.mOnTouchListener = mOnTouchListener;
    }

    public void setmOnClickListener(OnClickListener mOnClickListener) {
        this.mOnClickListener = mOnClickListener;
    }


    //判断是否能够接受到这个事件
    boolean isContainer(int x, int y) {
        return (x >= left && x < right && y >= top && y < bottom);
    }

    //View要接受分发
    public boolean dispatchTouchEvent(MotionEvent event) {
        //是否需要消费事件
        boolean result = false;
        if (mOnTouchListener != null && mOnTouchListener.onTouch(this, event)) {
            result = true;
        }
        if (!result && onTouchEvent(event)) {
            result = true;
        }
        return result;
    }

    private boolean onTouchEvent(MotionEvent event) {
        if (mOnClickListener != null) {
            mOnClickListener.onClick(this);
            return true;
        }
        return false;
    }
}
