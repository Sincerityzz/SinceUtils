package com.sincerity.customview.musice;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.LayerDrawable;
import android.os.Build;
import android.util.AttributeSet;
import android.view.animation.AccelerateInterpolator;
import android.widget.RelativeLayout;

import androidx.annotation.RequiresApi;

import com.sincerity.customview.R;

/**
 * Created by Sincerity on 2019/12/19.
 * 描述：
 */
@RequiresApi(api = Build.VERSION_CODES.M)
public class BackGroundAnimationRelationLayout extends RelativeLayout {
    private LayerDrawable layerDrawable; //图层
    private ObjectAnimator objectAnimator;

    public BackGroundAnimationRelationLayout(Context context) {
        super(context);
    }

    public BackGroundAnimationRelationLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }


    public BackGroundAnimationRelationLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        Drawable drawable = getContext().getDrawable(R.mipmap.ic_blackground);
        Drawable[] drawables = new Drawable[2];
        drawables[0] = drawable;
        drawables[1] = drawable;
        layerDrawable = new LayerDrawable(drawables);
        objectAnimator = ObjectAnimator.ofFloat(this, "number", 0f, 1f);
        objectAnimator.setDuration(500);
        objectAnimator.setInterpolator(new AccelerateInterpolator());
        objectAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                //    VSYNC-start %duration/duration
                int foreground = (int) ((float) animation.getAnimatedValue() * 255);
                layerDrawable.getDrawable(1).setAlpha(foreground);
                setBackground(layerDrawable);
            }
        });
        objectAnimator.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {

                layerDrawable.setDrawable(0, layerDrawable.getDrawable(1));
            }

        });
    }

    public void setForeground(Drawable drawable) {
        layerDrawable.setDrawable(1, drawable);
        objectAnimator.start();
    }
}
