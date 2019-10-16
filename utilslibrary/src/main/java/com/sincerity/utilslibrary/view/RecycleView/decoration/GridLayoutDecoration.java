package com.sincerity.utilslibrary.view.RecycleView.decoration;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

/**
 * Created by Sincerity on 2019/10/5.
 * 描述：Grid
 */
public class GridLayoutDecoration extends RecyclerView.ItemDecoration {
    private Drawable mDivider;

    public GridLayoutDecoration(Context context, int drawableResId) {
        //获取Drawable
        mDivider = ContextCompat.getDrawable(context, drawableResId);
    }

    /**
     * 绘制分割线
     */
    @Override
    public void onDraw(@NonNull Canvas c, @NonNull RecyclerView parent, @NonNull RecyclerView.State state) {
//        super.onDraw(c, parent, state);
        //绘制分割线
        drawHorizontal(c, parent);
        drawVertical(c, parent);
    }

    private void drawVertical(Canvas c, RecyclerView parent) {
        int childCount = parent.getChildCount();
        for (int i = 0; i < childCount; i++) {
            View childView = parent.getChildAt(i);
            RecyclerView.LayoutParams layoutParams = (RecyclerView.LayoutParams) childView.getLayoutParams();
//            int left = childView.getLeft() - layoutParams.leftMargin;
//            int right = childView.getRight() + layoutParams.rightMargin;
            int top = childView.getTop() - layoutParams.topMargin;
            int bottom = childView.getBottom() + layoutParams.bottomMargin;
            int left = childView.getRight() + layoutParams.rightMargin;
            int right = left + mDivider.getIntrinsicWidth();
            mDivider.setBounds(left, top, right, bottom);
            mDivider.draw(c);
        }
    }

    private void drawHorizontal(Canvas c, RecyclerView parent) {
        int childCount = parent.getChildCount();
        for (int i = 0; i < childCount; i++) {
            View childView = parent.getChildAt(i);
            RecyclerView.LayoutParams layoutParams = (RecyclerView.LayoutParams) childView.getLayoutParams();
            int left = childView.getLeft() - layoutParams.leftMargin;
            int right = childView.getRight() + layoutParams.rightMargin;
            int top = childView.getBottom() + layoutParams.bottomMargin;
            int bottom = top + mDivider.getIntrinsicHeight();
            mDivider.setBounds(left, top, right, bottom);
            mDivider.draw(c);
        }
    }

    /**
     * 基本操作 就是流出分割线的位置
     */
    @Override
    public void getItemOffsets(@NonNull Rect outRect, @NonNull View view, @NonNull RecyclerView parent, @NonNull RecyclerView.State state) {
//        super.getItemOffsets(outRect, view, parent, state);
        //获取当前的位置 分割线的位置
//        outRect.bottom = mDivider.getIntrinsicHeight();
//        outRect.right = mDivider.getIntrinsicWidth();
        //解决item的BUG问题
        int bottom = mDivider.getIntrinsicHeight();
        int right = mDivider.getIntrinsicWidth();
        if (isLastColumn(parent, view)) { //最后一列
            right = 0;
        }
        if (isLastRow(parent, view)) {
            bottom = 0;

        }
        outRect.bottom = bottom;
        outRect.right = right;
    }

    private boolean isLastColumn(RecyclerView parent, View view) {
        //获取当前位置
        int position = ((RecyclerView.LayoutParams) view.getLayoutParams()).getViewLayoutPosition();
        int spanCount = getSpanCount(parent);
        return (position + 1) % spanCount == 0;
    }

    /**
     * 获取RecycleView的列数
     *
     * @param parent recycleView
     * @return 列数
     */
    private int getSpanCount(RecyclerView parent) {
        //获取列数 GridLayoutManager
        RecyclerView.LayoutManager layoutManager = parent.getLayoutManager();
        if (layoutManager instanceof GridLayoutManager) {
            GridLayoutManager manager = (GridLayoutManager) layoutManager;
            int spanCount = manager.getSpanCount();
            return spanCount;  //当前位置跟一列的总数去求模
        }
        return 1;
    }

    /**
     * 当前行数是否为最后一行
     *
     * @param parent RecyclerView
     * @param view   当前View
     * @return true or false
     */
    private boolean isLastRow(RecyclerView parent, View view) {
        //当前的位置大于行数-1  去乘以列数
        //列数
        int spanCount = getSpanCount(parent);
        //行数
        int rowNum = (int) Math.ceil(parent.getChildCount() / spanCount);
        //当前的位置
        int position = ((RecyclerView.LayoutParams) view.getLayoutParams()).getViewLayoutPosition();
        return position + 1 > (rowNum - 1) * spanCount;
    }
}
