package com.sincerity.customview.full;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.RelativeLayout;

import com.sincerity.utilslibrary.utils.UIManager;

/**
 * Created by Sincerity on 2019/12/19.
 * 描述：
 */
public class UiRelateLayout extends RelativeLayout {
    private boolean flag = true;

    public UiRelateLayout(Context context) {
        super(context);
    }

    public UiRelateLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public UiRelateLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        if (flag) {
            float scaleX = UIManager.getInstance(getContext()).getHorizonScaleValue();
            float scaleY = UIManager.getInstance(getContext()).getVerticalScaleVale();
            int childCount = getChildCount();
            for (int i = 0; i < childCount; i++) {
                View child = getChildAt(i);
                LayoutParams layoutParams = (LayoutParams) child.getLayoutParams();
                layoutParams.width = (int) (layoutParams.width * scaleX);
                layoutParams.height = (int) (layoutParams.height * scaleY);
                layoutParams.leftMargin = (int) (layoutParams.leftMargin * scaleX);
                layoutParams.rightMargin = (int) (layoutParams.rightMargin * scaleX);
                layoutParams.topMargin = (int) (layoutParams.topMargin * scaleY);
                layoutParams.bottomMargin = (int) (layoutParams.bottomMargin * scaleY);
            }
        }
    }
}
