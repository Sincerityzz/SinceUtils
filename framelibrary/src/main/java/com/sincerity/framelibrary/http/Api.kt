package com.sincerity.framelibrary.http

import retrofit2.Call
import retrofit2.http.POST

/**
 * Created by Sincerity on 2019/11/20.
 * 描述：
 */
interface Api {
    //    https://www.wanandroid.com/banner/json
    @POST("banner/json")
    fun getList(): Call<MutableList<Any>>
}