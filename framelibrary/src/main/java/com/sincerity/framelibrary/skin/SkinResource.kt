package com.sincerity.framelibrary.skin

import android.content.Context
import android.content.pm.PackageManager
import android.content.res.AssetManager
import android.content.res.ColorStateList
import android.content.res.Resources
import android.content.res.Resources.NotFoundException
import android.graphics.drawable.Drawable
import android.util.Log

/**
 * Created by Sincerity on 2019/11/27.
 * 描述：皮肤的资源管理
 */
class SkinResource(context: Context, skinPath: String) {
    private var mSkinResource: Resources? = null
    private var packageName = ""

    init {
        try {
            val superRes = context.resources
            val assetManager = AssetManager::class.java.newInstance()
            //添加本地下载好的资源皮肤
            val method = AssetManager::class.java.getDeclaredMethod("addAssetPath", String::class.java)
//            val file = File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS), "skin_plugin.skin")
            method.invoke(assetManager, skinPath)
            mSkinResource = Resources(assetManager, superRes.displayMetrics, superRes.configuration)
//            packageName = context.packageManager.getPackageInfo(skinpath, PackageManager.GET_ACTIVITIES).applicationInfo.packageName
            packageName = context.packageManager.getPackageArchiveInfo(skinPath, PackageManager.GET_ACTIVITIES).applicationInfo.packageName
        } catch (e: Exception) {
            e.printStackTrace()
        }

    }

    /**
     * 通过ID获取图片
     */
    fun getDrawableByName(resName: String): Drawable? {
        var resId = 0
        try {
            if (mSkinResource != null) {
                resId = mSkinResource!!.getIdentifier(resName, "mipmap", packageName)
            }
        } catch (e: NotFoundException) {
            e.printStackTrace()
        }
        if (resId == 0) {
            return null
        }
        return mSkinResource?.getDrawable(resId)
    }

    //通过ID获取颜色
    fun getColorByName(resName: String): ColorStateList? {
        Log.e("admin", "---------------> $resName")
        var stateList: ColorStateList? = null
        try {
            val resId = mSkinResource!!.getIdentifier(resName, "color", packageName)
            stateList = mSkinResource!!.getColorStateList(resId)
        } catch (e: NotFoundException) {
            return null
        }
        return stateList
    }
}