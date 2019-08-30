package com.sincerity.sinceutils;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.sincerity.utilslibrary.http.ResponseEntity;
import com.sincerity.utilslibrary.http.SVolley;
import com.sincerity.utilslibrary.ioc.BindView;
import com.sincerity.utilslibrary.ioc.ViewUtils;
import com.sincerity.utilslibrary.view.BannerView.BannerAdapter;
import com.sincerity.utilslibrary.view.BannerView.BannerView;

public class MainActivity extends AppCompatActivity {
    @BindView(R.id.banner_view)
    BannerView mBannerView;

    private String url = "https://wanandroid.com/wxarticle/chapters/json";
    private String BannerUrl = "https://www.wanandroid.com/banner/json";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ViewUtils.bind(this);
        SVolley.sendJsonRequest(BannerUrl, null, new ResponseEntity() {
            @Override
            public void onSuccess(Object object) {
                ImageBean imageBeanList = new Gson().fromJson(object.toString(), new TypeToken<ImageBean>() {
                }.getType());
                initBannerView(imageBeanList);
            }

            @Override
            public void onFail(String errorString) {

            }
        });
    }

    private void initBannerView(final ImageBean bean) {
        mBannerView.setBannerAdapter(new BannerAdapter() {
            @Override
            public View getView(int position) {
                ImageView view = new ImageView(MainActivity.this);
                String imagePath = bean.getData().get(position).getImagePath();
                Glide.with(MainActivity.this).load(imagePath).centerCrop().into(view);
                return view;
            }

            @Override
            public int getCount() {
                return bean.getData().size();
            }

            @Override
            public String getBannerDesc(int position) {
                return bean.getData().get(position).getTitle();
            }
        });
        mBannerView.setScrollerDuration(950); //设置动画的播放速率
        mBannerView.setCurrentSecond(3500); //设置间隔
        mBannerView.startAutoScroll();//开启无限轮播
    }

}
