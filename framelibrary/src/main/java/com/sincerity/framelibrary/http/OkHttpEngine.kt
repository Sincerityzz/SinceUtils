package com.sincerity.framelibrary.http

import android.content.Context
import android.text.TextUtils
import android.util.Log
import com.sincerity.utilslibrary.httputils.CallBack
import com.sincerity.utilslibrary.httputils.HttpUtils
import com.sincerity.utilslibrary.httputils.IHttpEngine
import okhttp3.*
import java.io.IOException
import java.util.concurrent.TimeUnit


/**
 * Created by Sincerity on 2019/11/7.
 * 描述： 默认OkHttp请求
 */
class OkHttpEngine : IHttpEngine {
    private var mOkHttpClick = OkHttpClient()
    override fun get(context: Context, cache: Boolean, url: String, prams: Map<String, Any>, callBack: CallBack?) {
        val mUrl = HttpUtils.getUrl(url, prams)
        //判断是否需要缓存
        if (cache) {
            val jsonValue = CacheUtil.getCacheResultJson(mUrl)
            if (!TextUtils.isEmpty(jsonValue)) {
                //需要缓存,并且数据库有缓存
                Log.e("admin", "已经读到缓存$mUrl")
                callBack?.onSuccess(jsonValue!!)
            }
        }

        val request = Request.Builder().url(mUrl).get().tag(context).build()
        mOkHttpClick.newCall(request).enqueue(
                object : Callback {
                    override fun onFailure(call: Call, e: IOException) {
                        callBack?.onFail(e)
                    }

                    override fun onResponse(call: Call, response: Response) {
                        //获取数据之后执行缓存,先比对在缓存
                        val result = response.body?.string()
                        Log.e("admin", result!!)
                        if (cache) {
                            val jsonValue = CacheUtil.getCacheResultJson(mUrl)
                            if (!TextUtils.isEmpty(jsonValue)) {
                                if (result == jsonValue) {
                                    Log.e("admin", "数据和缓存一致")
                                    return
                                }
                            }
                        }
                        //缓存
                        if (cache) {
                            val cacheDateNum = CacheUtil.cacheDate(url, result)
                            Log.e("admin", "缓存成功$url ----->$result--->$cacheDateNum")
                        }
                        //执行成功方法
                        result.let { callBack?.onSuccess(it) }
                    }

                })
    }


    override fun post(context: Context, cache: Boolean, url: String, prams: Map<String, Any>, callBack: CallBack?) {
//        var joinPrams: String = HttpUtils.joinPrams(url, prams)
        val requestBody = appendBody(prams)
        val request = Request.Builder().url(url).post(requestBody).tag(context).build()
        mOkHttpClick.newCall(request).enqueue(object : Callback {

            override fun onFailure(call: Call, e: IOException) {
                callBack?.onFail(e)
            }

            override fun onResponse(call: Call, response: Response) {
                response.body?.string()?.let { callBack?.onSuccess(it) }
            }


        })
    }

    private fun appendBody(params: Map<String, Any>?): RequestBody {
        mOkHttpClick.newBuilder()
                .connectTimeout(10, TimeUnit.SECONDS)
                .readTimeout(10, TimeUnit.SECONDS)
                .writeTimeout(10, TimeUnit.SECONDS)
                .build()
        val builder = FormBody.Builder()
        if (params != null) {
            for ((key, value) in params.entries) {
                builder.add(key, value.toString())
            }
        }
        return builder.build()
    }
}