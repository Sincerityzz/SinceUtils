package com.sincerity.customview.svg;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.RectF;
import android.os.Handler;
import android.os.Looper;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.core.graphics.PathParser;

import com.sincerity.customview.R;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

/**
 * Created by Sincerity on 2019/12/19.
 * 描述：SVG中国地图
 */
public class MapView extends View {
    private List<ProvinceItem> list;
    private Context context;
    private int[] colorArray = new int[]{0xFF239BD7, 0xFF30A9E5, 0xFF80CBF1, 0xFFFFFFFF};
    private Paint mPaint;
    private ProvinceItem select;
    private RectF totalRectF;
    private float scale = 1f; //默认缩放系数

    public MapView(Context context) {
        this(context, null);
    }

    public MapView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public MapView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context context) {
        this.context = context;
        thread.start();
        mPaint = new Paint();
        mPaint.setAntiAlias(true);
        list = new ArrayList<>();
    }

    private Thread thread = new Thread() {
        @Override
        public void run() {
            InputStream stream = context.getResources().openRawResource(R.raw.china);
            DocumentBuilderFactory facotory = DocumentBuilderFactory.newInstance();
            try {
                DocumentBuilder builder = facotory.newDocumentBuilder();
                Document doc = builder.parse(stream);
                Element rootElement = doc.getDocumentElement();
                NodeList items = rootElement.getElementsByTagName("path");
                float left = -1, right = -1, top = -1, bottom = -1;
                List<ProvinceItem> listItem = new ArrayList<>();
                for (int i = 0; i < items.getLength(); i++) {
                    Element element = (Element) items.item(i);
                    String pathData = element.getAttribute("android:pathData");
                    Path path = PathParser.createPathFromPathData(pathData);
                    ProvinceItem item = new ProvinceItem(path);
                    item.setColor(colorArray[i % 4]);
                    RectF rectF = new RectF();
                    path.computeBounds(rectF, true);
                    left = left == -1 ? rectF.left : Math.min(left, rectF.left);
                    right = right == -1 ? rectF.right : Math.max(right, rectF.right);
                    top = top == -1 ? rectF.top : Math.min(top, rectF.top);
                    bottom = bottom == -1 ? rectF.bottom : Math.max(bottom, rectF.bottom);
                    listItem.add(item);
                }
                list = listItem;
                totalRectF = new RectF(left, top, right, bottom);
                //刷新
                Handler handler = new Handler(Looper.getMainLooper());
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        requestLayout();
                        invalidate();
                    }
                });
            } catch (ParserConfigurationException | IOException | SAXException e) {
                e.printStackTrace();
            }
        }
    };

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (list != null) {
            canvas.save();
            canvas.scale(scale, scale);
            for (ProvinceItem provinceItem : list) {
                if (provinceItem != select) {
                    provinceItem.drawItem(canvas, mPaint, false);
                } else {
                    select.drawItem(canvas, mPaint, true);
                }

            }
        }
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int width = MeasureSpec.getSize(widthMeasureSpec);
        int height = MeasureSpec.getSize(heightMeasureSpec);
        if (totalRectF != null) {
            float mapWidth = totalRectF.width();
            scale = width / mapWidth;
        }
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        handlerTouch((event.getX() / scale), (event.getY() / scale));
        return super.onTouchEvent(event);
    }

    private void handlerTouch(float x, float y) {
        if (list == null) return;
        ProvinceItem selectItem = null;
        for (ProvinceItem provinceItem : list) {
            if (provinceItem.isTouch(x, y)) {
                selectItem = provinceItem;
            }
        }
        if (selectItem != null) {
            select = selectItem;
            postInvalidate();
        }
    }
}
