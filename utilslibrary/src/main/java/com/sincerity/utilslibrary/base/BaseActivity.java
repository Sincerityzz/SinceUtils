package com.sincerity.utilslibrary.base;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

/**
 * Created by Sincerity on 2019/9/3.
 * 描述：模板模式构建一个BaseActivity 构建BaseActivity时候注意要多留一层可以拓展的Activity.
 * 用来做各种插件的编写
 */
public abstract class BaseActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(setContentView());
        initActionBar();
        initViews();
        initListener();
        initData();
    }

    /**
     * 设置布局
     *
     * @return 布局文件
     */
    protected abstract int setContentView();

    /**
     * 初始化头部
     */
    protected abstract void initActionBar();

    /**
     * 初始化views
     */
    protected abstract void initViews();

    /**
     * 初始化监听
     */
    protected abstract void initListener();

    /**
     * 初始化数据
     */
    protected abstract void initData();

    /**
     * 启动Activity
     *
     * @param clazz 需要启动的Activity
     */
    protected void startActivity(Class<?> clazz) {
        Intent intent = new Intent(this, clazz);
        startActivity(intent);
    }

    /**
     * findById
     *
     * @param viewId   View 的ID
     * @param <T>强转的类型
     * @return 转换后的View
     */
    private <T extends View> T viewById(int viewId) {
        return (T) findViewById(viewId);
    }
}
