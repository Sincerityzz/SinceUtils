package com.sincerity.framelibrary.base

import android.content.Context
import com.google.gson.Gson
import com.sincerity.utilslibrary.httputils.CallBack
import com.sincerity.utilslibrary.httputils.HttpUtils

/**
 * Created by Sincerity on 2019/11/20.
 * 描述： 遗留问题 1. 处理后台返回的异常数据信息 用OkHttp的拦截器去做
 */
abstract class HttpCallBack<T> : CallBack {
    override fun onPreExecute(context: Context, params: MutableMap<String, Any>?) {
        params?.put("", "")
        onPreExecute()
    }

    //开始执行
    fun onPreExecute() {
    }

    override fun onSuccess(result: String) {
        val gson = Gson()
        val objResult: T = gson.fromJson(result, HttpUtils.Companion.analysisClazzInfo(this)) as T
        onSuccess(objResult)
    }

    abstract fun onSuccess(result: T)

}
