package com.sincerity.sinceutils.ui

import android.widget.Toast
import androidx.appcompat.widget.AppCompatTextView
import com.sincerity.framelibrary.defalut.DefaultNavigationBar
import com.sincerity.sinceutils.R
import com.sincerity.utilslibrary.base.BaseActivity
import com.sincerity.utilslibrary.exception.ExceptionCrashHandler
import com.sincerity.utilslibrary.ioc.BindView
import com.sincerity.utilslibrary.ioc.CheckNet
import com.sincerity.utilslibrary.ioc.OnClick
import com.sincerity.utilslibrary.ioc.ViewUtils
import kotlinx.android.synthetic.main.activity_ioc.*
import java.io.FileInputStream
import java.io.IOException
import java.io.InputStreamReader

/**
 * @author sincerity
 * 自定义Ioc注解相关
 */
class IocActivity : BaseActivity() {
    @BindView(R.id.ioc_text)
    private var mIocTextView: AppCompatTextView? = null

    override fun initListener() {

    }

    override fun initData() {
        ioc_text.text = String.format("出现异常信息:${getExceptionFile()}")
    }

    override fun initActionBar() {
        DefaultNavigationBar.Builder(this).setTitle("Ioc注解和收集Crash信息").builder()
    }

    override fun setContentView() = R.layout.activity_ioc

    override fun initViews() {
        ViewUtils.bind(this)
    }

    @OnClick(R.id.btn_ioc)
    fun onIocBtnClick() {
        Toast.makeText(this, "自定义Ioc点击事件", Toast.LENGTH_SHORT).show()
        mIocTextView?.text = String.format("通过IOC设置的文字")
    }

    @OnClick(R.id.btn_isNet)
    @CheckNet("暂时没有网络连接")
    fun onCheckNetClick() {
        Toast.makeText(this, "自定义无网络不响应点击事件", Toast.LENGTH_SHORT).show()
    }

    @OnClick(R.id.btn_crash)
    fun onCrashClick() {
        val dataList = arrayOfNulls<String>(5)
        dataList[10] = "测试"
    }

    private fun getExceptionFile(): String? { //获取上次异常的信息 上传到服务器
        val crashFile = ExceptionCrashHandler.getInstance().crashFile
        var crashText = ""
        var reader: InputStreamReader? = null
        if (crashFile.exists()) { //上传到服务器
            try {
                reader = InputStreamReader(FileInputStream(crashFile))
                val buffer = CharArray(1024)
                var len: Int
                while (reader.read(buffer).also { len = it } != -1) {
                    crashText = String(buffer, 0, len)
                }

            } catch (e: IOException) {
                e.printStackTrace()
            } finally {
                reader?.close()
            }
        }
        return crashText
    }
}
