package com.sincerity.sinceutils.image

import android.app.Activity
import android.content.Intent
import androidx.fragment.app.Fragment
import com.sincerity.sinceutils.common.ImageConstant
import com.sincerity.sinceutils.ui.PicSelectActivity

/**
 * Created by Sincerity on 2019/12/3.
 * 描述：好的框架架构  封装,不能暴露太多方法和类给使用者
 */
class ImageSelector {
    //单选还是多选
    private var mMode = ImageConstant.MODE_MULTI
    //是否显示拍照按钮
    private var isShowPhoneView = true
    //最多选择图片数量
    private var mMaxCount = 9
    //已经选择好的图片
    private var originList = ArrayList<String>()

    fun create(): ImageSelector {
        return ImageSelector()
    }

    fun count(count: Int): ImageSelector {
        this.mMaxCount = count
        return this
    }

    fun isShowCamera(showCamera: Boolean): ImageSelector {
        this.isShowPhoneView = showCamera
        return this
    }

    fun origin(originList: ArrayList<String>): ImageSelector {
        this.originList = originList
        return this
    }

    fun single(): ImageSelector {
        mMode = ImageConstant.MODE_SINGLE
        return this
    }

    fun multi(): ImageSelector {
        mMode = ImageConstant.MODE_MULTI
        return this
    }

    fun start(activity: Activity, requestCode: Int) {
        val intent = Intent(activity, PicSelectActivity::class.java)
        addParamsWithIntent(intent)
        activity.startActivityForResult(intent, requestCode)
    }

    fun start(fragment: Fragment, requestCode: Int) {
        val intent = Intent(fragment.context, PicSelectActivity::class.java)
        addParamsWithIntent(intent)
        fragment.startActivityForResult(intent, requestCode)
    }

    //给Intent添加参数
    private fun addParamsWithIntent(intent: Intent) {
        intent.getIntExtra(ImageConstant.EXTRA_SELECT_COUNT, mMaxCount)
        intent.getBooleanExtra(ImageConstant.EXTRA_SHOW_CAMERA, isShowPhoneView)
        if (mMode == ImageConstant.MODE_MULTI) {
            intent.putStringArrayListExtra(ImageConstant.EXTRA_DEFAULT_SELECTED_LIST, originList)
        }
        intent.getIntExtra(ImageConstant.EXTRA_SELECT_MODE, mMode)
    }
}