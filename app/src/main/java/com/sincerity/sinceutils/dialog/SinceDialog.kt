package com.sincerity.sinceutils.dialog

import android.annotation.SuppressLint
import android.app.Dialog
import android.content.Context
import android.content.DialogInterface
import android.view.Gravity
import android.view.View
import android.view.View.FOCUSABLE
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import androidx.annotation.StyleRes
import com.sincerity.utilslibrary.R


/**
 * Created by Sincerity on 2019/10/31.
 * 描述：Kotlin构建自己的万能Dialog
 */
class SinceDialog @JvmOverloads constructor(mContext: Context, @StyleRes mThemeId: Int = 0) :
        Dialog(mContext, mThemeId), DialogInterface {
    private var mContext: Context? = null
    private var mThemeId: Int? = 0
    private var mAlert: AlertController? = null

    init {
        this.mContext = mContext
        this.mThemeId = mThemeId
        mAlert = AlertController(this, window!!)
    }

    /**
     * 打开软件盘
     */
    @SuppressLint("NewApi")
    fun openKeyBoard(mView: View) {
        mView.focusable = FOCUSABLE
        val imm = mContext?.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.showSoftInput(mView, InputMethodManager.RESULT_SHOWN)
        imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, InputMethodManager.HIDE_IMPLICIT_ONLY)
    }

    fun closeKeyBoard(mView: View) {
        val imm = context
                .getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(mView.windowToken, 0)
    }

    //设置文本
    fun setText(viewId: Int, text: CharSequence?) {
        mAlert?.setText(viewId, text)
    }

    fun <T : View> getView(viewId: Int): T? {
        return mAlert?.getView(viewId)
    }

    //设置监听
    fun setOnClickListener(viewId: Int, listener: View.OnClickListener?) {
        mAlert?.setOnClickListener(viewId, listener)
    }

    companion object
    class Builder @JvmOverloads constructor(context: Context, @StyleRes theme: Int = 0) {
        private var alertPrams: AlertController.AlertPrams = AlertController.AlertPrams(context, theme)
        private var mTheme: Int? = 0
        private lateinit var dialog: SinceDialog

        /**
         * <int text> 结构的话 SparseArray比HashMap更加高效
         */

        init {
            this.mTheme = theme
        }

        fun setContentView(layoutResId: Int): Builder {
            alertPrams.mView = null
            alertPrams.mViewLayoutResId = layoutResId
            return this
        }

        fun setContentView(mView: View): Builder {
            alertPrams.mView = mView
            alertPrams.mViewLayoutResId = 0
            return this
        }

        fun setOnCancelListener(onCancelListener: DialogInterface.OnCancelListener): Builder {
            alertPrams.mOnCancelListener = onCancelListener
            return this
        }

        fun setOnKeyListener(onKeyListener: DialogInterface.OnKeyListener): Builder {
            alertPrams.mOnKeyListener = onKeyListener
            return this
        }

        fun setOnDismissListener(onDismissListener: DialogInterface.OnDismissListener): Builder {
            alertPrams.mOnDismissListener = onDismissListener
            return this
        }

        /**
         * 设置文本
         */
        fun setText(viewId: Int, text: CharSequence): Builder {
            alertPrams.textMap.put(viewId, text)
            return this
        }

        /**
         * 设置点击事件
         */
        fun setOnClickListener(viewId: Int, listener: View.OnClickListener): Builder {
            alertPrams.clickMap.put(viewId, listener)
            return this
        }

        private fun create(): SinceDialog {
            // We can't use Dialog's 3-arg constructor with the createThemeContextWrapper param,
            // so we always have to re-set the theme
            val dialog = SinceDialog(alertPrams.mContext!!, alertPrams.mThemeId!!)
            alertPrams.apply(dialog.mAlert)
            dialog.setCancelable(alertPrams.mCancelable)
            dialog.setOnCancelListener(alertPrams.mOnCancelListener)
            dialog.setOnDismissListener(alertPrams.mOnDismissListener)
            if (alertPrams.mOnKeyListener != null) {
                dialog.setOnKeyListener(alertPrams.mOnKeyListener)
            }
            return dialog
        }

        /**
         * 显示对话框
         */
        fun show(): SinceDialog {
            dialog = create()
            dialog.show()
            return dialog
        }

        //全屏
        fun fullWindow(): Builder {
            alertPrams.mWidth = ViewGroup.LayoutParams.MATCH_PARENT
            return this
        }

        //从底部弹出是否带动画
        fun fromBottom(isAnimal: Boolean): Builder {
            if (isAnimal) {
                alertPrams.mAnimals = R.style.dialog_from_bottom_anim
            }
            alertPrams.mGravity = Gravity.BOTTOM
            return this
        }

        fun setWidthAndHeight(width: Int, height: Int): Builder {
            alertPrams.mWidth = width
            alertPrams.mHeight = height
            return this
        }

        fun addDefaultAnimal(): Builder {
            alertPrams.mAnimals = R.style.dialog_scale_anim
            return this
        }

        //设置动画
        fun addAnimals(animal: Int): Builder {
            alertPrams.mAnimals = animal
            return this
        }
        // Builder的其他代码省略 ......

        // 2 : 设置各种参数
    }
}