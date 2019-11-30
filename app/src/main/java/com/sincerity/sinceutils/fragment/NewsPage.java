package com.sincerity.sinceutils.fragment;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.sincerity.framelibrary.base.HttpCallBack;
import com.sincerity.sinceutils.bean.ImageBean;
import com.sincerity.sinceutils.R;
import com.sincerity.utilslibrary.httputils.HttpUtils;
import com.sincerity.utilslibrary.ioc.BindView;
import com.sincerity.utilslibrary.view.BannerView.BannerAdapter;
import com.sincerity.utilslibrary.view.BannerView.BannerViewPager;

import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Objects;

/**
 * Created by Sincerity on 2019/9/6.
 * 描述：
 */
public class NewsPage extends Fragment {
    private View mView;
    BannerViewPager mBannerView;
    @BindView(R.id.mNewsList)
    RecyclerView mRecycleView;
    private String BannerUrl = "https://www.wanandroid.com/banner/json";

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mView = LayoutInflater.from(getActivity()).inflate(R.layout.fragment_news, container, false);
        mBannerView = mView.findViewById(R.id.bannerView);
        initData();
        return mView;
    }

    private void initData() {
        HttpUtils.Companion.
                with(Objects.requireNonNull(getActivity()))
                .url(BannerUrl)
                .addParams()
                .post()
                .execute(new HttpCallBack<ImageBean>() {
                    @Override
                    public void onSuccess(ImageBean result) {

                        for (int i = 0; i < result.getData().size(); i++) {
                            Log.e("admin", "成功" + result.getData().get(i).toString());
                        }
                    }

                    @Override
                    public void onFail(@NotNull Exception exception) {
                        Log.e("admin", "失败" + Objects.requireNonNull(exception.getMessage()));
                    }
                });
//        initViewPager(beans);
    }

    private void initViewPager(final List<ImageBean> beans) {
        mBannerView.setAdapter(new BannerAdapter() {
            @Override
            public View getView(int position, View mConvertView) {
                ImageView imageView = new ImageView(getActivity());
                RequestOptions options = new RequestOptions()
                        .placeholder(R.mipmap.ic_launcher) //占位图
                        .error(R.mipmap.ic_launcher)//加载失败图
                        .circleCrop();//圆形
                Glide.with(NewsPage.this)
                        .load(beans.get(0).getData().get(position).getImagePath())
                        .apply(options)
                        .centerCrop()
                        .into(imageView);
                return imageView;
            }

            @Override
            public int getCount() {
                return beans.size();
            }

            @Override
            public String getBannerDesc(int position) {
                return beans.get(0).getData().get(position).getTitle();
            }
        });
        mBannerView.setScrollerDuration(500); //设置动画的播放速率
        mBannerView.setCurrentSecond(3500); //设置间隔
    }
}
