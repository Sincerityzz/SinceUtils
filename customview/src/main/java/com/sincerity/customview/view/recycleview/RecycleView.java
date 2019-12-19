package com.sincerity.customview.view.recycleview;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.ViewGroup;

import com.sincerity.customview.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Sincerity on 2019/12/18.
 * 描述：
 */
public class RecycleView extends ViewGroup {
    private Adapter adapter;
    private List<View> viewList;//当前显示的View
    private int currentY;//
    private int rowCount;
    private int firstRow;
    private int scrollY;
    private boolean needRelayout;
    private int width;
    private int height;
    private int[] heights;
    Recycler recycler;
    private int touchSlop;

    public Adapter getAdapter() {
        return adapter;
    }

    public void setAdapter(Adapter adapter) {
        this.adapter = adapter;
        if (adapter != null) {
            recycler = new Recycler(adapter.getItemViewTypeCount());
            scrollY = 0;
            firstRow = 0;
            needRelayout = true;
            requestLayout();
        }

    }

    public RecycleView(Context context) {
        this(context, null);
    }

    public RecycleView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public RecycleView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }

    private void init(Context context, AttributeSet attrs) {
        ViewConfiguration viewConfiguration = ViewConfiguration.get(context);
        this.touchSlop = viewConfiguration.getScaledTouchSlop();
        this.viewList = new ArrayList<>();
        this.needRelayout = true;
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        if (needRelayout || changed) {
            needRelayout = false;
            viewList.clear();
            removeAllViews();
            if (adapter != null) {
                width = r - l;
                height = b - t;
                int top = 0, bottom;
                for (int i = 0; i < rowCount && top < height; i++) {
                    bottom = top + heights[i];
                    View view = makeAndStep(i, 0, top, width, bottom);
                    viewList.add(view);
                    top = bottom;
                }
            }
        }
    }

    private View makeAndStep(int row, int left, int top, int right, int bottom) {
        View view = obtainView(row, right - left, bottom - top);
        view.layout(left, top, right, bottom);
        return view;
    }

    private View obtainView(int row, int width, int height) {
        int type = adapter.getItemViewType(row);
        View recycleView = recycler.get(type);
        View view;
        if (recycleView == null) {
            view = adapter.onCreateViewHolder(row, recycleView, this);
            if (view == null) {
                throw new RuntimeException("onCreateViewHolder mast be to layout in your RecycleView");
            }
        } else {
            view = adapter.onBindViewHolder(row, recycleView, this);
        }
        view.setTag(R.id.tag_type_view, type);
        view.measure(MeasureSpec.makeMeasureSpec(width, MeasureSpec.EXACTLY),
                MeasureSpec.makeMeasureSpec(height, MeasureSpec.EXACTLY));
        addView(view, 0);
        return view;
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        final int widthSize = MeasureSpec.getSize(widthMeasureSpec);
        final int heightSize = MeasureSpec.getSize(heightMeasureSpec);
        int h = 0;
        if (adapter != null) {
            this.rowCount = adapter.getCount();
            heights = new int[rowCount];
            for (int i = 0; i < heights.length; i++) {
                heights[i] = adapter.getLineHeight();
            }
        }
        int tempH = sunArray(heights, 0, heights.length);
        h = Math.min(heightSize, tempH);
        setMeasuredDimension(widthSize, h);
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    private int sunArray(int array[], int firstIndex, int count) {
        int sum = 0;
        count += firstIndex;
        for (int i = firstIndex; i < count; i++) {
            sum += array[i];
        }
        return sum;
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent event) {
        Log.e("onInterceptTouchEvent", event.getAction() + "----");
        boolean intercept = false;
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN: {
                currentY = (int) event.getRawY();
                break;
            }
            case MotionEvent.ACTION_MOVE: {
                // 当手指在屏幕上滑动距离大于 touchSlop 最小滑动距离时，则继承ViewGroup的 RecyclerView 拦截事件
                int y2 = Math.abs(currentY - (int) event.getRawY());
                if (y2 > touchSlop) {
                    intercept = true;
                }
            }
        }
        return intercept;
    }
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        Log.e("onTouchEvent", event.getAction() + "----");
        switch (event.getAction()) {
            case MotionEvent.ACTION_MOVE: {
                // 移动的距离   y方向
                int y2 = (int) event.getRawY();
                // 大于0表示 上滑
                // 小于0表示 下滑
                int diffY = currentY - y2;
                // 画布移动  并不影响子控件的位置
                scrollBy(0, diffY);
            }
        }
        return super.onTouchEvent(event);
    }


    @Override
    public void scrollBy(int x, int y) {
        scrollY += y;
        scrollY = scrollBounds(scrollY);
        // scrollY
        if (scrollY > 0) {
            // 上滑正  下滑负  边界值
            while (scrollY > heights[firstRow]) {
                // 1 上滑移除  2 上划加载  3下滑移除  4 下滑加载
                removeView(viewList.remove(0));
                scrollY -= heights[firstRow];
                firstRow++;
            }
            // 上滑添加
            while (getFillHeight() < height) {
                int addLast = firstRow + viewList.size();
                View view = obtainView(addLast, width, heights[addLast]);
                viewList.add(viewList.size(), view);
            }
        } else if (scrollY < 0) {
            // 下滑加载
            while (scrollY < 0) {
                int firstAddRow = firstRow - 1;
                View view = obtainView(firstAddRow, width, heights[firstAddRow]);
                viewList.add(0, view);
                firstRow--;
                scrollY += heights[firstRow + 1];
            }
            // 下滑移除
            while (sunArray(heights, firstRow, viewList.size()) - scrollY - heights[firstRow + viewList.size() - 1] >= height) {
                removeView(viewList.remove(viewList.size() - 1));
            }
        }
        repositionViews();
    }

    private int scrollBounds(int scrollY) {
        // 上滑
        if (scrollY > 0) {
        } else {
            // 极限值  会取零  非极限值的情况下   scroll
            scrollY = Math.max(scrollY, -sunArray(heights, 0, firstRow));
        }
        return scrollY;
    }

    private void repositionViews() {
        int left, top, right, bottom, i;
        top = -scrollY;
        i = firstRow;
        for (View view : viewList) {
            bottom = top + heights[i++];
            view.layout(0, top, width, bottom);
            top = bottom;
        }
    }


    private int getFillHeight() {
        return sunArray(heights, firstRow, viewList.size()) - scrollY;
    }

    @Override
    public void removeView(View view) {
        super.removeView(view);
        int tag = (int) view.getTag(R.id.tag_type_view);
        recycler.put(view, tag);
    }


    public interface Adapter {
        View onCreateViewHolder(int position, View convertView, ViewGroup parent);

        View onBindViewHolder(int position, View convertView, ViewGroup parent);

        int getItemViewType(int row);

        int getItemViewTypeCount();

        int getCount();

        int getLineHeight();
    }
}
