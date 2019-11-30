package com.sincerity.sinceutils

import android.content.Intent
import android.os.Bundle
import android.view.Gravity
import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatMultiAutoCompleteTextView
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.sincerity.framelibrary.defalut.DefaultNavigationBar
import com.sincerity.sinceutils.ui.IocActivity
import com.sincerity.sinceutils.ui.LoginActivity
import com.sincerity.sinceutils.ui.SQLDateBaseActivity
import com.sincerity.sinceutils.ui.SkinPeelerActivity
import com.sincerity.utilslibrary.dialog.SinceDialog
import com.sincerity.utilslibrary.view.RecycleView.adapter.BaseAdapter
import com.sincerity.utilslibrary.view.RecycleView.adapter.BaseViewHolder
import com.sincerity.utilslibrary.view.RecycleView.decoration.RecycleViewItemDecoration
import kotlinx.android.synthetic.main.activity_kotlin.*

class KotlinActivity : AppCompatActivity() {
    private var adapter: BaseAdapter<String>? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_kotlin)
        val data = resources.getStringArray(R.array.main_item).asList()
        DefaultNavigationBar.Builder(this).setTitle("数据测试").builder()
        adapter = object : BaseAdapter<String>(this, android.R.layout.simple_list_item_1, data) {

            override fun setData(baseViewHolder: BaseViewHolder?, t: String?, i: Int) {
                if (baseViewHolder != null) {
                    val view = baseViewHolder.getView<TextView>(android.R.id.text1)
                    view.gravity = Gravity.CENTER
                    view.text = t
                }

            }

        }
        initView()
    }

    private fun initView() {
        DefaultNavigationBar.Builder(this).setTitle("测试学习")
        exRecycle.layoutManager = LinearLayoutManager(this)
        exRecycle.addItemDecoration(RecycleViewItemDecoration(3))
        exRecycle.setRecycledViewPool(RecyclerView.RecycledViewPool())
        exRecycle.itemAnimator = DefaultItemAnimator()
        exRecycle.adapter = adapter
        adapter?.setOnItemClickListener { _, p ->
            val intent = Intent()
            var clazz: Class<*>? = null
            when (p) {
                0 -> { //Ioc
                    clazz = IocActivity::class.java
                }
                1 -> {
                    clazz = LoginActivity::class.java
                }
                2 -> {
                    getDialog()
                }
                3 -> {
                    clazz = SQLDateBaseActivity::class.java
                }
                4->{
                    clazz=SkinPeelerActivity::class.java
                }
                else -> {

                }
            }
            if (clazz != null) {
                intent.setClass(this, clazz)
                startActivity(intent)
            }

        }

    }


    /**
     * 建造者模式创建的万能的对话框
     *
     */
    private fun getDialog() {
        val dialog = SinceDialog.Builder(this)
                .setContentView(R.layout.dialog)
                .setText(R.id.cancel, "再想想")
                .fromBottom(true)
                .fullWindow()
                .show()
        val textView = dialog.getView<AppCompatMultiAutoCompleteTextView>(R.id.content)
        dialog.openKeyBoard(textView!!)
        dialog.setOnClickListener(R.id.ok, View.OnClickListener { Toast.makeText(this, textView.text.toString(), Toast.LENGTH_SHORT).show() })
        dialog.setOnClickListener(R.id.cancel, View.OnClickListener {
            dialog.closeKeyBoard(textView)
            dialog.dismiss()
            Toast.makeText(this, "关闭", Toast.LENGTH_SHORT).show()
        })
    }

}
