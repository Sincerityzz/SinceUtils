package com.sincerity.utilslibrary.view.indicator;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.FrameLayout;
import android.widget.LinearLayout;

/**
 * Created by Sincerity on 2019/9/26.
 * 描述：包含ItemView和底部指示器
 */
public class IndicatorView extends FrameLayout {
    private LinearLayout mIndicatorGroup;//指示器容器

    public IndicatorView(Context context) {
        this(context, null);
    }

    public IndicatorView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public IndicatorView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }
}
