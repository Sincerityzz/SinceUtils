package com.sincerity.utilslibrary.httputils

import android.content.Context
import java.io.UnsupportedEncodingException
import java.lang.reflect.ParameterizedType
import java.net.URLEncoder
import java.util.*
import kotlin.collections.HashMap

/*** Created by Sincerity on 2019/11/7.
 * 描述：
 */
class HttpUtils(mContext: Context) {
    private var httpEngine: IHttpEngine? = null //默认请求
    private var url: String? = null
    private var mType = GET_TYPE
    private var mContext: Context? = mContext
    private var mParams = HashMap<String, Any>()
    private var callBack: CallBack? = null
    private var mCache = false

    fun init(httpEngine: IHttpEngine) {
        this.httpEngine = httpEngine
    }

    fun url(url: String): HttpUtils {
        this.url = url
        return this
    }

    //设置请求方式
    fun post(): HttpUtils {
        mType = POST_TYPE
        return this
    }

    //设置请求方式
    fun get(): HttpUtils {
        mType = GET_TYPE
        return this
    }

    /**
     * 读取缓存
     */
    fun cache(isCache: Boolean): HttpUtils {
        mCache = isCache
        return this
    }


    //添加参数
    fun addParams(string: String, any: Any): HttpUtils {
        mParams[string] = any
        return this
    }

    //添加参数
    fun addParams(map: Map<String, Any>?): HttpUtils {
        map?.let { mParams.putAll(it) }
        return this
    }

    fun addParams(): HttpUtils {
        addParams(null)
        return this
    }

    //回调 执行
    fun execute(callBack: CallBack?) {

        if (callBack == null) {
            this.callBack = CallBack.callBack
        }
        mContext?.let { CallBack.callBack.onPreExecute(it, mParams) }
        //判断执行方法
        if (mType == POST_TYPE) {
            url?.let {
                if (callBack != null) {
                    post(it, mParams, callBack)
                }
            }
        }
        if (mType == GET_TYPE) {
            url?.let {
                if (callBack != null) {
                    get(it, mParams, callBack)
                }
            }
        }

    }

    fun execute() {
        execute(null)
    }

    fun exchangeEngine(httpEngine: IHttpEngine): HttpUtils {
        this.httpEngine = httpEngine
        return this
    }

    private fun get(url: String, prams: Map<String, Any>, callBack: CallBack) {
        httpEngine?.get(mContext!!, mCache, url, prams, callBack)
    }

    private fun post(url: String, prams: Map<String, Any>, callBack: CallBack) {
        httpEngine?.post(mContext!!, mCache, url, prams, callBack)
    }

    companion object {
        private const val POST_TYPE = 0x0011
        private const val GET_TYPE = 0x0012

        fun with(mContext: Context): HttpUtils {
            return HttpUtils(mContext)
        }

        fun analysisClazzInfo(any: Any): Class<Any> {
            val type = any.javaClass.genericSuperclass
            val arguments = (type as ParameterizedType).actualTypeArguments
            return arguments[0] as Class<Any>

        }

        @Throws(UnsupportedEncodingException::class)
        fun getUrl(url: String, params: Map<String, Any?>): String {
            val keys: List<String> = ArrayList(params.keys)
            Collections.sort(keys)
            var pester = url
            for (i in keys.indices) {
                val key = keys[i]
                var value = params[key] as String
                value = URLEncoder.encode(value, "UTF-8")
                pester = if (i == keys.size - 1) { //拼接时，不包括最后一个&字符
                    "$pester$key=$value"
                } else {
                    "$pester$key=$value&"
                }
            }
            return pester
        }

    }


}