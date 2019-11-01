package com.sincerity.utilslibrary.view.BannerView;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.os.Handler;
import android.os.Message;
import androidx.annotation.NonNull;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateInterpolator;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;


/**
 * Created by Sincerity on 2019/8/29.
 * 描述：
 */
public class BannerViewPager extends ViewPager {
    private BannerAdapter mAdapter;
    private final int MSG_SCROLLER = 0X0011;
    private int mCurrentSecond = 2000;
    private static String TAG = "BannerView";
    /*自定义页面切换的速率*/
    private BannerScroller scroller;
    //界面的复用 内存优化仿照ListView的Item复用
    private List<View> mConvertViews;
    private Activity mActivity; //内存优化当前的Activity

    /**
     * @param mCurrentSecond 设置滚动的间隔时间
     */
    public void setCurrentSecond(int mCurrentSecond) {
        this.mCurrentSecond = mCurrentSecond;
    }

    /**
     * Handler会引发内存泄漏
     */
    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            setCurrentItem(getCurrentItem() + 1);
            startSColl();
        }
    };


    public BannerViewPager(Context context) {
        this(context, null);
    }

    public BannerViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
        mActivity = (Activity) context;
        //改变ViewPager的切换速率 反射
        try {
            Field field = ViewPager.class.getDeclaredField("mScroller");
            /* 第一个参数是当前对象,第二个是一个插值器*/
            scroller = new BannerScroller(context, new AccelerateInterpolator());
            field.setAccessible(true);//强制改变私有属性
            /*第一个参数代表参数在哪个类第二个参数代表要设置的值*/
            field.set(this, scroller);
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        mConvertViews = new ArrayList<>();
    }

    /**
     * @param mScrollerDuration 滚动的速率
     */
    public void setScrollerDuration(int mScrollerDuration) {
        scroller.setScrollerDuration(mScrollerDuration);
    }

    /**
     * 设置适配器
     *
     * @param adapter
     */
    public void setAdapter(BannerAdapter adapter) {
        this.mAdapter = adapter;
        setAdapter(new BannerPagerAdapter());
        //管理Activity的生命周期
        mActivity.getApplication().registerActivityLifecycleCallbacks(callbacks);
    }

    /**
     * 自动滚动
     */
    public void startSColl() {
        mHandler.removeMessages(MSG_SCROLLER);
        mHandler.sendEmptyMessageDelayed(MSG_SCROLLER, mCurrentSecond);

    }

    //创建适配器
    private class BannerPagerAdapter extends PagerAdapter {

        @Override
        public int getCount() {
            //为了实现无限循环
            return Integer.MAX_VALUE;
        }

        @Override
        public boolean isViewFromObject(@NonNull View view, @NonNull Object o) {
            //ViewPager里面对每个页面的管理是key-value形式的，
            // 也就是说每个page都有个对应的id（id是object类型），
            // 需要对page操作的时候都是通过id来完成的
            return view == o;
        }

        /**
         * 创建ItemView的方法
         *
         * @param container viewPager
         * @param position  当前的位置
         * @return
         */
        @NonNull
        @Override
        public Object instantiateItem(@NonNull ViewGroup container, int position) {
            //采用Adapter适配模式,为了完全让用户自定义   position的变化值为0-65566
            View ItemView = mAdapter.getView(position % mAdapter.getCount(), getConvertView());
            container.addView(ItemView);
            return ItemView;
        }

        /**
         * 销毁ItemView的方法
         *
         * @param container ViewPager
         * @param position  当前位置
         * @param object    ItemView
         */
        @Override
        public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
            container.removeView((View) object);
            mConvertViews.add((View) object);
        }
    }

    /**
     * 获取复用的界面
     *
     * @return View
     */
    private View getConvertView() {
        /**
         * The specified child already has a parent.
         * You must call removeView() on the child's parent first.
         */
        for (int i = 0; i < mConvertViews.size(); i++) {
            //获取没有添加在ViewPager的界面
            if (mConvertViews.get(i).getParent() == null) {
                return mConvertViews.get(i);
            }
        }
        return null;
    }

    /**
     * 处理内存泄露
     * 当Activity销毁后会回调这个方法
     */
    @Override
    protected void onDetachedFromWindow() {
        //移除消息置空mHandler
        mHandler.removeMessages(MSG_SCROLLER);
        mHandler = null;
        //反注册Activity的生命周期监听
        mActivity.getApplication().unregisterActivityLifecycleCallbacks(callbacks);
        super.onDetachedFromWindow();
    }

    /**
     * 添加对Activity的生命周期的监听
     */
    Application.ActivityLifecycleCallbacks callbacks = new DefaultActivityLifecycleCallbacks() {
        @Override
        public void onActivityResumed(Activity activity) {
            //判断是不是当前的Activity生命周期
            if (activity == getContext())
                //开始轮播
                mHandler.sendEmptyMessageDelayed(mCurrentSecond, MSG_SCROLLER);
        }

        @Override
        public void onActivityPaused(Activity activity) {
            if (activity == getContext())
                //停止轮播
                mHandler.removeMessages(MSG_SCROLLER);
        }
    };
}
