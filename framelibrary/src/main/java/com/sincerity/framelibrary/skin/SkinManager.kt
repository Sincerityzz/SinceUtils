package com.sincerity.framelibrary.skin

import android.content.Context
import android.content.pm.PackageManager
import android.text.TextUtils
import android.util.ArrayMap
import com.sincerity.framelibrary.skin.callback.ISkinListener
import com.sincerity.framelibrary.skin.config.Preference
import com.sincerity.framelibrary.skin.config.SkinConfig
import java.io.File

/**
 * Created by Sincerity on 2019/11/27.
 * 描述：皮肤管理类
 */
class SkinManager {
    private lateinit var mContext: Context
    private val mSkinViews = ArrayMap<ISkinListener, ArrayList<SkinView>>()
    private var mSkinResource: SkinResource? = null
    private val variable by lazy {
        Preference(mContext, SkinConfig.skin_path, "")
    }

    /**
     * 静态代码块
     */
    companion object {
        //等价于static{}
        private var mInstance = SkinManager()

        fun getInstance(): SkinManager {
            return mInstance
        }


    }

    fun init(context: Context) {
        this.mContext = context.applicationContext
        //防止皮肤包被删除
        val path = variable.getValue(SkinConfig.skin_path, null)
        if (TextUtils.isEmpty(path)) {
            return
        }
        val file = File(path)
        if (!file.exists()) {
            variable.clearPreference(SkinConfig.skin_path)
            return
        }
        //校验签名
        mSkinResource = SkinResource(mContext, path)
    }


    /**
     * 加载皮肤
     */
    fun loadSkin(skinPath: String): Int {
        val file = File(skinPath)
        if (!file.exists()) {
            return SkinConfig.SkinConfig_FILE_NOTFOUND
        }
        //判断包名
        val packageName: String? = mContext.packageManager.getPackageArchiveInfo(skinPath, PackageManager.GET_ACTIVITIES).applicationInfo.packageName
        if (TextUtils.isEmpty(packageName)) {
            return SkinConfig.ITSNOTTHEDOCUMENTWENEED
        }
        val value = variable.getValue(SkinConfig.skin_path, null)
        if (skinPath == value) {
            return SkinConfig.SKIN_DEFAULT
        }
        //初始化资源管理 和校验签名
        mSkinResource = SkinResource(mContext, skinPath)
        loadSkinForSkinView()
        saveSkinState(skinPath)
        return SkinConfig.SKIN_SUCCESS
    }


    /**
     * 保存皮肤的状态
     * Kotlin的伴生对象
     */
    private fun saveSkinState(skinPath: String) {
        variable.setValue(SkinConfig.skin_path, null, skinPath)
    }

    /**
     * 撤回已经加载的皮肤
     */
    fun reStoreDefault(): Int {
        //恢复默认 1.判断当前是否有皮肤
        val value = variable.getValue(SkinConfig.skin_path, null)
        if (TextUtils.isEmpty(value)) {
            return SkinConfig.SKIN_DEFAULT
        }
        //skinPath 这里就是原来APK的资源文件路径
        val skinPath = mContext.packageResourcePath
        mSkinResource = SkinResource(mContext, skinPath)
        loadSkinForSkinView()
        variable.clearPreference(SkinConfig.skin_path)
        return SkinConfig.SKIN_SUCCESS
    }

    /**
     * 改变皮肤
     */
    private fun loadSkinForSkinView() {
        val keys = mSkinViews.keys
        for (key in keys) {
            val skinViews = mSkinViews[key]
            if (skinViews != null) {
                for (skinView in skinViews) {
                    skinView.skin()
                }
            }
            //通知View换肤
            if (mSkinResource != null) {
                key.chanageSkin(mSkinResource!!)
            }
        }
    }

    /**
     * 获取SkinView通过Activity
     */
    fun getSkinViews(skinListener: ISkinListener): ArrayList<SkinView>? {
        return mSkinViews[skinListener]
    }

    /**
     * 注册
     */
    fun register(skinListener: ISkinListener, skinViews: ArrayList<SkinView>) {
        mSkinViews[skinListener] = skinViews
    }

    /**
     * 获取当前的皮肤资源管理
     */
    fun getSkinResource(): SkinResource? {
        if (mSkinResource != null) {
            return mSkinResource
        }
        return null
    }

    /**
     * 检测是否需要换肤
     */
    fun checkChangeSkin(skinView: SkinView) {
        //如果当前保存了皮肤路径 就换肤
        val value = variable.getValue(SkinConfig.skin_path, null)
        if (!TextUtils.isEmpty(value)) {
            skinView.skin()
        }
    }

    /**
     * 解决内存泄露问题
     */
    fun unregister(iSkinListener: ISkinListener) {
        mSkinViews.remove(iSkinListener)
    }
}