package com.sincerity.utilslibrary.utils;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.os.Build;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;

/**
 * Created by Sincerity on 2019/12/17.
 * 描述：沉浸式支持
 */
public class ImmersiveUtils {
    private Activity mActivity;

    public static ImmersiveUtils getInstance() {
        final ImmersiveUtils immersiveUtils = new ImmersiveUtils();
        return immersiveUtils;
    }

    public void setTargetView(View toolbar) {
        if (mActivity != null) {
            setHeightAndPadding(mActivity, toolbar);
        }
    }

    private int getStatusBarHeight(Context context) {
        int id = context.getResources().getIdentifier("status_bar_height", "dimen", "android");
        if (id > 0) {
            return context.getResources().getDimensionPixelOffset(id);
        }
        return 0;
    }

    private void setHeightAndPadding(Activity context, View view) {
        if (view != null) {
            ViewGroup.LayoutParams layoutParams = view.getLayoutParams();
            layoutParams.height += getStatusBarHeight(context);
            view.setPadding(view.getPaddingLeft(),
                    view.getPaddingTop() + getStatusBarHeight(context), view.getPaddingRight(), view.getPaddingBottom());
        }

    }

    public ImmersiveUtils with(Activity activity) {
        this.mActivity = activity;
        if (Build.VERSION.SDK_INT == Build.VERSION_CODES.KITKAT) return null;
        if (Build.VERSION.SDK_INT == Build.VERSION_CODES.LOLLIPOP) {
            Window window = activity.getWindow();
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(Color.TRANSPARENT);
            int i = window.getDecorView().getSystemUiVisibility();
            i |= View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN;
            i |= View.SYSTEM_UI_FLAG_LAYOUT_STABLE;
            //隐藏虚拟导航栏
            i |= View.SYSTEM_UI_FLAG_HIDE_NAVIGATION;
            window.getDecorView().setSystemUiVisibility(i);
        } else {
            activity.getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        }
        return this;
    }
}
