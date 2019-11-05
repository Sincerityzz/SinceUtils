package com.sincerity.utilslibrary.dialog

import android.content.Context
import android.util.SparseArray
import android.view.LayoutInflater
import android.view.View
import android.widget.TextView
import org.jetbrains.annotations.NotNull
import java.lang.ref.WeakReference

/**
 * Created by Sincerity on 2019/10/31.
 * 描述： dialogViw的辅助处理类
 */
class DialogViewHelper @JvmOverloads constructor(mContext: Context? = null, mViewId: Int = 0) {


    var mContentView: View? = null
    //软引用
    private val mViews: SparseArray<WeakReference<View>> = SparseArray()

    init {
        mContentView = LayoutInflater.from(mContext).inflate(mViewId, null)
    }

    //设置布局
    fun setContentView(mView: View) {
        this.mContentView = mView
    }

    //设置文本
    fun setText(viewId: Int, @NotNull text: CharSequence?) {
        val mText: TextView = getView(viewId)
//        val mText: TextView? = mContentView?.findViewById(viewId) as TextView
        mText.text = text
    }

    fun <T : View> getView(viewId: Int): T {
        val weakReference = mViews.get(viewId)
        var view: View? = null
        if (weakReference != null) {
            view = weakReference.get()
        }
        if (view == null) {
            view = mContentView?.findViewById(viewId)
            if (view != null) {
                mViews.put(viewId, WeakReference(view))
            }
        }
        return view as T
    }

    //设置监听
    fun setOnClickListener(viewId: Int, listener: View.OnClickListener?) {
        val view: View? = getView(viewId)
        view?.setOnClickListener(listener)
    }
}