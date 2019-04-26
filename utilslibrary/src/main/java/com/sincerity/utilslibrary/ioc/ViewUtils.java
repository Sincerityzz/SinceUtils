package com.sincerity.utilslibrary.ioc;

import android.app.Activity;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.text.TextUtils;
import android.view.View;
import android.widget.Toast;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * Created by Sincerity on 2019/4/9.
 * 描述：
 */
public class ViewUtils {
    //兼容fragment
    public static void bind(View view) {
        bind(new ViewFinder(view), view);
    }

    //兼容Activity
    public static void bind(Activity activity) {
        bind(new ViewFinder(activity), activity);
    }

    //兼容adapter
    public static void bind(View view, Object object) {
        bind(new ViewFinder(view), object);
    }

    private static void bind(ViewFinder finder, Object object) {
        injectFiled(finder, object);
        injectEvent(finder, object);
    }

    //注解属性
    private static void injectFiled(ViewFinder finder, Object object) {
        Class<?> aClass = object.getClass();
        //得到类中所有的属性
        Field[] fields = aClass.getDeclaredFields();
        for (Field field : fields) {
            //得到注解后属性
            field.setAccessible(true);
            BindView viewById = field.getAnnotation(BindView.class);
            if (viewById != null) {
                //得到注解后属性的值
                int viewId = viewById.value();
                //通过属性的值得到这个View
                View view = finder.findById(viewId);
                if (view != null) {
                    //暴力反射 获取私有和共有的属性
//                    field.setAccessible(true);
                    try {
                        //反射注入View的属性
                        field.set(object, view);
                    } catch (IllegalAccessException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }

    //注解事件
    private static void injectEvent(ViewFinder finder, Object object) {
        Class<?> aClass = object.getClass();
        //得到类中所有的方法
        Method[] methods = aClass.getDeclaredMethods();
        for (Method method : methods) {
            //的到注解后的方法
            OnClick onClick = method.getAnnotation(OnClick.class);
            if (onClick != null) {
                //得到注解后的值
                int[] value = onClick.value();
                for (int viewId : value) {
                    //通过值找到View
                    View view = finder.findById(viewId);
                    boolean isCheckNet = method.getAnnotation(CheckNet.class) != null;
                    String msg = null;
                    if (isCheckNet) {
                        msg = method.getAnnotation(CheckNet.class).value();
                    }
                    if (view != null) {
                        //给View设置监听
                        view.setOnClickListener(new DeclaredOnClickListener(method, object, isCheckNet, msg));
                    }
                }
            }
        }
    }

    private static class DeclaredOnClickListener implements View.OnClickListener {
        private Method mMethod;
        private Object mObject;
        private boolean isChecked;
        private String msg;

        public DeclaredOnClickListener(Method mMethod, Object mObject, boolean isChecked, String msg) {
            this.mMethod = mMethod;
            this.mObject = mObject;
            this.isChecked = isChecked;
            this.msg = msg;
        }

        @Override
        public void onClick(View v) {
            if (isChecked) {
                if (!isNetworkConnected(v.getContext())) {
                    Toast.makeText(v.getContext(), TextUtils.isEmpty(msg) ? "网络连接有问题,请检查网络状态"
                            : msg, Toast.LENGTH_SHORT).show();
                    return;
                }
            }
            try {
                //反射注入对象
                mMethod.invoke(mObject, v);
            } catch (Exception e) {
                e.printStackTrace();
                try {
                    mMethod.invoke(mObject, null);
                } catch (IllegalAccessException e1) {
                    e1.printStackTrace();
                } catch (InvocationTargetException e1) {
                    e1.printStackTrace();
                }
            }
        }

        private boolean isNetworkConnected(Context context) {
            if (context != null) {
                ConnectivityManager manager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
                NetworkInfo networkInfo = manager.getActiveNetworkInfo();
                if (networkInfo != null) {
                    return networkInfo.isConnected();
                }
            }
            return false;
        }
    }
}
