package com.sincerity.utilslibrary.utils;

import android.util.TypedValue;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

/**
 * Created by Sincerity on 2019/12/19.
 * 描述：界面元素进行赋值
 */
public class ViewScaleUtils {
    public static void setViewLayoutPrams(View view, int width, int height, int leftMargin, int rightMargin, int topMargin, int bottomMargin) {
        RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) view.getLayoutParams();
        if (layoutParams != null) {
            if (width != RelativeLayout.LayoutParams.MATCH_PARENT && width != RelativeLayout.LayoutParams.WRAP_CONTENT) {
                layoutParams.width = UIManager.getInstance().getWidth(width);
            } else {
                layoutParams.width = width;
            }
            if (height != RelativeLayout.LayoutParams.MATCH_PARENT && height != RelativeLayout.LayoutParams.WRAP_CONTENT) {
                layoutParams.height = UIManager.getInstance().getHeight(height);
            } else {
                layoutParams.height = height;
            }
            layoutParams.topMargin = UIManager.getInstance().getHeight(topMargin);
            layoutParams.bottomMargin = UIManager.getInstance().getHeight(bottomMargin);
            layoutParams.leftMargin = UIManager.getInstance().getWidth(leftMargin);
            layoutParams.rightMargin = UIManager.getInstance().getWidth(rightMargin);
        }

    }

    /**
     * 字体适配
     *
     * @param view 适配的View
     * @param size 字体的大小
     */
    public static void setTextSize(TextView view, int size) {
        view.setTextSize(TypedValue.COMPLEX_UNIT_PX, UIManager.getInstance().getHeight(size));
    }
}
