package com.sincerity.framelibrary.http

import android.content.Context
import android.os.Handler
import android.text.TextUtils
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
    private val mHandler = Handler()
    override fun get(context: Context, cache: Boolean, url: String, prams: Map<String, Any>, callBack: CallBack?) {
        val mUrl = HttpUtils.getUrl(url, prams)
        //判断是否需要缓存
        if (cache) {
            val jsonValue = CacheUtil.getCacheResultJson(mUrl)
            if (!TextUtils.isEmpty(jsonValue)) {
                //需要缓存,并且数据库有缓存
                mHandler.post {
                    callBack?.onSuccess(jsonValue!!)
                }
            }
        }

        val request = Request.Builder().url(mUrl).get().tag(context).build()
        mOkHttpClick.newCall(request).enqueue(
                object : Callback {
                    override fun onFailure(call: Call, e: IOException) {
                        mHandler.post {
                            callBack?.onFail(e)
                        }
                    }

                    override fun onResponse(call: Call, response: Response) {
                        //获取数据之后执行缓存,先比对在缓存
                        val result = response.body?.string()
                        if (cache) {
                            val jsonValue = CacheUtil.getCacheResultJson(mUrl)
                            if (!TextUtils.isEmpty(jsonValue)) {
                                if (result == jsonValue) {
                                    return
                                }
                            }
                        }
                        //缓存
                        if (cache) {
                            CacheUtil.cacheDate(url, result)
                        }
                        mHandler.post {
                            //执行成功方法
                            result.let { callBack?.onSuccess(it!!) }
                        }
                    }

                })
    }


    override fun post(context: Context, cache: Boolean, url: String, prams: Map<String, Any>, callBack: CallBack?) {
//        var joinPrams: String = HttpUtils.joinPrams(url, prams)
        val requestBody = appendBody(prams)
        val request = Request.Builder().url(url).post(requestBody).tag(context).build()
        mOkHttpClick.newCall(request).enqueue(object : Callback {

            override fun onFailure(call: Call, e: IOException) {
                mHandler.post {
                    callBack?.onFail(e)
                }
            }

            override fun onResponse(call: Call, response: Response) {
                mHandler.post {
                    response.body?.string()?.let { callBack?.onSuccess(it) }
                }
            }
        })
    }

    private fun appendBody(params: Map<String, Any>?): RequestBody {
        mOkHttpClick.newBuilder()
                .connectTimeout(30, TimeUnit.SECONDS)
                .readTimeout(30, TimeUnit.SECONDS)
                .writeTimeout(30, TimeUnit.SECONDS)
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