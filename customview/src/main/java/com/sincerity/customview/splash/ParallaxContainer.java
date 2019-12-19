package com.sincerity.customview.splash;

import android.content.Context;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.viewpager.widget.ViewPager;

import com.nineoldandroids.view.ViewHelper;
import com.sincerity.customview.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Sincerity on 2019/12/16.
 * 描述：
 */
public class ParallaxContainer extends FrameLayout implements ViewPager.OnPageChangeListener {
    private List<SplashFragment> fragments;
    private ImageView iv_man;
    private ParallaxAdapter adapter;

    public void setIv_man(ImageView iv_man) {
        this.iv_man = iv_man;
    }

    public ParallaxContainer(@NonNull Context context) {
        super(context);
    }

    public ParallaxContainer(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public ParallaxContainer(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public void setUp(int... childIds) {
        fragments = new ArrayList<>();
        for (int childId : childIds) {
            SplashFragment fragment = new SplashFragment();
            Bundle bundle = new Bundle();
            bundle.putInt("LayoutId", childId);
            fragment.setArguments(bundle);
            fragments.add(fragment);
        }
        ViewPager viewPager = new ViewPager(getContext());
        viewPager.setId(R.id.parallax_pager);
        viewPager.setLayoutParams(new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT));
        SplashActivity activity = (SplashActivity) getContext();
        adapter = new ParallaxAdapter(activity.getSupportFragmentManager(), 1, fragments);
        viewPager.setAdapter(adapter);
        viewPager.addOnPageChangeListener(this);
        addView(viewPager, 0);

    }

    /**
     * @param position             页面的下标
     * @param positionOffset       下标偏移
     * @param positionOffsetPixels 偏移下属距离
     */
    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
        Log.e("TAG", positionOffset + "---" + positionOffsetPixels);
        //做动画
        int mWidth = getWidth();
        SplashFragment inFragment = null;
        SplashFragment outFragment = null;
        try {
            outFragment = fragments.get(position - 1);
            inFragment = fragments.get(position);
        } catch (Exception ignored) {

        }
        if (outFragment != null) {
            List<View> views = outFragment.getParallaxViews();
            //动画
            if (views != null) {
                for (View view : views) {
                    ParallaxViewTag tag = (ParallaxViewTag) view.getTag(R.id.parallax_view);
                    if (tag == null) {
                        continue;
                    }
                    //X平移
                    ViewHelper.setTranslationX(view, (mWidth - positionOffsetPixels) * tag.xIn);
                    ViewHelper.setTranslationY(view, (mWidth - positionOffsetPixels) * tag.yIn);
                }
            }
        }
        if (inFragment != null) {
            List<View> views = inFragment.getParallaxViews();
            //动画
            if (views != null) {
                for (View view : views) {
                    ParallaxViewTag tag = (ParallaxViewTag) view.getTag(R.id.parallax_view);
                    if (tag == null) {
                        continue;
                    }
                    //Y平移 向右移
                    ViewHelper.setTranslationX(view, (0 - positionOffsetPixels) * tag.xOut);
                    ViewHelper.setTranslationY(view, (0 - positionOffsetPixels) * tag.yOut);
                }
            }
        }
    }

    @Override
    public void onPageSelected(int position) {
        if (position == adapter.getCount() - 1) {
            iv_man.setVisibility(INVISIBLE);
        } else {
            iv_man.setVisibility(VISIBLE);
        }
    }

    @Override
    public void onPageScrollStateChanged(int state) {
        AnimationDrawable drawable = (AnimationDrawable) iv_man.getBackground();
        switch (state) {
            case ViewPager.SCROLL_STATE_DRAGGING:
                drawable.start();
                break;
            case ViewPager.SCROLL_STATE_IDLE:
                drawable.stop();
                break;
        }
    }
}
