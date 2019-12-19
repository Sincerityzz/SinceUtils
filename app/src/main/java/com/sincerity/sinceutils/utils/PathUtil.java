package com.sincerity.sinceutils.utils;

/**
 * Created by Sincerity on 2019/12/6.
 */
public class PathUtil {

    /**
     * @param oldApkPath:
     * @param newApkPath
     * @param pathPath
     */
    public static native void combine(String oldApkPath, String newApkPath, String pathPath);

}
