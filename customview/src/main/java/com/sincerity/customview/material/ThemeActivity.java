package com.sincerity.customview.material;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.sincerity.customview.R;
import com.sincerity.utilslibrary.utils.ImmersiveUtils;

public class ThemeActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_theme);
        ImmersiveUtils.getInstance().with(this).setTargetView(findViewById(R.id.toolbar));
    }
}
