package com.sincerity.framelibrary.skin.support

import android.content.Context
import android.content.ContextWrapper
import android.os.Build
import android.util.AttributeSet
import android.util.Log
import android.view.InflateException
import android.view.View
import androidx.appcompat.R
import androidx.appcompat.view.ContextThemeWrapper
import androidx.appcompat.widget.*
import androidx.collection.ArrayMap
import androidx.core.view.ViewCompat
import java.lang.reflect.Constructor
import java.lang.reflect.InvocationTargetException
import java.lang.reflect.Method

/**
 * Created by Sincerity on 2019/11/27.
 * 描述：
 */
class SkinAppCompatViewInFalter {

    private val sConstructorSignature = arrayOf(
            Context::class.java, AttributeSet::class.java)
    private val sOnClickAttrs = intArrayOf(android.R.attr.onClick)

    private val sClassPrefixList = arrayOf(
            "android.widget.",
            "android.view.",
            "android.webkit."
    )

    private val LOG_TAG = "AppCompatViewInflater"

    private val sConstructorMap: MutableMap<String, Constructor<out View>> = ArrayMap()

    private val mConstructorArgs = arrayOfNulls<Any>(2)

    fun createView(parent: View?, name: String, context: Context,
                   attrs: AttributeSet, inheritContext: Boolean,
                   readAndroidTheme: Boolean, readAppTheme: Boolean, wrapContext: Boolean): View? {
        var originalContext: Context? = context
        if (inheritContext && parent != null) {
            originalContext = parent.context
        }
        if (readAndroidTheme || readAppTheme) {
            originalContext = themifyContext(context, attrs, readAndroidTheme, readAppTheme)
        }
        if (wrapContext) {
            originalContext = TintContextWrapper.wrap(context)
        }
        var view: View? = null
        when (name) {
            "TextView" -> view = AppCompatTextView(context, attrs)
            "ImageView" -> view = AppCompatImageView(context, attrs)
            "Button" -> view = AppCompatButton(context, attrs)
            "EditText" -> view = AppCompatEditText(context, attrs)
            "Spinner" -> view = AppCompatSpinner(context, attrs)
            "ImageButton" -> view = AppCompatImageButton(context, attrs)
            "CheckBox" -> view = AppCompatCheckBox(context, attrs)
            "RadioButton" -> view = AppCompatRadioButton(context, attrs)
            "CheckedTextView" -> view = AppCompatCheckedTextView(context, attrs)
            "AutoCompleteTextView" -> view = AppCompatAutoCompleteTextView(context, attrs)
            "MultiAutoCompleteTextView" -> view = AppCompatMultiAutoCompleteTextView(context, attrs)
            "RatingBar" -> view = AppCompatRatingBar(context, attrs)
            "SeekBar" -> view = AppCompatSeekBar(context, attrs)
        }
        if (view == null) {
            view = createViewFromTag(originalContext!!, name, attrs)
        }
        view?.let { checkOnClickListener(it, attrs) }

        return view
    }

    private fun createViewFromTag(context: Context, name: String, attrs: AttributeSet): View? {
        var mName = name
        if (mName == "view") {
            mName = attrs.getAttributeValue(null, "class")
        }
        return try {
            mConstructorArgs[0] = context
            mConstructorArgs[1] = attrs
            if (-1 == mName.indexOf('.')) {
                for (i in sClassPrefixList.indices) {
                    val view = createViewByPrefix(context, name, sClassPrefixList[i])
                    if (view != null) {
                        return view
                    }
                }
                null
            } else {
                createViewByPrefix(context, name, null)
            }
        } catch (e: Exception) {
            null
        } finally {
            mConstructorArgs[0] = null
            mConstructorArgs[1] = null
        }
    }

    /**
     * android:onClick doesn't handle views with a ContextWrapper context. This method
     * backports new framework functionality to traverse the Context wrappers to find a
     * suitable target.
     */
    private fun checkOnClickListener(view: View, attrs: AttributeSet) {
        val context = view.context
        if (context !is ContextWrapper ||
                Build.VERSION.SDK_INT >= 19 && !ViewCompat.hasOnClickListeners(view)) {
            return
        }
        val a = context.obtainStyledAttributes(attrs, sOnClickAttrs)
        val handlerName = a.getString(0)
        if (handlerName != null) {
            view.setOnClickListener(DeclaredOnClickListener(view, handlerName))
        }
        a.recycle()
    }

    @Throws(ClassNotFoundException::class, InflateException::class)
    private fun createViewByPrefix(context: Context, name: String, prefix: String?): View? {
        var constructor = sConstructorMap[name]
        return try {
            if (constructor == null) {
                val clazz = Class.forName(
                        if (prefix != null) prefix + name else name,
                        false,
                        context.classLoader).asSubclass(View::class.java)
                constructor = clazz.getConstructor(*sConstructorSignature)
                sConstructorMap[name] = constructor
            }
            constructor.isAccessible = true
            constructor.newInstance(*mConstructorArgs)
        } catch (e: Exception) {
            null
        }
    }

    /**
     * Allows us to emulate the `android:theme` attribute for devices before L.
     */
    private fun themifyContext(context: Context, attrs: AttributeSet,
                               useAndroidTheme: Boolean, useAppTheme: Boolean): Context {
        var mContext = context
        val a = context.obtainStyledAttributes(attrs, R.styleable.View, 0, 0)
        var themeId = 0
        if (useAndroidTheme) {
            themeId = a.getResourceId(R.styleable.View_android_theme, 0)
        }
        if (useAppTheme && themeId == 0) {
            themeId = a.getResourceId(R.styleable.View_theme, 0)
            if (themeId != 0) {
                Log.i(LOG_TAG, "app:theme is now deprecated. Please move to using android:theme instead.")
            }
        }
        a.recycle()
        if (themeId != 0 && (mContext !is ContextThemeWrapper
                        || mContext.themeResId != themeId)) {
            mContext = ContextThemeWrapper(mContext, themeId)
        }
        return mContext
    }

    /**
     * An implementation of OnClickListener that attempts to lazily load a
     * named click handling method from a parent or ancestor context.
     */
    private class DeclaredOnClickListener(private val mHostView: View, private val mMethodName: String) : View.OnClickListener {
        private var mResolvedMethod: Method? = null
        private var mResolvedContext: Context? = null
        override fun onClick(v: View) {
            if (mResolvedMethod == null) {
                resolveMethod(mHostView.context, mMethodName)
            }
            try {
                mResolvedMethod!!.invoke(mResolvedContext, v)
            } catch (e: IllegalAccessException) {
                throw IllegalStateException(
                        "Could not execute non-public method for android:onClick", e)
            } catch (e: InvocationTargetException) {
                throw IllegalStateException(
                        "Could not execute method for android:onClick", e)
            }
        }

        private fun resolveMethod(context: Context?, name: String) {
            var mContext = context
            while (mContext != null) {
                try {
                    if (!mContext.isRestricted) {
                        val method = mContext.javaClass.getMethod(mMethodName, View::class.java)
                        mResolvedMethod = method
                        mResolvedContext = context
                        return
                    }
                } catch (e: NoSuchMethodException) {
                }
                mContext = if (mContext is ContextWrapper) {
                    mContext.baseContext
                } else {
                    null
                }
            }
            val id = mHostView.id
            val idText = if (id == View.NO_ID) "" else " with id '${mHostView.context.resources.getResourceEntryName(id)} '"
            throw IllegalStateException("Could not find method $mMethodName (View) in a parent or ancestor Context for android:onClick " +
                    "attribute defined on view ${mHostView.javaClass} $idText")
        }

    }
}