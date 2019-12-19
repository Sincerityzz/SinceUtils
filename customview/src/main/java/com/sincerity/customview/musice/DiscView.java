package com.sincerity.customview.musice;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.RelativeLayout;

/**
 * Created by Sincerity on 2019/12/19.
 * 描述：
 */
public class DiscView extends RelativeLayout {
    public DiscView(Context context) {
        this(context,null);
    }

    public DiscView(Context context, AttributeSet attrs) {
        this(context, attrs,0);
    }

    public DiscView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }
}
