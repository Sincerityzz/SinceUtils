package com.sincerity.framelibrary.skin

import android.content.res.ColorStateList
import android.graphics.drawable.Drawable
import android.util.Log
import android.view.View
import android.widget.ImageView
import android.widget.TextView

/**
 * Created by Sincerity on 2019/11/27.
 * 描述：
 */
enum class SkinType(resName: String) {

    TEXT_COLOR("textColor") {
        override fun skin(mView: View?, mResName: String?) {
            val resource = getSkinResource()
            if (resource != null) {
                var colorStateList: ColorStateList? = null
                if (mResName != null) {
                    colorStateList = resource.getColorByName(mResName)
                }
                if (colorStateList == null) return
                val view = mView as TextView
                view.setTextColor(colorStateList)

            }
        }
    },
    BACKGROUD("background") {
        override fun skin(mView: View?, mResName: String?) {
            //背景可能是图片或者颜色
            val resource = getSkinResource()
            if (resource != null) {
                var drawable: Drawable? = null
                var colorStateList: ColorStateList? = null
                if (mResName != null) {
                    Log.e("admin", "资源名称 ---->  $mResName")
                    try {
                        //图片
                        drawable = resource.getDrawableByName(mResName)
                        //颜色
                        colorStateList = resource.getColorByName(mResName)
                    } catch (e: NullPointerException) {
                        e.printStackTrace()
                    }
                    if (drawable != null) {
                        Log.e("admin", "图片ID值 ---->  $drawable")
                        val view = mView as ImageView
                        view.setBackgroundDrawable(drawable)
                        return
                    }
                    if (colorStateList != null) {
                        Log.e("admin", "颜色值为 ---->  ${colorStateList.defaultColor}")
                        mView?.setBackgroundColor(colorStateList.defaultColor)
                    }
                }
            }
        }
    },
    SRC("src") {
        override fun skin(mView: View?, mResName: String?) {
            val resource = getSkinResource()
            if (resource != null) {
                val drawable: Drawable?
                if (mResName != null) {
                    //图片
                    drawable = resource.getDrawableByName(mResName)
                    if (drawable != null) {
                        val view = mView as ImageView
                        view.setImageDrawable(drawable)
                        return
                    }
                }
            }
        }
    };

    private var mResName = resName

//    init {
//        this.mResName = resName
//    }

    abstract fun skin(mView: View?, mResName: String?)
    fun getResourceName(): String {
        return mResName
    }

    fun getSkinResource(): SkinResource? {
        val skinResource = SkinManager.getInstance().getSkinResource()
        if (skinResource != null) {
            return skinResource
        }
        return null
    }
}