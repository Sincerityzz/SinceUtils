package com.sincerity.utilslibrary.utils;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.util.Log;

import androidx.core.content.FileProvider;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStreamReader;

/**
 * Created by Sincerity on 2019/10/22.
 * 描述：
 */
public class ApkUtil {

    /**
     * 获得app名称
     *
     * @param context
     * @return
     */
    public static String getAppName(Context context) {
        return context.getPackageManager().getApplicationLabel(context.getApplicationInfo()).toString();
    }

    /**
     * 获得当前版本号
     *
     * @param context
     * @return
     */
    public static int getVerCode(Context context) {
        int verCode = -1;
        try {
            verCode = context.getPackageManager().getPackageInfo(context.getPackageName(), 0).versionCode;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return verCode;
    }

    /**
     * 获得版本名称
     *
     * @param context
     * @return
     */
    public static String getVerName(Context context) {
        String verName = "";
        try {
            verName = context.getPackageManager().getPackageInfo(context.getPackageName(), 0).versionName;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return verName;
    }

    /**
     * 获取服务器上的配置文件字符串
     *
     * @param serverPath
     * @return
     * @throws Exception
     */
    public static String getUpdateVerJson(String serverPath) throws Exception {
        StringBuilder sb = new StringBuilder();
        HttpClient client = new DefaultHttpClient();
        HttpParams httpParams = client.getParams();
        HttpConnectionParams.setConnectionTimeout(httpParams, 3000);
        HttpConnectionParams.setSoTimeout(httpParams, 5000);
        HttpResponse response = (HttpResponse) client.execute(new HttpGet(serverPath));
        HttpEntity entity = response.getEntity();
        if (null != entity) {
            BufferedReader reader = new BufferedReader(new InputStreamReader(entity.getContent(), "UTF-8"), 8192);
            String line = null;
            while ((line = reader.readLine()) != null) {
                sb.append(line + "\n");
            }
            reader.close();
        }
        return sb.toString();
    }

    public static void installApk(Context context, File apkFile) {
        Log.e("admin", apkFile.getAbsolutePath());
        Intent installApkIntent = new Intent();
        installApkIntent.setAction(Intent.ACTION_VIEW);
        installApkIntent.addCategory(Intent.CATEGORY_DEFAULT);
        installApkIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.M) {
            installApkIntent.setDataAndType(FileProvider.getUriForFile(context, "com.sincerity.sinceutils.fileprovider", apkFile), "application/vnd.android.package-archive");
            installApkIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        } else {
            installApkIntent.setDataAndType(Uri.fromFile(apkFile), "application/vnd.android.package-archive");
        }
        if (context.getPackageManager().queryIntentActivities(installApkIntent, 0).size() > 0) {
            context.startActivity(installApkIntent);
        }
    }

    /**
     * 安装apk
     *
     * @param context
     * @param uri
     */
    public static void installApk(Context context, Uri uri) {
        Uri downloadFileUri;
        Intent intent = new Intent(Intent.ACTION_VIEW);
        File file = new File(context.getFilesDir() + "DataAcquisition.apk");
        if (file != null) {
            String path = file.getAbsolutePath();
            Log.e("admin", path);
//            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//            intent.setDataAndType(uri, "application/vnd.android.package-archive");
            downloadFileUri = Uri.parse("file://" + path);
            intent.setDataAndType(downloadFileUri, "application/vnd.android.package-archive");
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(intent);
        }

//        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//        intent.setDataAndType(uri, "application/vnd.android.package-archive");
//        intent.setDataAndType(Uri.parse("file://" + filepath), "application/vnd.android.package-archive");
//        intent.setDataAndType(Uri.parse("file://" + args[0].getAsString()), "application/vnd.android.package-archive");
//        context.startActivity(intent);
    }
//    public static void installApk(Context context, Uri uri,String filepath) {
//        Intent intent = new Intent(Intent.ACTION_VIEW);
//        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
////		intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);//20160615add
//        intent.setDataAndType(Uri.parse("file://"+ filepath),"application/vnd.android.package-archive");
////        intent.setDataAndType(uri, "application/vnd.android.package-archive");
//        context.startActivity(intent);
//    }
}
