package com.sincerity.framelibrary.skin

import android.view.View

/**
 * Created by Sincerity on 2019/11/27.
 * 描述：
 */
class SkinAttr constructor(resName: String, skinType: SkinType) {


    private val mResName: String? = resName
    private val mType: SkinType? = skinType

    fun skin(mView: View?) {
        mType?.skin(mView, mResName)
    }

}