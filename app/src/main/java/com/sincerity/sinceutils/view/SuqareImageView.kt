package com.sincerity.sinceutils.view

import android.content.Context
import android.util.AttributeSet
import android.widget.ImageView

/**
 * Created by Sincerity on 2019/12/3.
 * 描述：
 */
class SuqareImageView @JvmOverloads constructor(
        context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : ImageView(context, attrs, defStyleAttr) {
    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        val width = MeasureSpec.getSize(widthMeasureSpec)
        setMeasuredDimension(width, width)
    }
}