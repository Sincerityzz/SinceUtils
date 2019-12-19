package com.sincerity.customview.view;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PathMeasure;
import android.util.AttributeSet;
import android.view.View;

import androidx.annotation.Nullable;

import com.sincerity.customview.R;


/**
 * Created by Sincerity on 2019/12/18.
 * 描述：
 */
public class CarView extends View {
    private Path mPath;
    private Bitmap mBitmap;
    private PathMeasure pathMeasure;//路径计算
    private float distanceRatio = 0;//
    private Paint mPaint;
    private Paint mCarPaint;
    private Matrix CarMatrix;

    public CarView(Context context) {
        this(context, null);
    }

    public CarView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CarView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        mBitmap = BitmapFactory.decodeResource(getResources(), R.mipmap.car);
        mPath = new Path();
        mPath.addCircle(0, 0, 200, Path.Direction.CW);
        pathMeasure = new PathMeasure(mPath, false);
        mPaint = new Paint();
        mPaint.setStrokeWidth(5);
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setAntiAlias(true);
        mPaint.setColor(Color.BLACK);

        mCarPaint = new Paint();
        mCarPaint.setColor(Color.DKGRAY);
        mCarPaint.setStrokeWidth(2);
        mCarPaint.setStyle(Paint.Style.STROKE);
        CarMatrix = new Matrix();

    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.drawColor(Color.WHITE);
        int centerX = getWidth() ;
        int centerY = getHeight();
        canvas.translate(centerX/2, centerY/2);
        CarMatrix.reset();
        distanceRatio += 0.006f;
        if (distanceRatio >= 1) {
            distanceRatio = 0;
        }
        float[] pos = new float[2];
        float[] tan = new float[2];
        float distance = pathMeasure.getLength() * distanceRatio;
        pathMeasure.getPosTan(distance, pos, tan);
        float degree = (float) (Math.atan2(tan[1], tan[0]) * 180 / Math.PI);
        CarMatrix.postRotate(degree, mBitmap.getWidth() / 2, mBitmap.getHeight() / 2);
        CarMatrix.postTranslate(pos[0] - (mBitmap.getWidth() /2), pos[1] - mBitmap.getHeight() /2);
        canvas.drawPath(mPath, mPaint);
        canvas.drawBitmap(mBitmap, CarMatrix, mCarPaint);
        invalidate();
    }
}
