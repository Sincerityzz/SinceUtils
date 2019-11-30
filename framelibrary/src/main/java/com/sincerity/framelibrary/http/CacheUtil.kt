package com.sincerity.framelibrary.http

import com.sincerity.framelibrary.db.CacheData
import com.sincerity.framelibrary.db.DaoSupportFactory

/**
 * Created by Sincerity on 2019/11/26.
 * 描述：缓存工具类
 */
object CacheUtil {
    fun getCacheResultJson(url: String): String? {
        val daoSupport = DaoSupportFactory.getInstance().getDaoSupport(CacheData::class.java)
        //需要缓存,读取缓存DaoSupport<CacheData>
        val selection = daoSupport.querySupport()
                .selection("mUrlKey=?").selectionArgs(url).query()
        if (selection?.size != 0) {
            val cacheData = selection?.get(0)
            return cacheData?.mJsonValue
        }
        return null
    }

    fun cacheDate(url: String, result: String?): Long {
        val daoSupport = DaoSupportFactory.getInstance().getDaoSupport(CacheData::class.java)
        daoSupport.delete("mUrlKey=?", arrayOf(url))
        return daoSupport.insert(CacheData(url, result))
    }
}