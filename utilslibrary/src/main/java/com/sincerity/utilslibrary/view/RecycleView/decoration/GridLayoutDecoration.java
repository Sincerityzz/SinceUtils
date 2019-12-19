package com.sincerity.utilslibrary.view.RecycleView.decoration;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

/**
 * Created by Sincerity on 2019/10/5.
 * 描述：Grid
 */
public class GridLayoutDecoration extends RecyclerView.ItemDecoration {
    private Drawable mDivide;
    private static final int[] ATTRS = new int[]{android.R.attr.listDivider};
    private int size =0;

    public GridLayoutDecoration(Context context, int drawableResourceId) {
        if (drawableResourceId == 0) {
            final TypedArray a = context.obtainStyledAttributes(ATTRS);
            mDivide = a.getDrawable(0);
            a.recycle();
            return;
        }
        // 获取 Drawable
        mDivide = ContextCompat.getDrawable(context, drawableResourceId);
    }

    @Override
    public void getItemOffsets(@NonNull Rect outRect, @NonNull View view, @NonNull RecyclerView parent, @NonNull RecyclerView.State state) {

        //留出位置 下面和右边
        RecyclerView.LayoutManager layoutManager = parent.getLayoutManager();
        int spanCount = getColumnCount(layoutManager);

        if (layoutManager instanceof GridLayoutManager) {
            int childLayoutPosition = parent.getChildLayoutPosition(view);
            //判断是不是最后一列
            if ((childLayoutPosition + 1) % spanCount != 0) {
                outRect.right = mDivide.getIntrinsicWidth();
            }
        }

        //最后一行 当前位置 >（行数 - 1） * 列数
        int itemCount = parent.getAdapter().getItemCount();

        //计算有多少行 不能整除的时候需要加 1
        int rowCount = itemCount % spanCount == 0 ? itemCount / spanCount : itemCount / spanCount + 1;

        //获取当前位置
        int mCurrentPosition = parent.getChildLayoutPosition(view);

        if (mCurrentPosition < (rowCount - 1) * spanCount) {
            outRect.bottom = mDivide.getIntrinsicHeight();
        }

    }

    /**
     * 获取列数
     *
     * @param layoutManager
     * @return
     */
    private int getColumnCount(RecyclerView.LayoutManager layoutManager) {
        if (layoutManager instanceof GridLayoutManager) {
            GridLayoutManager lm = (GridLayoutManager) layoutManager;
            //获取列数
            return lm.getSpanCount();
        }
        return 1;
    }

    /**
     * 绘制分割线
     *
     * @param c
     * @param parent
     * @param state
     */
    @Override
    public void onDraw(@NonNull Canvas c, @NonNull RecyclerView parent, @NonNull RecyclerView.State state) {
        int childCount = parent.getChildCount();
        drawHorizontal(c, parent);
        drawVertical(c, parent);
    }


    /**
     * 绘制垂直方向的分隔线
     *
     * @param c
     * @param parent
     */
    private void drawVertical(Canvas c, RecyclerView parent) {
        int childCount = parent.getChildCount();
        for (int i = 0; i < childCount; i++) {
            View childView = parent.getChildAt(i);
            RecyclerView.LayoutParams params = (RecyclerView.LayoutParams) childView.getLayoutParams();
            int left = childView.getRight() + params.rightMargin;
            int right = left + mDivide.getIntrinsicWidth();
            int top = childView.getTop() - params.topMargin;
            int bottom = childView.getBottom() + params.bottomMargin;
            mDivide.setBounds(new Rect(left, top, right, bottom));
            mDivide.draw(c);
        }
    }

    /**
     * 绘制水平方向的分隔线
     *
     * @param c
     * @param parent
     */
    private void drawHorizontal(Canvas c, RecyclerView parent) {
        int childCount = parent.getChildCount();
        for (int i = 0; i < childCount; i++) {
            View childView = parent.getChildAt(i);
            RecyclerView.LayoutParams params = (RecyclerView.LayoutParams) childView.getLayoutParams();
            int left = childView.getLeft() - params.leftMargin;
            int right = childView.getRight() + mDivide.getIntrinsicWidth() + params.rightMargin;
            int top = childView.getBottom() + params.bottomMargin;
            int bottom = top + mDivide.getIntrinsicHeight();
            mDivide.setBounds(new Rect(left, top, right, bottom));
            mDivide.draw(c);
        }
    }

}
