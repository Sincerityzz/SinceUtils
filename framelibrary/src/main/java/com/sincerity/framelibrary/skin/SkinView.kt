package com.sincerity.framelibrary.skin

import android.view.View

/**
 * Created by Sincerity on 2019/11/27.
 * 描述：
 */
class SkinView constructor(view: View?, skinAttrs: MutableList<SkinAttr>) {

    private val mView = view
    private val mAttrs = skinAttrs
    fun skin() {
        for (attr: SkinAttr in mAttrs) {
            attr.skin(mView)
        }
    }
}