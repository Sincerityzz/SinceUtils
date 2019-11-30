package com.sincerity.framelibrary.http

import android.content.Context
import com.sincerity.utilslibrary.httputils.CallBack
import com.sincerity.utilslibrary.httputils.IHttpEngine
import okhttp3.OkHttpClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

/**
 * Created by Sincerity on 2019/11/20.
 * 描述：
 */
class RetrofitEngine : IHttpEngine {
    private val retrofit = Retrofit.Builder()
    private var mOkHttpClick = OkHttpClient()

    init {
        mOkHttpClick.newBuilder()
                .connectTimeout(10, TimeUnit.SECONDS)
                .readTimeout(10, TimeUnit.SECONDS)
                .writeTimeout(10, TimeUnit.SECONDS)
                .build()
    }

    override fun get(context: Context, cache: Boolean, url: String, prams: Map<String, Any>, callBack: CallBack?) {
        val retrofit = retrofit
                .baseUrl(url)
                .addConverterFactory(GsonConverterFactory.create())
                .client(mOkHttpClick)
                .build()
        val api = retrofit.create(Api::class.java)
        api.getList().enqueue(object : Callback<MutableList<Any>> {
            override fun onFailure(call: Call<MutableList<Any>>, t: Throwable) {
                callBack?.onFail(t as Exception)
            }

            override fun onResponse(call: Call<MutableList<Any>>, response: Response<MutableList<Any>>) {
                callBack?.onSuccess(response.message())
            }

        })
    }

    override fun post(context: Context, cache: Boolean, url: String, prams: Map<String, Any>, callBack: CallBack?) {
    }
}