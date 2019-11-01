package com.sincerity.utilslibrary.view.BannerView;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import androidx.viewpager.widget.ViewPager;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.Gravity;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.sincerity.utilslibrary.R;

/**
 * Created by Sincerity on 2019/8/30.
 * 描述：自定义的BannerView 支持无限轮播
 */
public class BannerView extends RelativeLayout {
    private BannerViewPager mBannerVp;//bannerView
    private TextView mTvBannerDesc;//描述
    private LinearLayout mPointContainer;//指示器容器
    private BannerAdapter mBannerAdapter;//自定义的Banner的Adapter
    private Context mContext;
    private Drawable mIndicatorFocusDrawable;
    private Drawable mIndicatorNormalDrawable;
    private int mCurrentIndex = 0;
    private int mDotGravity = 0; //默认指示器的显示位置
    private int mDotSize = 8;//默认指示器的大小
    private int mDotSpacing = 2;//指示器的间隔
    private int mDotBottomColor = Color.TRANSPARENT; //默认底部颜色
    private RelativeLayout mBottomView;
    private float mWidthProportion, mHeightProportion;//宽高比例

    public BannerView(Context context) {
        this(context, null);
    }

    public BannerView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);

    }

    public BannerView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.mContext = context;
        //把布局加载到这个View中
        inflate(context, R.layout.view_banner, this);
        initAttributeSet(attrs);
        initViews();
        //设置默认的指示器颜色
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

    }

    private void initAttributeSet(AttributeSet attrs) {
        //获取自定义的属性
        TypedArray array = mContext.obtainStyledAttributes(attrs, R.styleable.BannerView);
        //获取指示器的位置 如果没有默认为左边
        mDotGravity = array.getInt(R.styleable.BannerView_dotIndicatorGravity, mDotGravity);
        //当前指示器的颜色
        mIndicatorFocusDrawable = array.getDrawable(R.styleable.BannerView_dotIndicatorFocus);
        if (mIndicatorFocusDrawable == null) {
            mIndicatorFocusDrawable = new ColorDrawable(Color.RED);
        }
        //正常指示器的颜色
        mIndicatorNormalDrawable = array.getDrawable(R.styleable.BannerView_dotIndicatorNormal);
        if (mIndicatorNormalDrawable == null) {
            mIndicatorNormalDrawable = new ColorDrawable(Color.WHITE);
        }
        //指示器的大小
        mDotSize = (int) array.getDimension(R.styleable.BannerView_dotIndicatorSize, dip2px(mDotSize));
        //指示器的间隔
        mDotSpacing = (int) array.getDimension(R.styleable.BannerView_dotIndicatorSpacing, dip2px(mDotSpacing));
        //指示器底部的背景颜色
        mDotBottomColor = array.getColor(R.styleable.BannerView_bottomColor, mDotBottomColor);
        mWidthProportion = array.getFloat(R.styleable.BannerView_widthProportion, mWidthProportion);
        mHeightProportion = array.getFloat(R.styleable.BannerView_heightProportion, mHeightProportion);
        array.recycle();
    }

    //初始化View
    private void initViews() {
        mBannerVp = findViewById(R.id.banner_vp);
        mTvBannerDesc = findViewById(R.id.banner_desc_tv);
        mPointContainer = findViewById(R.id.point_container);
        mBottomView = findViewById(R.id.bottom_view);
        mBottomView.setBackgroundColor(mDotBottomColor);
    }

    /**
     * @param mBannerAdapter 设置的Banner的适配器
     */
    public void setBannerAdapter(BannerAdapter mBannerAdapter) {
        this.mBannerAdapter = mBannerAdapter;
        mBannerVp.setAdapter(mBannerAdapter);
        initDotIndicator();//初始化点的指示器
        mBannerVp.addOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {
            @Override
            public void onPageSelected(int i) {
                //监听当前选中的位置
                pagerSelected(i);
            }
        });
        //初始化第一次的Banner描述
        String mFirstDesc = mBannerAdapter.getBannerDesc(0);
        mTvBannerDesc.setText(mFirstDesc);
        if (mWidthProportion == 0 || mHeightProportion == 0) {
            return;
        }
        //动态计算高度
        int width = getMeasuredWidth();
        int height = (int) (width * mHeightProportion / mWidthProportion);
        getLayoutParams().height = height;
    }

    private void pagerSelected(int position) {
        //把当前的点亮的位置变为默认
        DotIndicatorView oldIndicatorView = (DotIndicatorView) mPointContainer.getChildAt(mCurrentIndex);
        oldIndicatorView.setDrawable(mIndicatorNormalDrawable);
        //得到当前选中的position
        mCurrentIndex = position % mBannerAdapter.getCount();
        //选中的位置变为点亮
        DotIndicatorView currentIndicatorView = (DotIndicatorView) mPointContainer.getChildAt(mCurrentIndex);
        currentIndicatorView.setDrawable(mIndicatorFocusDrawable);
        //设置广告描述
        String bannerDesc = mBannerAdapter.getBannerDesc(mCurrentIndex);
        mTvBannerDesc.setText(bannerDesc);
    }

    private void initDotIndicator() {
        int count = mBannerAdapter.getCount();
        //设置指示器的位置在右边
        mPointContainer.setGravity(getDotGravity());
        //循环去添加点的指示器
        for (int i = 0; i < count; i++) {
            DotIndicatorView mDot = new DotIndicatorView(mContext);
            //设置大小
            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(mDotSize, mDotSize);
            //设置点的间隔
            layoutParams.leftMargin = layoutParams.rightMargin = dip2px(mDotSpacing);

            mDot.setLayoutParams(layoutParams);
            //设置指示器的颜色
            if (i == 0) {
                mDot.setDrawable(mIndicatorFocusDrawable);
            } else {
                mDot.setDrawable(mIndicatorNormalDrawable);
            }
            //把指示器添加到指示器容器中
            mPointContainer.addView(mDot);
        }
    }

    //获取指示器的位置
    private int getDotGravity() {
        switch (mDotGravity) {
            case 0:
                return Gravity.LEFT;
            case 1:
                return Gravity.CENTER;
            case 2:
                return Gravity.RIGHT;
        }
        return Gravity.LEFT;
    }

    //dp转px
    private int dip2px(int dp) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, getResources().getDisplayMetrics());
    }

    //开始自动滚动
    public void startAutoScroll() {
        mBannerVp.startSColl();
    }

    //设置自动滚动的速率
    public void setScrollerDuration(int i) {
        mBannerVp.setScrollerDuration(i);
    }

    //设置默认的滚动间隔
    public void setCurrentSecond(int i) {
        mBannerVp.setCurrentSecond(i);
    }
}
