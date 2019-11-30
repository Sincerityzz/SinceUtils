package com.sincerity.utilslibrary.navigation

/**
 * Created by Sincerity on 2019/11/5.
 * 描述：定义导航栏的接口的规范 Builder设计模式的接口
 */
interface INavigation {
    //头部的规范
    fun bindViewById(): Int

    //绑定头部参数
    fun applyView()

}