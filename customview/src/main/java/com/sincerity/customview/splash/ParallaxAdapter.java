package com.sincerity.customview.splash;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import java.util.List;

/**
 * Created by Sincerity on 2019/12/16.
 * 描述：平行控件
 */
public class ParallaxAdapter extends FragmentPagerAdapter {
    private List<SplashFragment> fragments;

    public ParallaxAdapter(@NonNull FragmentManager fm, int behavior, List<SplashFragment> fragments) {
        super(fm, behavior);
        this.fragments = fragments;
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        return fragments.get(position);
    }

    @Override
    public int getCount() {
        return fragments.size();
    }
}
