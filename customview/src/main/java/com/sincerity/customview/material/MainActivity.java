package com.sincerity.customview.material;

import android.os.Bundle;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.sincerity.customview.R;

public class MainActivity extends AppCompatActivity {
    private BottomNavigationView mBottomView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main1);
        mBottomView = findViewById(R.id.bottom_view);
        mBottomView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.main:
                        showToast("首页");
                        return true;
                    case R.id.main2:
                        showToast("发现");
                        return true;
                    case R.id.main3:
                        showToast("生活");
                        return true;
                    case R.id.main4:
                        showToast("汇总");
                        return true;
                }
                return false;
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.toolbar_menu, menu);
        return true;
    }

    @Override
    public void onOptionsMenuClosed(Menu menu) {
        super.onOptionsMenuClosed(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.add:
                showToast("add");
                break;
            case R.id.add2:
                showToast("add2");
                break;
            case R.id.add3:
                showToast("add3");
                break;
            case R.id.add4:
                showToast("add4");
                break;
            case R.id.add5:
                showToast("add5");
                break;
        }
        return true;
    }

    private void showToast(String str) {
        Toast toast = Toast.makeText(this, str, Toast.LENGTH_SHORT);
        toast.setGravity(Gravity.CENTER, 0, 0);
        toast.show();
    }
}
