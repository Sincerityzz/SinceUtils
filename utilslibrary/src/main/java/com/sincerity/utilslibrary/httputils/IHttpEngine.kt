package com.sincerity.utilslibrary.httputils

import android.content.Context
import org.jetbrains.annotations.NotNull

/**
 * Created by Sincerity on 2019/11/7.
 * 描述：网络引擎的规范
 */
 interface IHttpEngine {

    //get
    fun get(context: Context, cache:Boolean,@NotNull url: String, prams: Map<String, Any>, callBack: CallBack?)

    //post
    fun post(context: Context,cache:Boolean, @NotNull url: String, prams: Map<String, Any>, callBack: CallBack?)
    //下载文件
    //上传文件
}