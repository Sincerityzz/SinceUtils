package com.sincerity.framelibrary.skin.support

import android.content.Context
import android.content.res.Resources
import android.text.TextUtils
import android.util.AttributeSet
import com.sincerity.framelibrary.skin.SkinAttr
import com.sincerity.framelibrary.skin.SkinType

/**
 * Created by Sincerity on 2019/11/27.
 * 描述：皮肤属性的支持类
 */
object SkinAttrSupport {
    /**
     * 获取SkinAttr的属性
     */
    fun getSkinAttrs(context: Context, attrs: AttributeSet): ArrayList<SkinAttr>? {
        //bg  src   textColor
        val list: ArrayList<SkinAttr> = arrayListOf()
        val count = attrs.attributeCount
        //包含是.. 不包含是until
        for (index in 0 until count) {
            val name = attrs.getAttributeName(index)
            val value = attrs.getAttributeValue(index)
            val skinType = getSkinType(name)
            if (skinType != null) {
                val resName = getResName(context, value)
                if (TextUtils.isEmpty(resName)) {
                    continue
                }
                val skinAttr = SkinAttr(resName!!, skinType)
                list.add(skinAttr)
            }
        }
        return list
    }

    private fun getResName(context: Context, value: String): String? {
        if (value.startsWith("@")) {
            try {
                val resId = value.substring(1).toInt()
                return context.resources.getResourceEntryName(resId)
            } catch (e: Resources.NotFoundException) {
                e.printStackTrace()
            }
        }
        return null
    }

    //通过名称获取SkinType
    private fun getSkinType(name: String?): SkinType? {
        val skinTypes = SkinType.values()
        for (skinType in skinTypes) {
            if (skinType.getResourceName() == name) {
                return skinType
            }
        }
        return null
    }

}