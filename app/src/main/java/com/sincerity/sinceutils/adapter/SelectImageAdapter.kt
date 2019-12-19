package com.sincerity.sinceutils.adapter

import android.content.Context
import android.text.TextUtils
import android.view.Gravity
import android.view.View
import android.widget.ImageView
import android.widget.Toast
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.sincerity.sinceutils.R
import com.sincerity.sinceutils.bean.SelectImageBean
import com.sincerity.sinceutils.image.itf.SelectImageListener
import com.sincerity.utilslibrary.view.RecycleView.adapter.BaseAdapter
import com.sincerity.utilslibrary.view.RecycleView.adapter.BaseViewHolder


/**
 * Created by Sincerity on 2019/12/2.
 * 描述：
 */
class SelectImageAdapter(context: Context, data:
ArrayList<SelectImageBean>, private val mResultList: ArrayList<String>, private val mMaxCount: Int)
    : BaseAdapter<SelectImageBean>(context, R.layout.item_select_img, data) {
    private val mContext = context
    var mListener: SelectImageListener? = null
    var mCameraListener: SelectImageListener? = null
    override fun setData(baseViewHolder: BaseViewHolder?, t: SelectImageBean, i: Int) {
        if (baseViewHolder != null) {
            if (TextUtils.isEmpty(t.path)) {
                showCameraIcon(baseViewHolder,i)
            } else {
                showNormalIcon(baseViewHolder, t, i)
            }

        }

    }

    /**
     * 加载正常的图标
     */
    private fun showNormalIcon(baseViewHolder: BaseViewHolder, t: SelectImageBean, i: Int) {
        //显示图片
        baseViewHolder.setViewVisibility(R.id.camera_ll, View.INVISIBLE)
        baseViewHolder.setViewVisibility(R.id.id_media_indicator, View.VISIBLE)
        //                baseViewHolder.setViewVisibility(R.id.id_mask, View.VISIBLE)
        baseViewHolder.setViewVisibility(R.id.id_img, View.VISIBLE)
        //显示图片了
        val view = baseViewHolder.getView<ImageView>(R.id.id_img)
        //加载图片
        val options = RequestOptions()
        Glide.with(mContext).load(t.path).apply(options).centerCrop().into(view)
        //设置选中
        val indicatorView = baseViewHolder.getView<ImageView>(R.id.id_media_indicator)
        val path = t.path
        indicatorView.isSelected = mResultList.contains(path)
        baseViewHolder.itemView.setOnClickListener {

            if (mResultList.contains(path)) {
                mResultList.remove(path)
            } else {
                if (mResultList.size > mMaxCount) {
                    val toast = Toast.makeText(mContext, "最多只能选择$mMaxCount 张图片", Toast.LENGTH_SHORT)
                    toast.setGravity(Gravity.CENTER, 0, 0)
                    toast.show()
                    return@setOnClickListener
                }
                mResultList.add(path)
            }
            notifyItemChanged(i)
            mListener?.selectImage(i)
        }
    }

    /**
     * 首位显示相机图标
     */
    private fun showCameraIcon(baseViewHolder: BaseViewHolder,position:Int) {
        //显示拍照
        baseViewHolder.setViewVisibility(R.id.camera_ll, View.VISIBLE)
        baseViewHolder.setViewVisibility(R.id.id_media_indicator, View.INVISIBLE)
        //                baseViewHolder.setViewVisibility(R.id.id_mask, View.INVISIBLE)
        baseViewHolder.setViewVisibility(R.id.id_img, View.INVISIBLE)
        baseViewHolder.itemView.setOnClickListener {
            mCameraListener?.selectImage(position)
        }
    }

}