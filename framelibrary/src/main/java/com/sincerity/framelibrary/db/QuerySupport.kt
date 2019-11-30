package com.sincerity.framelibrary.db

import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import java.lang.reflect.Method
import java.util.*
import kotlin.collections.ArrayList

/**
 * Created by Sincerity on 2019/11/25.
 * 描述：
 */
class QuerySupport<T>(private val mSqlDateBase: SQLiteDatabase, private var mClazz: Class<T>) {

    //查找列
    private var mQueryColumns: Array<out String>? = null
    //查询条件
    private var mQuerySelections: String? = null
    //查询参数
    private var mQuerySelectionArgs: Array<out String>? = null
    //查询分组
    private var mQueryGroupBy: String? = null
    //查询对结果过滤
    private var mQueryHaving: String? = null
    //查询排序
    private var mQueryOrderBy: String? = null
    //查询用于分页
    private var mQueryLint: String? = null

    /**
     * 查询列
     * @param columns 查询某一列的SQL
     */
    fun columns(vararg columns: String): QuerySupport<T> {
        this.mQueryColumns = columns
        return this
    }

    /**
     * 分组查询
     */
    fun groupBy(mQueryGroupBy: String): QuerySupport<T> {
        this.mQueryGroupBy = mQueryGroupBy
        return this
    }

    /**
     * 分页查询
     */
    fun lint(mQueryLint: String): QuerySupport<T> {
        this.mQueryLint = mQueryLint
        return this
    }

    /**
     * 查询某一列的条件
     * @param selectionArgs 查询条件
     */
    fun selectionArgs(vararg selectionArgs: String): QuerySupport<T> {
        this.mQuerySelectionArgs = selectionArgs
        return this
    }

    fun having(having: String): QuerySupport<T> {
        this.mQueryHaving = having
        return this
    }

    fun orderBy(orderBy: String): QuerySupport<T> {
        this.mQueryOrderBy = orderBy
        return this
    }

    fun selection(selections: String): QuerySupport<T> {
        this.mQuerySelections = selections
        return this
    }

    /**
     * 条件查询
     */
    fun query(): List<T>? {
        /**
         * @param 1 :表名
         * @param 2 按列去查询
         * @param 3 查询条件 SQL语句
         * @param 4 是用于替换第三个参数sql语句中的占位符（？）数组，如果第三，四个参数不指定则默认查询所有行
         * @param 5 分组操作 用于指定需要去group by的列，不指定则表示不对查询结果进行group by操作。
         * @param 6 用于对group by之后的数据进行进一步的过滤，不指定则表示不进行过滤。
         * @param 7 用于指定查询结果的排序方式，不指定则表示使用默认的排序方式
         * @param 8 分页数据
         */
        val cursor = mSqlDateBase.query(mClazz.simpleName, mQueryColumns, mQuerySelections, mQuerySelectionArgs, mQueryGroupBy, mQueryHaving, mQueryOrderBy, mQueryLint)
        clearQueryParams()
        return cursorToList(cursor)
    }

    /**
     * 查询所有
     */
    fun queryAll(): List<T>? {
        val cursor = mSqlDateBase.query(mClazz.simpleName, null, null, null, null, null, null, null)
        return cursorToList(cursor)
    }

    private fun clearQueryParams() {
        mQueryColumns = null
        mQuerySelections = null
        mQuerySelectionArgs = null
        mQueryGroupBy = null
        mQueryHaving = null
        mQueryOrderBy = null


    }

    private fun cursorToList(cursor: Cursor?): List<T> {
        val list = ArrayList<T>()
        if (cursor != null && cursor.moveToFirst()) {
            do {
                val instance = mClazz.newInstance()
                val fields = mClazz.declaredFields
                for (field in fields) {
                    field.isAccessible = true
                    val name = field.name
                    //下标拿到某一列
                    val index = cursor.getColumnIndex(name)
                    if (index == -1) {
                        continue
                    }
                    //通过分社获取类型
                    val cursorMethod = cursorMethod(field.type)
                    if (cursorMethod != null) {
                        var invoke = cursorMethod.invoke(cursor, index) ?: continue
                        if (field.type == Boolean::class.java) {
                            if ("0" == invoke) {
                                invoke = false
                            } else if ("1" == invoke) {
                                invoke = true
                            }
                        } else if (field.type == Char::class.java || field.type == CharSequence::class.java) {
                            invoke = (invoke as String).codePointAt(0)
                        } else if (field.type == Date::class.java) {
                            val date = invoke as Long
                            if (date <= 0) {
                                invoke == null
                            } else {
                                invoke = Date(date)
                            }
                        }
                        //反射注入
                        field.set(instance, invoke)
                    }
                }
                list.add(instance)
            } while (cursor.moveToNext())
        }
        cursor?.close()
        return list

    }

    private fun cursorMethod(type: Class<*>): Method? {
        val methodName = getColumnMethodName(type)
        return Cursor::class.java.getMethod(methodName, Int::class.java)
    }

    private fun getColumnMethodName(type: Class<*>): String {
        val typeName: String = if (type.isPrimitive) {
            DaoUtils.capitalize(type.name)!!
        } else {
            type.simpleName
        }
        var methodName = "get$typeName"
        if ("getBoolean" == methodName) {
            methodName = "getInt"
        } else if ("getChar" == methodName || "getCharacter" == methodName) {
            methodName = "getString"
        } else if ("getDate" == methodName) {
            methodName = "getLong"
        } else if ("getInteger" == methodName) {
            methodName = "getInt"
        }
        return methodName
    }
}