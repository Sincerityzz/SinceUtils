package com.sincerity.customview.splash;

import android.os.Bundle;
import android.widget.ImageView;

import androidx.fragment.app.FragmentActivity;

import com.sincerity.customview.R;

//平行控件
public class SplashActivity extends FragmentActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        ParallaxContainer container = (ParallaxContainer) findViewById(R.id.iv_main);
        container.setUp(R.layout.view_intro_1, R.layout.view_intro_2, R.layout.view_intro_3,
                R.layout.view_intro_4, R.layout.view_intro_5, R.layout.view_intro_6, R.layout.view_login);
        ImageView imageView = findViewById(R.id.iv_man);
        imageView.setBackgroundResource(R.drawable.man_run);
        container.setIv_man(imageView);

    }
}
