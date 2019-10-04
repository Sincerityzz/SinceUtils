package com.sincerity.sinceutils;

import android.support.v4.app.Fragment;
import android.widget.RadioButton;

import com.sincerity.sinceutils.fragment.FoundPage;
import com.sincerity.sinceutils.fragment.HomePage;
import com.sincerity.sinceutils.fragment.MyInfoPage;
import com.sincerity.sinceutils.fragment.NewsPage;
import com.sincerity.utilslibrary.base.BaseActivity;
import com.sincerity.utilslibrary.ioc.BindView;
import com.sincerity.utilslibrary.ioc.OnClick;
import com.sincerity.utilslibrary.ioc.ViewUtils;
import com.sincerity.utilslibrary.utils.FragmentMangerHelper;

public class MainActivity extends BaseActivity {
    @BindView(R.id.home)
    RadioButton mHome;
    @BindView(R.id.news)
    RadioButton mNews;
    @BindView(R.id.found)
    RadioButton mFound;
    @BindView(R.id.myInfo)
    RadioButton mMy;
    private String url = "https://wanandroid.com/wxarticle/chapters/json";
    private String BannerUrl = "https://www.wanandroid.com/banner/json";
    private String TAG = MainActivity.class.getSimpleName();
    private FragmentMangerHelper mFragmentManger;
    private Fragment mHomePage, mNewsPage, mFoundPage, mMyInfoPage;

    @Override
    protected int setContentView() {
        return R.layout.activity_main;
    }

    @Override
    protected void initActionBar() {
        ViewUtils.bind(this);
    }

    @Override
    protected void initViews() {
        mFragmentManger = new FragmentMangerHelper(getSupportFragmentManager(), R.id.home_container);
        mHomePage = new HomePage();
        mFragmentManger.addFragment(mHomePage);
    }

    @Override
    protected void initListener() {

    }

    @OnClick(R.id.home)
    public void onHomeClick() {
        if (mHomePage == null) {
            mHomePage = new HomePage();
        }
        mFragmentManger.switchFragment(mHomePage);
    }

    @OnClick(R.id.news)
    public void onNewsClick() {
        if (mNewsPage == null) {
            mNewsPage = new NewsPage();
        }
        mFragmentManger.switchFragment(mNewsPage);
    }

    @OnClick(R.id.found)
    public void onFoundClick() {
        if (mFoundPage == null) {
            mFoundPage = new FoundPage();
        }
        mFragmentManger.switchFragment(mFoundPage);
    }

    @OnClick(R.id.myInfo)
    public void onMyInfoClick() {
        if (mMyInfoPage == null) {
            mMyInfoPage = new MyInfoPage();
        }
        mFragmentManger.switchFragment(mMyInfoPage);
    }

    @Override
    protected void initData() {

    }

}
