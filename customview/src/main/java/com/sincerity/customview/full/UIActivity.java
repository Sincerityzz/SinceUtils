package com.sincerity.customview.full;

import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.sincerity.customview.R;
import com.sincerity.utilslibrary.utils.UIManager;
import com.sincerity.utilslibrary.utils.ViewScaleUtils;

//屏幕上适配方案
public class UIActivity extends AppCompatActivity {
    private TextView textView1, textView2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        UIManager.getInstance(this.getApplicationContext());
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ui);
        textView1 = findViewById(R.id.tv3);
        textView2 = findViewById(R.id.tv4);
        ViewScaleUtils.setViewLayoutPrams(textView1, 540, 100, 0, 0, 0, 0);
        ViewScaleUtils.setViewLayoutPrams(textView2, 1080, 100, 0, 0, 0, 0);
    }
}
