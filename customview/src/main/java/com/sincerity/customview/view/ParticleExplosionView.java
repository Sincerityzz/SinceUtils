package com.sincerity.customview.view;

import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.LinearInterpolator;

import androidx.annotation.Nullable;

import com.sincerity.customview.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Sincerity on 2019/12/12.
 * 描述：离子爆炸效果
 */
public class ParticleExplosionView extends View {
    private Bitmap mBitmap;
    private Paint mPaint;
    private float d = 3;
    private List<Ball> mBalls = new ArrayList<>();
    private ValueAnimator mAnimal;

    public ParticleExplosionView(Context context) {
        this(context, null);
    }

    public ParticleExplosionView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ParticleExplosionView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        mPaint = new Paint();
        mBitmap = BitmapFactory.decodeResource(getResources(), R.mipmap.back);
        for (int i = 0; i < mBitmap.getWidth(); i++) {
            for (int j = 0; j < mBitmap.getHeight(); j++) {
                Ball ball = new Ball();
                ball.color = mBitmap.getPixel(i, j);
                ball.x = i * d + d / 2;
                ball.y = j * d + d / 2;
                ball.r = d / 2;
                ball.vX = (float) (Math.pow(-1, Math.ceil(Math.random() * 1000)) * 20 * Math.random());
                ball.vY = rangInt(-15, 35);
                ball.aX = 0;
                ball.aY = 0.98F;
                mBalls.add(ball);
            }
        }
        mAnimal = ValueAnimator.ofFloat(0, 1);
        mAnimal.setRepeatCount(-1);
        mAnimal.setDuration(2000);
        mAnimal.setInterpolator(new LinearInterpolator());
        mAnimal.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {

            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                updateBall();
                invalidate();
            }
        });
    }

    private void updateBall() {
        for (Ball mBall : mBalls) {
            mBall.x += mBall.vX;
            mBall.y += mBall.vY;
            mBall.vX += mBall.aX;
            mBall.vY += mBall.aY;
        }
    }

    private float rangInt(int i, int j) {
        int max = Math.max(i, j);
        int min = Math.min(i, j) - 1;
        return (float) (min + Math.ceil(Math.random() * (max - min)));
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.translate(500, 500);
        for (Ball mBall : mBalls) {
            mPaint.setColor(mBall.color);
            canvas.drawCircle(mBall.x, mBall.y, mBall.r, mPaint);
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            mAnimal.start();
        }
        return super.onTouchEvent(event);
    }
}
