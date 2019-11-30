package com.sincerity.sinceutils.ui

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.sincerity.framelibrary.base.HttpCallBack
import com.sincerity.framelibrary.db.DaoSupportFactory
import com.sincerity.framelibrary.defalut.DefaultNavigationBar
import com.sincerity.framelibrary.http.OkHttpEngine
import com.sincerity.sinceutils.R
import com.sincerity.sinceutils.bean.ImageBean
import com.sincerity.sinceutils.bean.Person
import com.sincerity.utilslibrary.httputils.HttpUtils
import kotlinx.android.synthetic.main.activity_test.*

class SQLDateBaseActivity : AppCompatActivity() {
    private val TAG: String = javaClass.simpleName
    private var daoSupport = DaoSupportFactory.getInstance().getDaoSupport(Person::class.java)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        DefaultNavigationBar.Builder(this).setTitle("数据库相关").builder()
        supportActionBar
        setContentView(R.layout.activity_test)
    }

    fun onInsert(view: View) {
        val insert = daoSupport.insert(Person("Lina", 18))
        Log.e(TAG, "添加成功 添加的下标Id: $insert ")
        tv_msg.text = String.format("添加成功 添加的下标Id: $insert ")
    }

    fun onInsertAll(view: View) {
        val list: MutableList<Person> = ArrayList()
        val startTime = System.currentTimeMillis()
        for (i in 0..7999) {
            list.add(Person("张珊$i", 22 + i))
        }
        val isTrue = daoSupport.insert(list)
        val endTime = System.currentTimeMillis()
        Log.e(TAG, "批量添加结果 $isTrue ")
        tv_msg.text = String.format("批量添加结果 $isTrue  \n + 耗时: ${endTime - startTime} 毫秒")
    }

    fun onQueryAll(view: View?) {
        val startTime = System.currentTimeMillis()
        val list = daoSupport.querySupport().queryAll()
        val endTime = System.currentTimeMillis()

        Log.e(TAG, "查询所有的数据 ${list?.size} 条")
        tv_msg.text = String.format("查询所有的数据 ${list?.size} 条 \n + 耗时: ${endTime - startTime} 毫秒")
    }

    fun onQueryBySql(view: View) {
        val personList = daoSupport.querySupport().selection("name = ?").selectionArgs("张珊0").query()
        val sb = StringBuilder()
        if (personList != null) {
            sb.append("查询到数据共${personList.size}条")
            for (person in personList) {
                Log.e(TAG, "条件查询$person")
                sb.append("条件查询:").append("$person \n")
            }
        }
        tv_msg.text = sb.toString()
    }

    fun onQueryById(view: View) {
        val person = daoSupport.querySupport().selection("id=?").selectionArgs("22").query()
        Log.e(TAG, "ID查询$person")
        tv_msg.text = String.format("ID查询$person")
    }

    fun onUpdate(view: View) {
        val update = daoSupport.update(Person("张珊99", 121), "age=?", arrayOf("222"))
        Log.e(TAG, "更新 $update 条数据更新成功")
        tv_msg.text = String.format("更新 $update 条数据更新成功")
    }

    fun onDelete(view: View) {
        val delete = daoSupport.delete("id=?", arrayOf("99"))
        Log.e(TAG, "删除 $delete 条数据受影响")
        tv_msg.text = String.format("删除 $delete 条数据受影响")
    }

    /**
     *数据缓存
     */
    fun cacheData(view: View) {
        tv_msg.text = httpInfo(true)
    }

    /**
     * 数据引擎
     */
    fun httpEngine(view: View) {
        tv_msg.text = httpInfo(false)
    }

    private fun httpInfo(isCache: Boolean): String? {
        var resultStr: String? = null
        HttpUtils.with(this)
                .exchangeEngine(OkHttpEngine())
                .url("https://www.wanandroid.com/banner/json")
                .addParams()
                .cache(isCache)
                .execute(object : HttpCallBack<ImageBean>() {
                    override fun onFail(exception: Exception) {
                        resultStr = exception.message
                    }

                    override fun onSuccess(result: ImageBean) {
                        resultStr = result.data.toString()
                    }
                })
        return resultStr
    }


}