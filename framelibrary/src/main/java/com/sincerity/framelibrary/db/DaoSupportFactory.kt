package com.sincerity.framelibrary.db

import android.database.sqlite.SQLiteDatabase
import android.os.Environment
import java.io.File

/**
 * Created by Sincerity on 2019/11/22.
 * 描述：
 */
object DaoSupportFactory {
    private var sqLiteDatabase: SQLiteDatabase? = null
    //静态内部类
    private var daoSupportFactory: DaoSupportFactory? = null

    //构造方法
    init {
        val file = File(Environment.getExternalStorageDirectory().absolutePath +
                File.separator + "since" + File.separator + "database")
        if (!file.exists()) {
            file.mkdirs()
        }
        val fileRoot = File(file, "cache.db")
        sqLiteDatabase = SQLiteDatabase.openOrCreateDatabase(fileRoot, null)
    }


    fun getInstance(): DaoSupportFactory {
        if (daoSupportFactory == null) {
            synchronized(DaoSupportFactory::class.java) {
                if (daoSupportFactory == null) {
                    daoSupportFactory = DaoSupportFactory
                }
            }
        }
        return daoSupportFactory!!

    }


    fun <T> getDaoSupport(clazz: Class<T>): IDaoSupport<T> {
        val dao = DaoSupport<T>()
        sqLiteDatabase?.let { dao.init(it, clazz) }
        return dao
    }
}