package com.sincerity.utilslibrary.utils;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.widget.Toast;

import com.sincerity.utilslibrary.BuildConfig;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * Created by Sincerity on 2019/10/22.
 * 描述：
 */
public class UpdateManager {

    /**
     * 空
     */
    private final static int MSG_EMPTY = -1;

    /**
     * 更新消息
     */
    private final static int MSG_UPDATE = 0;

    /**
     * 正在下载消息
     */
    private final static int MSG_DOWNLOADING = 1;

    /**
     * 下载完成消息
     */
    private final static int MSG_DOWNLOADED = 2;

    /**
     * 记录进度值
     */
    private int progress = 0;

    private String downUrl;
    private String apkname;
    private Context context;
    private ProgressDialog mProgressDialog;
    private boolean isClick = true;

    public UpdateManager(Context context, boolean isClick) {
        super();
        this.context = context;
        this.isClick = isClick;
    }

    @SuppressLint("HandlerLeak")
    private Handler mHandler = new Handler() {

        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case MSG_UPDATE:
                    if (BuildConfig.DEBUG) {
                        doNewVersionUpdate();
                        return;
                    }
                    if (msg.arg1 > msg.arg2) {
                        if (!Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
                            Toast.makeText(context, "SD卡不存在。", Toast.LENGTH_SHORT).show();
                        } else {
                            doNewVersionUpdate();
                        }
                    } else {
                        notNewVersionShow();
                    }
                    break;
                case MSG_DOWNLOADING:
                    mProgressDialog.setProgress(progress);
                    break;
                case MSG_DOWNLOADED:
                    downFinish();
                    break;
                case MSG_EMPTY:
                    Toast.makeText(context, "无法建立到 :83 服务器的连接。", Toast.LENGTH_SHORT).show();
                    break;
            }
            super.handleMessage(msg);
        }
    };

    /**
     * 比较版本决定是否更新
     */
    public void checkUpdateInfo() {
        new Thread() {

            @Override
            public void run() {
                String buffer = HttpClientUtil.doGet(context, "http://218.16.98.103:83/iphone/DataAcquisition.json");
                if (buffer == null) {
                    mHandler.sendEmptyMessage(MSG_EMPTY);
                    return;
                }

                JSONObject json = null;
                try {
                    json = new JSONObject(buffer);
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                if (json == null) {
                    mHandler.sendEmptyMessage(MSG_EMPTY);
                    return;
                }

                int newVerCode = -1;
                try {
                    newVerCode = json.getInt("verCode");
                    if (BuildConfig.DEBUG) {
                        downUrl = "http://218.16.98.103:83/iphone/DataAcquisition_10.apk";
                    }
                    apkname = json.getString("apkname");
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                int vercode = ApkUtil.getVerCode(context);
                Message msg = new Message();
                msg.what = MSG_UPDATE;
                msg.arg1 = newVerCode;
                msg.arg2 = vercode;
                mHandler.sendMessage(msg);
            }
        }.start();
    }

    /**
     * 无更新关闭对话框
     */
    private void notNewVersionShow() {
        if (!isClick) {
            return;
        }

        new AlertDialog.Builder(context).setTitle("更新").setMessage("更新了")
                .setPositiveButton("取消", new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                }).create().show();
    }

    /**
     * 有新版本弹出选择下载更新
     */
    private void doNewVersionUpdate() {
        new AlertDialog.Builder(context).setTitle("更新").setMessage("更关心")
                .setPositiveButton("确定", new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        mProgressDialog = new ProgressDialog(context);
                        mProgressDialog.setTitle("进度");
                        mProgressDialog.setMessage("123");
                        mProgressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
                        mProgressDialog.setMax(100);
                        downFile();
                    }
                }).setNegativeButton("取消", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int whichButton) {
                dialog.dismiss();
            }
        }).create().show();
    }

    /**
     * 下载文件
     *
     */
    void downFile() {
        mProgressDialog.show();
        new Thread() {

            @Override
            public void run() {
                HttpClient httpClient = new DefaultHttpClient();
                HttpGet request = new HttpGet(downUrl);
                HttpResponse response;
                try {
                    response = httpClient.execute(request);
                    HttpEntity entity = response.getEntity();
                    long length = entity.getContentLength();
                    InputStream is = entity.getContent();
                    FileOutputStream fileOutputStream = null;
                    if (is != null) {
                        File file = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS), apkname);
                        Log.e("admin------", file.getAbsolutePath());
                        fileOutputStream = new FileOutputStream(file);
                        byte[] buf = new byte[1024];
                        int ch = -1;
                        int count = 0;
                        while ((ch = is.read(buf)) != -1) {
                            count += ch;
                            progress = (int) (count * 100 / length);
                            mHandler.sendEmptyMessage(MSG_DOWNLOADING);
                            if (ch <= 0 || count >= length) {
                                mHandler.sendEmptyMessage(MSG_DOWNLOADED);
                            }
                            fileOutputStream.write(buf, 0, ch);
                        }
                        fileOutputStream.flush();
                        if (fileOutputStream != null) {
                            fileOutputStream.close();
                        }
                        is.close();
                    }
                } catch (ClientProtocolException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }.start();
    }

    /**
     * 下载完成关闭进度条并启动安装
     */
    void downFinish() {
        mHandler.post(new Runnable() {

            @Override
            public void run() {
                mProgressDialog.cancel();
                ApkUtil.installApk(context, new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS), apkname));
//                ApkUtil.installApk(context, Uri.fromFile(new File(Environment.getExternalStorageDirectory(), apkname)));
            }
        });
    }
}
