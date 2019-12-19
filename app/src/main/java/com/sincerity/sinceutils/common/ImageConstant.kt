package com.sincerity.sinceutils.common

/**
 * Created by Sincerity on 2019/12/2.
 * 描述：
 */
object ImageConstant {

    //选择模式
    val MODE_MULTI = 0x0011
    val MODE_SINGLE = 0x0012
    //加载所有数据
    val LOADER_TYPE = 0x0021
    //传递过来的Key
    val EXTRA_SHOW_CAMERA = "EXTRA_SHOW_CAMERA" //是否显示相机的extra
    val EXTRA_SELECT_COUNT = "EXTRA_SELECT_COUNT" //最多显示多少张图片的extra_key
    val EXTRA_DEFAULT_SELECTED_LIST = "EXTRA_DEFAULT_SELECTED_LIST"//原始的图片路径extra_key
    val EXTRA_SELECT_MODE = "EXTRA_SELECT_MODEs"//选择模式
    val EXTRA_RESULT = "EXTRA_RESULT"//返回选择推按列表
    val SELECT_IMAGE_STARTACTIVITY_FLAG = 0x0011 //相册
    val CAMARE: Int = 0x0012 //相机
}