package com.sincerity.framelibrary.util

import android.annotation.TargetApi
import android.app.Activity
import android.content.Context
import android.os.Build
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager

/**
 * Created by Sincerity on 2019/12/3.
 * 描述：沉浸式状态栏
 */
object StateBarUtil {
    @TargetApi(19)
    fun stateBarTintColor(activity: Activity, color: Int) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            activity.window.statusBarColor = color
            return
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT && Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
            activity.window.addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
            val viewGroup = activity.findViewById<ViewGroup>(android.R.id.content)
            val contentView = viewGroup.getChildAt(0)
            if (contentView != null) {
                contentView.fitsSystemWindows = true
            }
            val statusBarView = createStatusBarView(activity)
            viewGroup.addView(statusBarView, 0)
            statusBarView.setBackgroundColor(color)

        }
    }

    /**
     * 创建一个需要填充的views
     */
    private fun createStatusBarView(activity: Activity): View {
        val view = View(activity)
        val params = ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, getStatusBatHeight(activity))
        view.layoutParams = params
        return view
    }

    /**
     * 获取状态栏的高度
     */
    private fun getStatusBatHeight(activity: Context): Int {
        var result = 0
        val resourceId = activity.resources.getIdentifier("status_bar_height", "dimen", "android")
        if (resourceId > 0) {
            result = activity.resources.getDimensionPixelSize(resourceId)
        }
        return result
    }

}