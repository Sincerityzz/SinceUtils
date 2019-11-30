package com.sincerity.framelibrary.defalut

import android.app.Activity
import android.content.Context
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import com.sincerity.framelibrary.R
import com.sincerity.utilslibrary.navigation.AbsNavigation

/**
 * Created by Sincerity on 2019/11/5.
 * 描述：
 */
class DefaultNavigationBar<P : AbsNavigation.Builder.AbsNavigationPrams>(params: Builder.DefaultNavigationPrams) :
        AbsNavigation<DefaultNavigationBar.Builder.DefaultNavigationPrams>(params) {
    override fun bindViewById(): Int {
        return R.layout.title_bar

    }

    override fun applyView() {
        //绑定效果
        setLeftIconIsVisible(R.id.toolBar, getParams().mContext, true)
//        setImageResource(R.id.title_right, getParams().mRightRes)
        setText(R.id.title_content, getParams().mTitle)
        setText(R.id.title_right, getParams().mRightTitle)
        setBackgroundColor(R.id.toolBar, getParams().bgColor)
        setLeftIconOnClickListener(R.id.toolBar, getParams().mLeftOnClickListener)
        setOnClickListener(R.id.title_right, getParams().mRightOnClickListener)

    }


    class Builder @JvmOverloads constructor(mContext: Context, parent: ViewGroup? = null) : AbsNavigation.Builder() {

        var prams: DefaultNavigationPrams

        init {
            prams = DefaultNavigationPrams(mContext, parent)
        }

        override fun builder(): DefaultNavigationBar<AbsNavigationPrams> {

            return DefaultNavigationBar(prams)
        }

        fun setBackVisible(visible: Boolean): Builder {
            prams.mVisibleBack = visible
            return this
        }

        //设置效果
        fun setTitle(title: String): Builder {
            prams.mTitle = title
            return this
        }

        /**
         * 设置右边的副标题
         * @param title 标题
         */
        fun setRightText(title: String): Builder {
            prams.mRightTitle = title
            return this
        }

        fun setRightIcon(res: Int): Builder {
            prams.mRightRes = res
            return this
        }

        /**
         * 设置返回
         */
        fun setBackOnClickListener(mClickListener: View.OnClickListener): AbsNavigation.Builder {
            prams.mLeftOnClickListener = mClickListener
            return this
        }

        class DefaultNavigationPrams constructor(mContext: Context, parent: ViewGroup?)
            : AbsNavigation.Builder.AbsNavigationPrams(mContext, parent) {
            var mTitle = "测试标题"
            var mRightTitle: String? = null
            var mVisibleBack = true
            var mRightRes = 0
            var bgColor = ContextCompat.getColor(mContext, R.color.title_bar_color)
            var mLeftOnClickListener = View.OnClickListener {
                (mContext as Activity).finish()
            }
            var mRightOnClickListener = View.OnClickListener {
                (mContext as Activity).finish()
            }
        }
    }
}