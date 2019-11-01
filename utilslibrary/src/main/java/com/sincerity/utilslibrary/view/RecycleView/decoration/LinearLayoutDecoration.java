package com.sincerity.utilslibrary.view.RecycleView.decoration;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

/**
 * Created by Sincerity on 2019/10/5.
 * 描述：
 */
public class LinearLayoutDecoration extends RecyclerView.ItemDecoration {
    private Drawable mDivider;

    public LinearLayoutDecoration(Context context, int drawableResId) {
        //获取Drawable
        mDivider = ContextCompat.getDrawable(context, drawableResId);
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
            rect.top = rect.bottom - mDivider.getIntrinsicHeight();
            mDivider.setBounds(rect);
            mDivider.draw(c);
//            c.drawRect(rect, mPaint);
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
            outRect.top = mDivider.getIntrinsicHeight();
        }
    }
}
