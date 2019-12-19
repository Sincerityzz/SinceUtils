package com.sincerity.sinceutils.bean

import android.text.TextUtils

/**
 * Created by Sincerity on 2019/12/2.
 * 描述：
 */
class SelectImageBean( val path: String,  val name: String,  val time: Long) {
    override fun equals(other: Any?): Boolean {
        if (other is SelectImageBean) {
            return TextUtils.equals(this.path, other.path)
        }
        return false
    }
}