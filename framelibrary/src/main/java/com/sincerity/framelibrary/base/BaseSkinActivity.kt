package com.sincerity.framelibrary.base

import android.content.Context
import android.content.res.TypedArray
import android.os.Build
import android.os.Bundle
import android.util.AttributeSet
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewParent
import androidx.appcompat.R
import androidx.appcompat.app.AppCompatViewInflater
import androidx.appcompat.widget.VectorEnabledTintResources
import androidx.core.view.LayoutInflaterCompat
import androidx.core.view.ViewCompat
import com.sincerity.framelibrary.defalut.DefaultNavigationBar
import com.sincerity.framelibrary.skin.SkinManager
import com.sincerity.framelibrary.skin.SkinView
import com.sincerity.framelibrary.skin.callback.ISkinListener
import com.sincerity.framelibrary.skin.support.SkinAppCompatViewInFalter
import com.sincerity.framelibrary.skin.support.SkinAttrSupport
import com.sincerity.utilslibrary.base.BaseActivity
import org.xmlpull.v1.XmlPullParser

abstract class BaseSkinActivity : BaseActivity(), ISkinListener {


    private var mAppCompatViewInflater: SkinAppCompatViewInFalter? = null
    private val TAG = javaClass.simpleName
    private val PRE_sLOLLIPOP = Build.VERSION.SDK_INT < 21
    private var builder: DefaultNavigationBar.Builder? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        val layoutInflater = LayoutInflater.from(this)
        LayoutInflaterCompat.setFactory2(layoutInflater, this)
//        val inflater = LayoutInflater.from(this)
//        LayoutInflaterCompat.setFactory2(inflater, object : Factory2 {
//
//            override fun onCreateView(parent: View?, name: String?, context: Context?, attrs: AttributeSet?): View? {
//                if (name.equals("Button")) {
//                    val textView = TextView(context, attrs)
//                    textView.text = "插件换肤"
//                    textView.layoutParams = ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)
//                    textView.setBackgroundColor(Color.GREEN)
//                    textView.setTextColor(Color.WHITE)
//                    return textView
//                }
//                //获取View后去解析View
//                return null
//            }
//
//            override fun onCreateView(name: String?, context: Context?, attrs: AttributeSet?): View? {
//                if (name.equals("Button")) {
//                    val textView = TextView(context)
//                  textView.text = "插件换肤"
//                    textView.setBackgroundColor(Color.GREEN)
//                    textView.setTextColor(Color.WHITE)
//                    return textView
//               }
//                return null
//            }
//
//        })
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(parent: View?, name: String, context: Context, attrs: AttributeSet): View? {
        //创建View
        val view = createView(parent, name, context, attrs)
        //解析属性
        val skinAttrs = SkinAttrSupport.getSkinAttrs(context, attrs)
        if (skinAttrs != null) {
            val skinView = SkinView(view, skinAttrs)
            //统一交给SkinManager去管理
            managerSkinView(skinView)
            //判断是否换肤
            SkinManager.getInstance().checkChangeSkin(skinView)
        }

        return view
    }

    /**
     * 管理SkinView
     */
    private fun managerSkinView(skinView: SkinView) {
        var skinViews = SkinManager.getInstance().getSkinViews(this)
        if (skinViews == null) {
            skinViews = ArrayList()
            SkinManager.getInstance().register(this, skinViews)
        }
        skinViews.add(skinView)
    }


    private fun createView(parent: View?, name: String?, context: Context,
                           attrs: AttributeSet): View? {
        if (mAppCompatViewInflater == null) {
            val a: TypedArray = this.obtainStyledAttributes(R.styleable.AppCompatTheme)
            val viewInflaterClassName = a.getString(R.styleable.AppCompatTheme_viewInflaterClass)
            mAppCompatViewInflater = if (viewInflaterClassName == null
                    || AppCompatViewInflater::class.java.name == viewInflaterClassName) {
                SkinAppCompatViewInFalter()
            } else {
                try {
                    val viewInflaterClass = Class.forName(viewInflaterClassName)
                    viewInflaterClass.getDeclaredConstructor()
                            .newInstance() as SkinAppCompatViewInFalter
                } catch (t: Throwable) {
                    Log.i(TAG, "Failed to instantiate custom view inflater "
                            + viewInflaterClassName + ". Falling back to default.", t)
                    SkinAppCompatViewInFalter()
                }
            }
        }
        var inheritContext = false
        if (PRE_sLOLLIPOP) {
            inheritContext = if (attrs is XmlPullParser) (attrs as XmlPullParser).depth > 1 // Otherwise we have to use the old heuristic
            else shouldInheritContext(parent as ViewParent?)
        }
        return mAppCompatViewInflater?.createView(parent, name!!, context, attrs, inheritContext,
                PRE_sLOLLIPOP,  /* Only read android:theme pre-L (L+ handles this anyway) */
                true,  /* Read read app:theme as a fallback at all times for legacy reasons */
                VectorEnabledTintResources.shouldBeUsed() /* Only tint wrap the context if enabled */
        )
    }

    final override fun initActionBar() {
        DefaultNavigationBar.Builder(this).setTitle(setTitle()).builder()
    }

    abstract fun setTitle(): String
    private fun shouldInheritContext(parent: ViewParent?): Boolean {
        var parent: ViewParent? = parent
                ?: // The initial parent is null so just return false
                return false
        val windowDecor: View = window.decorView
        while (true) {
            if (parent == null) {
                return true
            } else if (parent === windowDecor || parent !is View
                    || ViewCompat.isAttachedToWindow((parent as View?)!!)) {
                return false
            }
            parent = parent.getParent()
        }
    }

    override fun onDestroy() {
        SkinManager.getInstance().unregister(this)
        super.onDestroy()
    }
}
