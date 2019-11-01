package com.sincerity.utilslibrary.view.indicator;

import android.content.Context;
import android.content.res.TypedArray;
import androidx.viewpager.widget.ViewPager;
import android.util.AttributeSet;
import android.view.View;
import android.widget.HorizontalScrollView;

import com.sincerity.utilslibrary.R;

/**
 * Created by Sincerity on 2019/9/24.
 * 描述：
 */
public class TrackIndicatorView extends HorizontalScrollView implements ViewPager.OnPageChangeListener {
    private IndicatorAdapter mAdapter;
    private IndicatorGroupView mIndicatorGroup;//指示器容器
    private int mTabVisibleNum = 4;//默认显示的条目数
    private int mItemWidth;
    private ViewPager mViewPager;
    private int mCurrentPosition = 0;
    private boolean mIsExecuteScroll = false; //点击抖动问题
    private boolean mSmoothScroll;

    public TrackIndicatorView(Context context) {
        this(context, null);
    }

    public TrackIndicatorView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public TrackIndicatorView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mIndicatorGroup = new IndicatorGroupView(context);
        addView(mIndicatorGroup);
        //自定义Item的宽度
        initAttrs(context, attrs);
    }

    private void initAttrs(Context context, AttributeSet attrs) {
        TypedArray array = context.obtainStyledAttributes(attrs, R.styleable.TrackIndicatorView);
        mTabVisibleNum = array.getInteger(R.styleable.TrackIndicatorView_tabWidth, mTabVisibleNum);
        array.recycle();
    }

    /**
     *  设置适配器
     * @param adapter 适配器
     * @param viewPager viewpager
     */
    public void setAdapter(IndicatorAdapter adapter, ViewPager viewPager) {
        setAdapter(mAdapter, mViewPager, true);
    }

    /**
     *  设置适配器
     * @param adapter 适配器
     * @param viewPager viewpager
     * @param b 是否平滑滚动
     */
    public void setAdapter(IndicatorAdapter adapter, ViewPager viewPager, boolean b) {
        this.mSmoothScroll = b;
        if (viewPager == null) {
            throw new NullPointerException("viewPager is null!");
        }
        mViewPager = viewPager;
        mViewPager.addOnPageChangeListener(this);
        setAdapter(adapter);
    }

    /**
     * 适配器
     *
     * @param adapter 适配器
     */
    public void setAdapter(IndicatorAdapter adapter) {
        if (adapter == null) {
            throw new NullPointerException("Adapter can not null,please setAdapter!!");
        }
        this.mAdapter = adapter;
        int mItemCount = mAdapter.getCount();
        for (int i = 0; i < mItemCount; i++) {
            View itemView = mAdapter.getView(i, mIndicatorGroup);
            mIndicatorGroup.addItemView(itemView);
            //设置点击事件
            switchItemClick(itemView, i);
        }
        mAdapter.highlightTheCurrent(mIndicatorGroup.getItem(0));

    }

    /**
     * 设置条目的点击事件和联动
     *
     * @param itemView 条目
     * @param i        位置
     */
    private void switchItemClick(View itemView, final int i) {
        itemView.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                mViewPager.setCurrentItem(i,mSmoothScroll);
                //移动IndicatorView
                smoothScrollIndicator(i);
                //移动下标
                mIndicatorGroup.scrollBottomTrack(i);
            }
        });
    }

    /**
     * 点击移动 带动画
     *
     * @param i 当前下标
     */
    private void smoothScrollIndicator(int i) {
        //总长度
        float totalScroll = (i) * mItemWidth;
        //左边的偏移
        float offsetScroll = (getWidth() - mItemWidth) >> 1;
        //最终的偏移量
        final int finalScroll = (int) (totalScroll - offsetScroll);
        smoothScrollTo(finalScroll, 0);
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        super.onLayout(changed, l, t, r, b);
        if (changed) {
            //指定宽度
            mItemWidth = getItemWidth();
            for (int i = 0; i < mAdapter.getCount(); i++) {
                mIndicatorGroup.getItem(i).getLayoutParams().width = mItemWidth;
            }
            mIndicatorGroup.addBottomTrackView(mAdapter.getTrackView(), mItemWidth);
        }
    }

    private int getItemWidth() {
        int parentWidth = getWidth();
        //已经指定显示的个数
        if (mTabVisibleNum != 0) {
            return parentWidth / mTabVisibleNum;
        }
        // 没有指定显示的个数
        int itemWidth;
        // 获取最宽的
        int maxItemWidth = 0;

        for (int i = 0; i < mAdapter.getCount(); i++) {
            int currentItemWidth = mIndicatorGroup.getItem(i).getMeasuredWidth();
            maxItemWidth = Math.max(currentItemWidth, maxItemWidth);
        }
        // 宽度就是获取最宽的一个
        itemWidth = maxItemWidth;

        int allWidth = mAdapter.getCount() * itemWidth;
        // 最后算一次所有条目宽度相加是不是大于一屏幕
        if (allWidth < parentWidth) {
            itemWidth = parentWidth / mAdapter.getCount();
        }
        return itemWidth;
    }

    @Override
    public void onPageScrolled(int i, float v, int i1) {
        if (mIsExecuteScroll) {
            //滚动时调用
            scrollIndicator(i, v);
            mIndicatorGroup.scrollBottomTrack(i, v);
        }
    }


    @Override
    public void onPageSelected(int i) {
        //重置当前位置
        mAdapter.resetColor(mIndicatorGroup.getItem(mCurrentPosition));
        mCurrentPosition = i;
        //高亮当前位置
        mAdapter.highlightTheCurrent(mIndicatorGroup.getItem(mCurrentPosition));
    }

    @Override
    public void onPageScrollStateChanged(int i) {
        if (i == 1) {
            mIsExecuteScroll = true;
        }
        if (i == 0) {
            mIsExecuteScroll = false;
        }
    }

    private void scrollIndicator(int i, float v) {
        //总长度
        float totalScroll = (i + v) * mItemWidth;
        //左边的偏移
        float offsetScroll = (getWidth() - mItemWidth) >> 1;
        //最终的偏移量
        final int finalScroll = (int) (totalScroll - offsetScroll);
        scrollTo(finalScroll, 0);
    }

}
