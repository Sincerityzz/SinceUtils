package com.sincerity.sinceutils.ui;

import android.widget.RadioButton;

import androidx.fragment.app.Fragment;

import com.sincerity.sinceutils.R;
import com.sincerity.sinceutils.fragment.FoundPage;
import com.sincerity.sinceutils.fragment.HomePage;
import com.sincerity.sinceutils.fragment.MyInfoPage;
import com.sincerity.sinceutils.fragment.NewsPage;
import com.sincerity.utilslibrary.exception.ExceptionCrashHandler;
import com.sincerity.utilslibrary.base.BaseActivity;
import com.sincerity.utilslibrary.ioc.BindView;
import com.sincerity.utilslibrary.ioc.OnClick;
import com.sincerity.utilslibrary.ioc.ViewUtils;
import com.sincerity.utilslibrary.utils.FragmentMangerHelper;

import java.io.File;

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
        //获取上次异常的信息 上传到服务器
        File crashFile = ExceptionCrashHandler.getInstance().getCrashFile();
        if (crashFile.exists()) {
            //上传到服务器
//            try {
//                InputStreamReader reader = new InputStreamReader(new FileInputStream(crashFile));
//                char[] buffer = new char[1024];
//                int len = 0;
//                while ((len = reader.read(buffer)) != -1) {
//                    String str = new String(buffer, 0, len);
//                    Log.e("admin", str);
//                }
//            } catch (FileNotFoundException e) {
//                e.printStackTrace();
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
        }
    }

}
