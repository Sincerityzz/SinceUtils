package com.sincerity.customview.svg;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.RectF;
import android.graphics.Region;

/**
 * Created by Sincerity on 2019/12/19.
 * 描述：
 */
public class ProvinceItem {
    ProvinceItem(Path path) {
        this.path = path;
    }

    private Path path;
    private int color;

    public Path getPath() {
        return path;
    }

    public void setPath(Path path) {
        this.path = path;
    }

    public int getColor() {
        return color;
    }

    public void setColor(int color) {
        this.color = color;
    }

    /**
     * @param canvas 画布
     * @param mPaint 画笔
     * @param b      是否选中
     */
    void drawItem(Canvas canvas, Paint mPaint, boolean b) {
        if (b) {
            //选中描边
            mPaint.clearShadowLayer();
            mPaint.setStrokeWidth(1);
            mPaint.setStyle(Paint.Style.FILL);
            mPaint.setColor(color);
            //选中后里面的效果
            mPaint.setStyle(Paint.Style.STROKE);
            int strokeColor = 0xFFD0E8F4;
            mPaint.setColor(strokeColor);
            canvas.drawPath(path, mPaint);
        } else {
            mPaint.setStrokeWidth(2);
            mPaint.setColor(Color.BLACK);
            mPaint.setStyle(Paint.Style.FILL);
            mPaint.setShadowLayer(8, 0, 0, 0xffffff);
            canvas.drawPath(path, mPaint);
            //边界
            mPaint.clearShadowLayer();
            mPaint.setColor(color);
            mPaint.setStyle(Paint.Style.FILL);
            mPaint.setStrokeWidth(2);
            canvas.drawPath(path, mPaint);
        }
    }

    boolean isTouch(float x, float y) {
        RectF rectF = new RectF();
        path.computeBounds(rectF, true);
        Region region = new Region();//不规则的连续区域 path可以转Region
        region.setPath(path, new Region((int) rectF.left, (int) rectF.top, (int) rectF.right, (int) rectF.bottom));
        return region.contains((int) x, (int) y);
    }
}
