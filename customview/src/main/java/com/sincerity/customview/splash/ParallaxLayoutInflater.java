package com.sincerity.customview.splash;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.sincerity.customview.R;

/**
 * Created by Sincerity on 2019/12/16.
 * 描述：
 */
public class ParallaxLayoutInflater extends LayoutInflater {
    private SplashFragment fragment;

    protected ParallaxLayoutInflater(LayoutInflater original, Context newContext, SplashFragment fragment) {
        super(original, newContext);
        this.fragment = fragment;
        setFactory2(new ParallaxFactory(this));
    }

    protected ParallaxLayoutInflater(LayoutInflater original, Context context) {
        super(original, context);
    }

    @Override
    public LayoutInflater cloneInContext(Context newContext) {
        return new ParallaxLayoutInflater(this, newContext, fragment);
    }

    int[] attrIds = {R.attr.a_in, R.attr.a_out, R.attr.y_in, R.attr.y_out, R.attr.x_in, R.attr.x_out};

    class ParallaxFactory implements Factory2 {
        private LayoutInflater inflater;
        private final String[] sClassPrefix = {"android.widget", "android.view"};

        public ParallaxFactory(LayoutInflater inflater) {
            this.inflater = inflater;
        }

        @SuppressLint({"Recycle", "ResourceType"})
        @Nullable
        @Override
        public View onCreateView(@Nullable View parent, @NonNull String name, @NonNull Context context, @NonNull AttributeSet attrs) {
            Log.e("Tag", "onCreateView");
            View view = createViewForName(context, name, attrs);
            if (view != null) {
                TypedArray att = context.obtainStyledAttributes(attrs, attrIds);
                if (att.length() > 0) {
                    ParallaxViewTag tag = new ParallaxViewTag();
                    tag.alphaIn = att.getFloat(0, 0f);
                    tag.alphaOut = att.getFloat(1, 0f);
                    tag.xIn = att.getFloat(2, 0f);
                    tag.xOut = att.getFloat(3, 0f);
                    tag.yIn = att.getFloat(4, 0f);
                    tag.yOut = att.getFloat(5, 0f);
                    view.setTag(R.id.parallax_view, tag);
                }
                fragment.getParallaxViews().add(view);
                att.recycle();
            }
            return view;
        }

        @Nullable
        @Override
        public View onCreateView(@NonNull String name, @NonNull Context context, @NonNull AttributeSet attrs) {

            return null;
        }

        private View reflectView(String name, String prefix, Context context, AttributeSet attrs) {
            try {
                return inflater.createView(name, prefix, attrs);
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
            return null;
        }

        private View createViewForName(Context context, String name, AttributeSet attrs) {
            if (name.contains(".")) {
                return reflectView(name, null, context, attrs);
            } else {
                for (String classPrefix : sClassPrefix) {
                    View view = reflectView(name, classPrefix, context, attrs);
                    //获取系统控件的自定义属性attrs属性集合

                    if (view != null) {
                        return view;
                    }
                }
            }
            return null;
        }
    }


}
