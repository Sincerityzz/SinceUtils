package com.sincerity.framelibrary.db

/**
 * Created by Sincerity on 2019/11/26.
 * 描述：
 */
class CacheData() {

    //缓存KEY
    var mUrlKey: String? = null
    //缓存Value
    var mJsonValue: String? = null

    constructor(mUrlKey: String?, mJsonValue: String?) : this() {
        this.mUrlKey = mUrlKey
        this.mJsonValue = mJsonValue
    }


}