package com.sincerity.utilslibrary.dialog

import android.content.Context
import android.content.DialogInterface
import android.util.SparseArray
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import android.view.Window

/**
 * Created by Sincerity on 2019/10/31.
 * 描述：
 */
class AlertController constructor(dialog: SinceDialog, mWindow: Window) {
    private var dialog: SinceDialog? = null
    private var mWindow: Window? = null
    var viewHelper: DialogViewHelper? = null

    init {
        this.dialog = dialog
        this.mWindow = mWindow
    }

    //设置文本
    fun setText(viewId: Int, text: CharSequence?) {
        viewHelper?.setText(viewId, text)
    }

    fun <T : View> getView(viewId: Int): T? {
        return viewHelper?.getView(viewId)
    }

    //设置监听
    fun setOnClickListener(viewId: Int, listener: View.OnClickListener?) {
        viewHelper?.setOnClickListener(viewId, listener)
    }

    companion object
    class AlertPrams constructor(context: Context, theme: Int) {
        //高度
        var mHeight = ViewGroup.LayoutParams.WRAP_CONTENT
        //位置
        var mGravity = Gravity.CENTER
        //默认动画
        var mAnimals = 0
        //宽度
        var mWidth = ViewGroup.LayoutParams.WRAP_CONTENT
        //布局ID
        var mViewLayoutResId = 0
        //布局文件
        var mView: View? = null
        //返回键的监听
        var mOnKeyListener: DialogInterface.OnKeyListener? = null
        //确认的监听
        var mOnDismissListener: DialogInterface.OnDismissListener? = null
        //取消的监听
        var mOnCancelListener: DialogInterface.OnCancelListener? = null
        //默认点击空白可以取消
        var mCancelable: Boolean = true
        var mContext: Context? = null
        var mThemeId: Int? = 0
        //存放文本信息
        var textMap: SparseArray<CharSequence> = SparseArray()
        //存放点击事件
        var clickMap: SparseArray<View.OnClickListener> = SparseArray()

        init {
            this.mContext = context
            this.mThemeId = theme
        }

        fun apply(mAlert: AlertController?) {
            var viewHelper: DialogViewHelper? = null
            //设置布局
            if (mViewLayoutResId != 0) {
                viewHelper = DialogViewHelper(mContext.let { it }, mViewLayoutResId)
            }
            if (mView != null) {
                viewHelper = DialogViewHelper()
                viewHelper.setContentView(mView!!)
            }
            viewHelper?.mContentView?.let { mAlert?.dialog?.setContentView(it) }
            requireNotNull(viewHelper) { "请设置布局setContentView()" }
            mAlert?.viewHelper = viewHelper
            //设置文本
            for (i in 0 until textMap.size()) {
                mAlert?.setText(textMap.keyAt(i), textMap.valueAt(i))
            }
            for (i in 0 until clickMap.size()) {
                mAlert?.setOnClickListener(clickMap.keyAt(i), clickMap.valueAt(i))
            }
            val mWindow = mAlert?.mWindow
            //设置Controller

            mWindow?.setGravity(mGravity)
            if (mAnimals != 0) {
                mWindow?.setWindowAnimations(mAnimals)
            }
            val params = mWindow?.attributes
            params?.width = mWidth
            params?.height = mHeight
            mWindow?.attributes = params
            //设置监听

            //配置自定义的效果 动画

        }
    }
}