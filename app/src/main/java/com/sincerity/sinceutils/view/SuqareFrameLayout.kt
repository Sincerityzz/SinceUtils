package com.sincerity.sinceutils.view

import android.content.Context
import android.util.AttributeSet
import android.widget.FrameLayout

/**
 * Created by Sincerity on 2019/12/3.
 * 描述：  正方形的容器
 */
class SuqareFrameLayout @JvmOverloads constructor(
        context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : FrameLayout(context, attrs, defStyleAttr) {
    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        val width = MeasureSpec.getSize(widthMeasureSpec)
        setMeasuredDimension(width, width)
    }
}