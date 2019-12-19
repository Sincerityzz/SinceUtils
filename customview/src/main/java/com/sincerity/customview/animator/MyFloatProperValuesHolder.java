package com.sincerity.customview.animator;

import android.view.View;

import java.lang.ref.WeakReference;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * Created by Sincerity on 2019/12/13.
 * 描述：
 */
class MyFloatProperValuesHolder {
    private String mPropertyName; //属性名称
     Class mValueType;//
    private Method mSetter = null;
    private MyKeyFarmedSet myKeyFrames;

    MyFloatProperValuesHolder(String propertyName, float... values) {
        this.mPropertyName = propertyName;
        this.mValueType = float.class;
        myKeyFrames = MyKeyFarmedSet.ofFloat(values);
    }

    void setUpSetter(WeakReference<View> target) {
        char mFirstLetter = Character.toUpperCase(mPropertyName.charAt(0));
        String s = mPropertyName.substring(1);
        String methodName = "set" + mFirstLetter + s;
        try {
            mSetter = View.class.getMethod(methodName, float.class);
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        }
    }

    void setAnimatedValue(View view, float fraction) {
        Object value = myKeyFrames.getValue(fraction);
        try {
            mSetter.invoke(view,value);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
    }
}
