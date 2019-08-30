package com.sincerity.utilslibrary.view.BannerView;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.View;

/**
 * Created by Sincerity on 2019/8/30.
 * 描述：
 */
public class DotIndicatorView extends View {
    private Drawable drawable;

    public DotIndicatorView(Context context) {
        this(context, null);
    }

    public DotIndicatorView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public DotIndicatorView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    //给自定义页面添加图片
    public void setDrawable(Drawable drawable) {
        this.drawable = drawable;
        invalidate(); //重新绘制页面 调用OnDraw
    }

    public Drawable getDrawable() {
        return drawable;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        if (drawable != null) {
            //如果动态设置图片必须要设置SetBounds,不然图片会不显示
//            drawable.setBounds(0, 0, getMeasuredWidth(), getMeasuredHeight());
//            drawable.draw(canvas);
            //画圆 先把Drawable变成BitMap
            Bitmap bitmap = DrawableToBitmap(drawable);
            //把bitmap变为圆形
            Bitmap circleBitmap = BitmapToCircleBitMap(bitmap);
            //绘制圆形bitmap
            canvas.drawBitmap(circleBitmap, 0, 0, null);

        }
    }

    /**
     * @param bitmap 需要转为圆形的bitmap
     * @return 圆形的bitmap
     */
    private Bitmap BitmapToCircleBitMap(Bitmap bitmap) {
        Bitmap circleBitmap = Bitmap.createBitmap(getMeasuredWidth(), getMeasuredHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(circleBitmap);
        Paint paint = new Paint();
        paint.setAntiAlias(true);//抗锯齿
        paint.setFilterBitmap(true);
        paint.setDither(true); //防抖动
        canvas.drawCircle(getMeasuredHeight() / 2, getMeasuredHeight() / 2, getMeasuredWidth() / 2, paint);
        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN)); //取bitmap和圆形的交集
        canvas.drawBitmap(bitmap, 0, 0, paint);
        bitmap.recycle(); //回收BitMap
        bitmap = null;
        return circleBitmap;
    }

    /**
     * @param drawable 需要转化的Drawable
     * @return 转化后的bitmap
     */
    private Bitmap DrawableToBitmap(Drawable drawable) {
        //若果是bitmapDrawable类型直接强转
        if (drawable instanceof BitmapDrawable) {
            return ((BitmapDrawable) drawable).getBitmap();
        }
        Bitmap bitmap = Bitmap.createBitmap(getMeasuredWidth(), getMeasuredHeight(), Bitmap.Config.ARGB_8888);
        //创建一个画布
        Canvas canvas = new Canvas(bitmap);
        //把Bitmap画到画布上
        drawable.setBounds(0, 0, getMeasuredWidth(), getMeasuredHeight());
        drawable.draw(canvas);
        return bitmap;
    }
}
