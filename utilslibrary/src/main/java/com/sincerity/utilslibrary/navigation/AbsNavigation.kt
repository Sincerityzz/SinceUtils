package com.sincerity.utilslibrary.navigation

import android.app.Activity
import android.content.Context
import android.util.Log
import android.util.SparseArray
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.annotation.IdRes
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.content.ContextCompat
import com.sincerity.utilslibrary.R
import java.lang.ref.WeakReference

/**
 * Created by Sincerity on 2019/11/5.
 * 描述：设计模式
 */
abstract class AbsNavigation<out P : AbsNavigation.Builder.AbsNavigationPrams> constructor(private var mPrams: P) : INavigation {
    private val mViews: SparseArray<WeakReference<View>> = SparseArray()

    init {
        createAndBindView()
    }

    //绑定并且创建View
    private fun createAndBindView() {
        if (mPrams.mParent == null) {
            //获取Activity的根布局 android.R.id.content 根布局
            val mActivityDoctorView: ViewGroup = (mPrams.mContext as Activity).window.decorView as ViewGroup
            mPrams.mParent = mActivityDoctorView.getChildAt(0) as ViewGroup?
            Log.e("Admin", mPrams.mParent.toString())
        }
        //创建View 查看源码
        val view = LayoutInflater.from(mPrams.mContext).inflate(bindViewById(), mPrams.mParent, false)
        mPrams.mParent?.addView(view, 0)
        applyView()
    }

    fun getParams(): P {
        return mPrams
    }

    /**
     * 设置图片资源
     * @param viewId id
     * @param context 上下文
     * @param isVisible 是否显示返回键
     */
    fun setLeftIconIsVisible(@IdRes viewId: Int, context: Context, isVisible: Boolean) {
        if (viewId != 0) {
            val view = mPrams.mParent?.findViewById<Toolbar>(viewId)
            view?.navigationIcon = ContextCompat.getDrawable(context, R.drawable.back)
            (context as AppCompatActivity).supportActionBar?.setDefaultDisplayHomeAsUpEnabled(isVisible)
        }
    }

    /**
     * 设置标题
     * @param viewId id
     * @param text 标题
     */
    fun setText(@IdRes viewId: Int, text: String?) {
        if (viewId != 0) {
            (mPrams.mParent?.findViewById<TextView>(viewId))?.text = text
        }
    }

    fun setBackgroundColor(@IdRes viewId: Int, bgColor: Int) {
        if (viewId != 0) {
            (mPrams.mParent?.findViewById<ViewGroup>(viewId))?.setBackgroundColor(bgColor)
        }
    }

    fun setLeftIconOnClickListener(@IdRes viewId: Int, mLeftOnClickListener: View.OnClickListener) {
        if (viewId != 0) {
            val toolbar = mPrams.mParent?.findViewById<Toolbar>(viewId)
            toolbar?.setNavigationOnClickListener(mLeftOnClickListener)
        }
    }

    fun setOnClickListener(@IdRes viewId: Int, onClickListener: View.OnClickListener) {
        if (viewId != 0) {
            (mPrams.mParent?.findViewById<View>(viewId))?.setOnClickListener(onClickListener)
        }

    }

    abstract class Builder {


        abstract fun builder(): AbsNavigation<AbsNavigationPrams>

        open class AbsNavigationPrams constructor(var mContext: Context, parent: ViewGroup?) {
            var mParent: ViewGroup? = parent
        }
    }
}