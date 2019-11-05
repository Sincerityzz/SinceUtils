package com.sincerity.sinceutils;

import android.Manifest;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatMultiAutoCompleteTextView;

import com.sincerity.utilslibrary.dialog.SinceDialog;
import com.sincerity.utilslibrary.ExceptionCrashHandler;
import com.sincerity.utilslibrary.bugsince.FixDexManager;
import com.sincerity.utilslibrary.utils.PermissionsUtils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
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
        PermissionsUtils.getInstance().chekPermissions(this, permissions, permissionsResult);
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
                    int len = 0;
                    while ((len = reader.read(buffer)) != -1) {
                        String str = new String(buffer, 0, len);
                        Log.e("admin", str);
                    }
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        @Override
        public void forbitPermissions() { //请求权限
            Log.e("TAG", "没有权限");
            Toast.makeText(LoginActivity.this, "权限不通过!即将关闭", Toast.LENGTH_SHORT).show();
            new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        Thread.sleep(2000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    finish();
                }
            }).start();
        }
    };

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        // 就多一个参数this
        PermissionsUtils.getInstance().onRequestPermissionsResult(this, requestCode, permissions, grantResults);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PermissionsUtils.mRequestCode) {
            PermissionsUtils.getInstance().chekPermissions(this, permissions, permissionsResult);
        }
    }

    public void hotFix(View view) {
        Toast.makeText(this, "测试bug修复" + 2 / 2, Toast.LENGTH_SHORT).show();
//        startActivity(new Intent(this, MainActivity.class));
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
        final SinceDialog dialog = new SinceDialog.Builder(this)
                .setContentView(R.layout.dialog)
                .setText(R.id.cancel, "再想想")
                .fromBottom(true)
                .fullWindow()
                .show();
        final AppCompatMultiAutoCompleteTextView textView = dialog.getView(R.id.content);
        dialog.openKeyBoard(textView);
        dialog.setOnClickListener(R.id.ok, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(LoginActivity.this, textView.getText().toString(), Toast.LENGTH_SHORT).show();
            }
        });
        dialog.setOnClickListener(R.id.cancel, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.closeKeyBoard(textView);
                dialog.dismiss();
                Toast.makeText(LoginActivity.this, "关闭", Toast.LENGTH_SHORT).show();
            }
        });
        //弹出键盘

    }
}
