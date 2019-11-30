package com.sincerity.sinceutils.ui

import android.content.Intent
import android.os.Environment
import android.view.View
import android.widget.Toast
import com.sincerity.framelibrary.base.BaseSkinActivity
import com.sincerity.framelibrary.skin.SkinManager
import com.sincerity.framelibrary.skin.SkinResource
import com.sincerity.sinceutils.R
import java.io.File

/**
 * 插件换肤功能
 * @author Sincerity
 * CreateBy on 2019/11/26  21:20
 */
class SkinPeelerActivity : BaseSkinActivity() {


    override fun setTitle() = "插件换肤"

    override fun initListener() {

    }

    override fun initData() {

    }


    override fun setContentView() = R.layout.activity_skin_peeler

    override fun initViews() {

    }

    override fun chanageSkin(skinRes: SkinResource) {

    }

    fun skinClick(view: View) {
        val file = File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS), "skin_plugin.skin")
        val result = SkinManager.getInstance().loadSkin(file.absolutePath)

        Toast.makeText(this, "解析结果$result", Toast.LENGTH_SHORT).show()
//        val overrideConfig = Configuration()
//        overrideConfig.smallestScreenWidthDp = 99999
//        val mOverrideConfiguration = Configuration(overrideConfig)
//        resources
//        val mContext = createConfigurationContext(mOverrideConfiguration)
//        mContext.resources
//        val context = this.createPackageContext("com.sincerity.skinpugin", Context.CONTEXT_IGNORE_SECURITY)
//        val superRes = resources
//        try {
//            val assetManager = AssetManager::class.java.newInstance()
        //添加本地下载好的资源皮肤
//            val method = AssetManager::class.java.getDeclaredMethod("addAssetPath", String::class.java)
//            val file = File(Intent().setAction(ACTION_OPEN_DOCUMENT), "skin_plugin.skin")
//            val file = File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS), "skin_plugin.skin")
//            val file = File(MediaStore.DownloadColumns.DOWNLOAD_URI, "skin_plugin.skin")
//            val clazzes = MediaStore::class.java.declaredClasses
//            for (clazz in clazzes) {
//                val i: Int = clazz.modifiers
//                val s: String = Modifier.toString(i)
//                if (s.contains("static")) {
//                    if (clazz.simpleName == "Downloads") {
//                        val field = clazz.declaredFields
//                        for (field1 in field) {
//                            val value = field1.get(clazz)
//
//                            Log.e("admin", value.toString())
//                            val path = Uri.parse(value.toString()).path
//                            Log.e("admin", path!!)
//                        }
//
//                    }
//                }
//            }
//            method.invoke(assetManager, file.absolutePath)
//            val resources = Resources(assetManager, superRes.displayMetrics, superRes.configuration)
//            获取ID
//            val drawableId = resources.getIdentifier("image_bg", "mipmap", "com.sincerity.skinpugin")
//            val drawable = resources.getDrawable(drawableId)
//            iv_skin.setImageDrawable(drawable)
//        } catch (e: Exception) {
//            e.printStackTrace()
//        }

    }

    fun restoreSkinClick(view: View) {
        val result = SkinManager.getInstance().reStoreDefault()
        Toast.makeText(this, "解析结果$result", Toast.LENGTH_SHORT).show()
    }

    fun onPageJump(view: View) {
        val intent = Intent(this, Skin1Activity::class.java)
        startActivity(intent)
    }
}


