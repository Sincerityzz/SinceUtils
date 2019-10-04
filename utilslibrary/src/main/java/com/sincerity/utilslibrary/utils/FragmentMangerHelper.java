package com.sincerity.utilslibrary.utils;

import android.support.annotation.IdRes;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;

import java.util.List;

/**
 * Created by Sincerity on 2019/9/4.
 * 描述： 控制Fragment的添加和显示的工具类
 */
public class FragmentMangerHelper {
    private FragmentManager mFragmentManager;
    private int mContainerViewId;

    public FragmentMangerHelper(FragmentManager mFragmentManager, @IdRes int mContainerViewId) {
        this.mFragmentManager = mFragmentManager;
        this.mContainerViewId = mContainerViewId;
    }

    /**
     * 添加Fragment
     *
     * @param fragment 目标Fragment
     */
    public void addFragment(Fragment fragment) {
        //开启一个事务
        FragmentTransaction fragmentTransaction = mFragmentManager.beginTransaction();

        //添加fragment
        fragmentTransaction.add(mContainerViewId, fragment);
        //提交这个事务
        fragmentTransaction.commit();
    }

    /**
     * 添加或者显示Fragment
     *
     * @param fragment 目标Fragment
     */
    public void switchFragment(Fragment fragment) {
        FragmentTransaction fragmentTransaction = mFragmentManager.beginTransaction();
        //首先拿到所有的Fragment 先隐藏
        List<Fragment> childFragments = mFragmentManager.getFragments();
        for (Fragment childFragment : childFragments) {
            fragmentTransaction.hide(childFragment);
        }
        //如果已经添加过Fragment就显示,否则就添加
        if (!childFragments.contains(fragment)) {
            fragmentTransaction.add(mContainerViewId, fragment);
        } else {
            fragmentTransaction.show(fragment);
        }
        fragmentTransaction.commit();
    }
}
