package com.sincerity.sinceutils.ui

import com.sincerity.framelibrary.base.BaseSkinActivity
import com.sincerity.framelibrary.skin.SkinResource
import com.sincerity.sinceutils.R
import kotlinx.android.synthetic.main.activity_skin1.*

class Skin1Activity : BaseSkinActivity() {

    override fun setTitle() = "皮肤包测试"

    override fun initListener() {
    }

    override fun initData() {

    }

    override fun setContentView() = R.layout.activity_skin1

    override fun initViews() {

    }

    override fun chanageSkin(skinRes: SkinResource) {
        val colorByName = skinRes.getColorByName("bg_color")
        if (colorByName!=null){
            skin_layout.setBackgroundColor(colorByName.defaultColor)
        }
    }
}
