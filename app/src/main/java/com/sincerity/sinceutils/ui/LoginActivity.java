package com.sincerity.sinceutils.ui;

import android.Manifest;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.sincerity.framelibrary.base.HttpCallBack;
import com.sincerity.framelibrary.defalut.DefaultNavigationBar;
import com.sincerity.framelibrary.http.OkHttpEngine;
import com.sincerity.sinceutils.R;
import com.sincerity.sinceutils.bean.ImageBean;
import com.sincerity.utilslibrary.bug.FixDexManager;
import com.sincerity.utilslibrary.exception.ExceptionCrashHandler;
import com.sincerity.utilslibrary.httputils.HttpUtils;
import com.sincerity.utilslibrary.utils.PermissionsUtils;

import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;

public class LoginActivity extends AppCompatActivity {
    public static String[] permissions = new String[]{
            Manifest.permission.WRITE_EXTERNAL_STORAGE, //读写
            Manifest.permission.READ_EXTERNAL_STORAGE
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        new DefaultNavigationBar.Builder(this)
                .setRightText("相册").builder();

        PermissionsUtils.getInstance().checkPermissions(this, permissions, permissionsResult);
    }

    PermissionsUtils.IPermissionsResult permissionsResult = new PermissionsUtils.IPermissionsResult() {
        @Override
        public void passPermissions() {
//            UpdateManager updateManager = new UpdateManager(LoginActivity.this, false);
//            updateManager.checkUpdateInfo();
            Log.e("TAG", "权限通过");
            fixDexBug();
            //获取上次异常的信息 上传到服务器
//            getExceptionFile();
//            File file = new File(Environment.getExternalStorageDirectory(), "fix.aptch");
//            if (file.exists()) {
//                //开始修复
//
//            }
        }

        private void getExceptionFile() {
            //获取上次异常的信息 上传到服务器
            File crashFile = ExceptionCrashHandler.getInstance().getCrashFile();
            if (crashFile.exists()) {
                //上传到服务器
                try {
                    InputStreamReader reader = new InputStreamReader(new FileInputStream(crashFile));
                    char[] buffer = new char[1024];
                    int len;
                    while ((len = reader.read(buffer)) != -1) {
                        String str = new String(buffer, 0, len);
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        @Override
        public void forbidPermissions() { //请求权限
            Log.e("TAG", "没有权限");
            Toast.makeText(LoginActivity.this, "权限不通过!即将关闭", Toast.LENGTH_SHORT).show();
            new Thread(() -> {
                try {
                    Thread.sleep(2000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                finish();
            }).start();
        }
    };

    @Override
    public void onRequestPermissionsResult(int requestCode, @NotNull String[] permissions, @NotNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        // 就多一个参数this
        PermissionsUtils.getInstance().onRequestPermissionsResult(this, requestCode, grantResults);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PermissionsUtils.mRequestCode) {
            PermissionsUtils.getInstance().checkPermissions(this, permissions, permissionsResult);
        }
    }

    public void hotFix(View view) {
//        try {
//            IDaoSupport<Person> daoSupport = DaoSupportFactory.INSTANCE.getInstance().getDaoSupport(Person.class);
//            List<Person> list = new ArrayList<>();
//            for (int i = 0; i < 8000; i++) {
//                list.add(new Person("张珊" + i, 22 + i));
//            }
        //没有优化之前8000条数据所需时间 322 298
        //优化之后8000条数据所需时间 300 270 267
//        daoSupport.insert(list);
//            List<Person> personList = daoSupport.query("age=?", new String[]{"23"});
//            for (Person person : personList) {
//                Log.e("admin", "条件查询" + person.toString());
//            }
//            Person person = daoSupport.queryById(11);
//            Log.e("admin", "ID查询" + person.toString());
//
//            int update = daoSupport.update(new Person("张珊99", 121), "age=?", new String[]{"222"});
//            Log.e("admin", update + "更新结果");
//        daoSupport.insert(new Person("张三", 18));
//        Toast.makeText(this, "测试bug修复" + 1, Toast.LENGTH_SHORT).show();
//        startActivity(new Intent(this, MainActivity.class));
//            List<Person> list1 = daoSupport.query("id<=?", new String[]{"200"});
//            Log.e("admin", "查询前200条数据" + list1.size());
//            for (Person person1 : list1) {
//                Log.e("admin", person1.toString());
//            }
//        } catch (SQLException e) {
//            e.printStackTrace();
//        }
        Toast.makeText(this, "1223", Toast.LENGTH_SHORT).show();

        HttpUtils.Companion.with(this)
                .exchangeEngine(new OkHttpEngine())
                .url("https://www.wanandroid.com/banner/json")
                .addParams()
                .cache(true)
                .execute(new HttpCallBack<ImageBean>() {
                    @Override
                    public void onFail(@NotNull Exception exception) {
                        Log.e("admin", "失败" + exception.getMessage());
                    }

                    @Override
                    public void onSuccess(ImageBean result) {
                        Log.e("admin", "成功" + result.getData().toString());
                    }
                });
    }

    private void fixDexBug() {
        File fixFile = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS), "fix.dex");
        if (fixFile.exists()) {
            FixDexManager manager = new FixDexManager(this);
            try {
                manager.fixDex(fixFile.getAbsolutePath());
                Toast.makeText(this, "修复成功", Toast.LENGTH_SHORT).show();
            } catch (Exception e) {
                e.printStackTrace();
                Toast.makeText(this, "修复失败", Toast.LENGTH_SHORT).show();
            }
        }
    }

    public void dialogTest(View view) {

        //弹出键盘
    }
}
