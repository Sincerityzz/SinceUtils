package com.sincerity.framelibrary.db

import android.content.ContentValues
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteException
import androidx.collection.ArrayMap
import java.lang.reflect.Method

/**
 * Created by Sincerity on 2019/11/22.
 * 描述：
 */
class DaoSupport<T> : IDaoSupport<T> {
    private var sqlDataBase: SQLiteDatabase? = null
    private var clazz: Class<T>? = null
    private val mPutMethodArgs = arrayOfNulls<Any>(2)
    private val mPutMethods: ArrayMap<String, Method> = ArrayMap()
    private var mQuerySupport: QuerySupport<T>? = null
    override fun init(sqlDataBase: SQLiteDatabase, clazz: Class<T>) {
        this.sqlDataBase = sqlDataBase
        this.clazz = clazz
        val sb = StringBuffer()
        sb.append("create table if not exists ").append(clazz.simpleName).append("( id integer primary key autoincrement, ")
        val fields = clazz.declaredFields
        for (field in fields) {
            field.isAccessible = true
            val name = field.name//属性
            val type = field.type.simpleName//属性的类型
            sb.append("$name ").append(DaoUtils.getColumnType(type)).append(", ")
        }
        sb.replace(sb.length - 2, sb.length, ")")
        sqlDataBase.execSQL(sb.toString())
    }

    override fun insert(t: T): Long {
        val values: ContentValues = contentValuesByObj(t)
        return sqlDataBase?.insert(clazz?.simpleName, null, values)!!
    }

    //优化之前
//    private fun contentValuesByObj(any: T): ContentValues {
//        val values = ContentValues()
//        val fields = clazz?.declaredFields
//        for (field in fields!!) {
//            try {
//                field.isAccessible = true
//                val key = field.name
//                val value = field.get(any)
//                val method = ContentValues::class.java.getDeclaredMethod("put", String::class.java, value.javaClass)
//                method.invoke(values, key, value)
//            } catch (e: Exception) {
//                e.printStackTrace()
//            }
//        }
//        return values
//    }
    //优化之后
    private fun contentValuesByObj(any: T): ContentValues {
        val values = ContentValues()
        val fields = clazz?.declaredFields
        for (field in fields!!) {
            try {
                field.isAccessible = true
                val key = field.name
                val value = field.get(any)
                mPutMethodArgs[0] = key
                mPutMethodArgs[1] = value
                val fieldTypeName = value.javaClass.name
                //缓存方法
                var putMethod = mPutMethods[fieldTypeName]
                if (putMethod == null) {
                    putMethod = ContentValues::class.java.getDeclaredMethod("put", String::class.java, value.javaClass)
                    mPutMethods[fieldTypeName] = putMethod
                }
                putMethod.invoke(values, mPutMethodArgs[0], mPutMethodArgs[1])
            } catch (e: Exception) {
                e.printStackTrace()
            } finally {
                mPutMethodArgs[0] = null
                mPutMethodArgs[1] = null
            }
        }
        return values
    }

    override fun insert(list: MutableList<T>): Boolean {
        try {
            sqlDataBase?.beginTransaction()
            for (t in list) {
                //单条插入
                insert(t)
            }
            sqlDataBase?.setTransactionSuccessful()
            sqlDataBase?.endTransaction()
        } catch (e: SQLiteException) {
            e.printStackTrace()
            return false
        }

        return true
    }

    /**
     * 删除数据
     *
     * @param string :删除的SQL语句
     * @param whereArgs 用于替换SQL语句中的变量
     * @return 受影响的行数
     */
    override fun delete(string: String, whereArgs: Array<String>): Int {
        return sqlDataBase!!.delete(clazz?.simpleName, string, whereArgs)
    }

    /**
     * 更新数据
     * @param  obj 待修改的对象
     * @param whereValue 修改的SQl语句
     * @param whereArgs 替换SQL中的变量
     * @return 受影响的行数
     */
    override fun update(obj: T, whereValue: String, whereArgs: Array<String>): Int {
        val value = contentValuesByObj(obj)
        return sqlDataBase!!.update(clazz?.simpleName, value, whereValue, whereArgs)
    }

    /**
     * 获取查询支持类
     *
     */
    override fun querySupport(): QuerySupport<T> {
        if (mQuerySupport == null) {
            mQuerySupport = QuerySupport(sqlDataBase!!, clazz!!)
        }
        return mQuerySupport!!
    }

}
