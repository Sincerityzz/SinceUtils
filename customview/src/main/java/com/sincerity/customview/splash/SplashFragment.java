package com.sincerity.customview.splash;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * Created by Sincerity on 2019/12/16.
 * 描述：
 */
public class SplashFragment extends Fragment {
    private List<View> parallaxViews = new ArrayList<>();

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        Bundle bundle = getArguments();
        int layoutId = Objects.requireNonNull(bundle).getInt("LayoutId");
        ParallaxLayoutInflater layoutInflater = new ParallaxLayoutInflater(inflater, getActivity(), this);
        return layoutInflater.inflate(layoutId, null);
    }

    public List<View> getParallaxViews() {
        return parallaxViews;
    }
}
