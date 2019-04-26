package com.sincerity.utilslibrary.ioc;

import android.app.Activity;
import android.view.View;

/**
 * Created by Sincerity on 2019/4/9.
 * 描述：辅助类 用来返回一个View的视图
 */
public class ViewFinder {
    private View view;
    private Activity mActivity;

    public ViewFinder(View view) {
        this.view = view;
    }

    public ViewFinder(Activity mActivity) {
        this.mActivity = mActivity;
    }

    public View findById(int viewId) {
        return mActivity != null ? mActivity.findViewById(viewId) : view.findViewById(viewId);
    }
}
