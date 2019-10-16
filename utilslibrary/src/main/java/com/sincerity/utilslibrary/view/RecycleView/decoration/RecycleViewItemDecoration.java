package com.sincerity.utilslibrary.view.RecycleView.decoration;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;

/**
 * Created by Sincerity on 2019/10/4.
 * 描述：RecycleView的分割线
 */
public class RecycleViewItemDecoration extends RecyclerView.ItemDecoration {
    private Paint mPaint;
    private int size;

    public RecycleViewItemDecoration(int size) {
        this.size = size;
        mPaint = new Paint();
        mPaint.setAntiAlias(true);
        mPaint.setColor(Color.BLACK);
    }

    /**
     * 绘制分割线
     */
    @Override
    public void onDraw(@NonNull Canvas c, @NonNull RecyclerView parent, @NonNull RecyclerView.State state) {
        super.onDraw(c, parent, state);
        //利用Canvas绘制 位置在每一个item的头去绘制
        int childCount = parent.getChildCount();
        //指定绘制的区域
        Rect rect = new Rect();
        rect.left = parent.getLeft();//左边起始位置
        rect.right = parent.getWidth() - parent.getPaddingRight(); //右边结束位置
        for (int i = 1; i < childCount; i++) {
            rect.bottom = parent.getChildAt(i).getTop();
            rect.top = rect.bottom - size;
            c.drawRect(rect, mPaint);
        }
    }

    /**
     * 基本操作 就是流出分割线的位置
     */
    @Override
    public void getItemOffsets(@NonNull Rect outRect, @NonNull View view, @NonNull RecyclerView parent, @NonNull RecyclerView.State state) {
        super.getItemOffsets(outRect, view, parent, state);
        //获取当前的位置
        int position = parent.getChildAdapterPosition(view);
        if (position != 0) {
            outRect.top = size;
        }
    }
}
