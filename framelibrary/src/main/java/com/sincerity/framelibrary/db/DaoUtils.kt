package com.sincerity.framelibrary.db

import android.text.TextUtils
import java.util.*

/**
 * Created by Sincerity on 2019/11/22.
 * 描述：
 */
object DaoUtils {

    fun getColumnType(type: String): String {
        return when {
            type.contains("String") -> {
                "text"
            }
            type.contains("int") -> {
                "integer"
            }
            type.contains("char") -> {
                "varchar"
            }
            else -> {
                type
            }
        }
    }

    fun capitalize(string: String?): String? {
        if (!TextUtils.isEmpty(string)) {
            return string!!.substring(0, 1).toUpperCase(Locale.US) + string.substring(1)
        }
        return if (string == null) null else ""
    }
}