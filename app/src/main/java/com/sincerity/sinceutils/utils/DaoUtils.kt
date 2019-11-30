package com.sincerity.sinceutils.utils

/**
 * Created by Sincerity on 2019/11/22.
 * 描述：
 */
class DaoUtils {
    companion object {
        fun getColumType(type: String): String {
            return when {
                type.contains("String") -> {
                    "text"
                }
                type.contains("int") -> {
                    "integer"
                }
                type.contains("boolean") -> {
                    "boolean"

                }
                else -> {
                    type
                }
            }
        }
    }

}