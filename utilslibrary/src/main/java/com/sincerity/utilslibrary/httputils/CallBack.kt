package com.sincerity.utilslibrary.httputils

import android.content.Context

/**
 * Created by Sincerity on 2019/11/7.
 * 描述：
 */
interface CallBack {
    fun onPreExecute(context: Context, params: MutableMap<String, Any>?)
    //会出现问题
    fun onSuccess(result: String)

    fun onFail(exception: Exception)

    companion object {
        val callBack: CallBack = object : CallBack {
            override fun onPreExecute(context: Context, params: MutableMap<String, Any>?) {

            }

            override fun onSuccess(result: String) {
            }

            override fun onFail(exception: Exception) {
            }

        }
    }

}