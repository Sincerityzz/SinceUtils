package com.sincerity.framelibrary.db

import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteException

/**
 * Created by Sincerity on 2019/11/22.
 * 描述：
 */
interface IDaoSupport<T> {
    fun init(sqlDataBase: SQLiteDatabase, clazz: Class<T>)
    //插入数据
    @Throws(SQLiteException::class)
    fun insert(t: T): Long

    //批量插入数据
    @Throws(SQLiteException::class)
    fun insert(list: MutableList<T>): Boolean

    /**
     * 获取查询的支持
     */
    fun querySupport(): QuerySupport<T>

    //删除数据
    @Throws(SQLiteException::class)
    fun delete(string: String, whereArgs: Array<String>): Int

    //修改数据
    @Throws(SQLiteException::class)
    fun update(obj: T, whereValue: String, whereArgs: Array<String>): Int
}