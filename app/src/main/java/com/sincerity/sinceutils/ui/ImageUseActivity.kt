package com.sincerity.sinceutils.ui

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Environment
import android.view.View
import androidx.core.content.FileProvider
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.sincerity.framelibrary.base.BaseSkinActivity
import com.sincerity.framelibrary.skin.SkinResource
import com.sincerity.sinceutils.R
import com.sincerity.sinceutils.common.ImageConstant
import com.sincerity.sinceutils.common.ImageConstant.SELECT_IMAGE_STARTACTIVITY_FLAG
import com.sincerity.sinceutils.image.ImageSelector
import com.sincerity.sinceutils.utils.PathUtil
import com.sincerity.utilslibrary.view.RecycleView.decoration.GridLayoutDecoration
import java.io.File
import java.util.*

/**
 * 沉浸式状态栏:
 *
 */
class ImageUseActivity : BaseSkinActivity() {
    private var mImageList = ArrayList<String>()
    private lateinit var mRecyclerView: RecyclerView
    override fun setTitle() = "图片测试"
    private val patch_path = Environment.getExternalStorageDirectory().absolutePath + File.separator + "version_1.0_2.0.patch"
    private val newApkPath = Environment.getExternalStorageDirectory().absolutePath + File.separator + "version2.0.apk"
    override fun initListener() {

    }

    override fun initData() {
        mRecyclerView.layoutManager = GridLayoutManager(this, 3)
        mRecyclerView.addItemDecoration(GridLayoutDecoration(this, 0))
        //访问后台接口需不需要去更新版本
        //需要更新版本, 要么提示需要下载, (已为您节省000X流量)
        //直接后台下载,提示更新
        //下载完整分包之后,调用我们的方法去合并生成的新APK 耗时操作
        //本地APk路径 packageResourcePath()
        if (!File(newApkPath).exists()) {
            return
        }
        PathUtil.combine(packageResourcePath, newApkPath, patch_path)
        //签名校验

        //安装最新版的APK
        installApk(this, File(newApkPath))
    }

    override fun setContentView() = R.layout.activity_image_use

    override fun initViews() {
        mRecyclerView = findViewById(R.id.id_select_container)
    }

    override fun chanageSkin(skinRes: SkinResource) {

    }

    //选择图片
    fun selectImage(view: View) {

        ImageSelector().create().count(9)
                .multi()
                .isShowCamera(true)
                .origin(mImageList)
                .start(this, SELECT_IMAGE_STARTACTIVITY_FLAG)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK && data != null) {
            if (requestCode == SELECT_IMAGE_STARTACTIVITY_FLAG) {
                mImageList = data.getStringArrayListExtra(ImageConstant.EXTRA_DEFAULT_SELECTED_LIST)!!
            }
        }
    }

    private fun installApk(context: Context, apkFile: File) {
        val installApkIntent = Intent()
        installApkIntent.action = Intent.ACTION_VIEW
        installApkIntent.addCategory(Intent.CATEGORY_DEFAULT)
        installApkIntent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
        //适配7.0+
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.M) {
            installApkIntent.setDataAndType(FileProvider.getUriForFile(context, "com.sincerity.sinceutils", apkFile), "application/vnd.android.package-archive")
            installApkIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
        } else {
            installApkIntent.setDataAndType(Uri.fromFile(apkFile), "application/vnd.android.package-archive")
        }
        if (context.packageManager.queryIntentActivities(installApkIntent, 0).size > 0) {
            context.startActivity(installApkIntent)
        }
    }
}
