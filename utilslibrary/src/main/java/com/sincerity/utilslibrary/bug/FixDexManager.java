package com.sincerity.utilslibrary.bug;

import android.content.Context;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

import dalvik.system.BaseDexClassLoader;

/**
 * Created by Sincerity on 2019/10/24.
 * 描述：底层替换热修复的关键整理以及详细步骤
 * 1. 获取运行的DexElement
 * 2. 移动补丁到系统可以访问的目录下APP/data下.
 * 3. 得到补丁的ClassLoader,在获取DexElement
 * 4. 合并运行的DexElement数组和补丁的DexElement数组 并且把补丁的 DexElement放到合并的数组的首位
 * 5. 注入到原来运行的类中即可完成底层替换的热修复
 */
public class FixDexManager {
    private Context mContext;
    private File mDexDir;

    public FixDexManager(Context context) {
        this.mContext = context;
        this.mDexDir = context.getDir("odex", Context.MODE_PRIVATE);
    }

    /**
     * 修复Dex包
     *
     * @param fileDexPath dex存在得目录
     */
    public void fixDex(String fileDexPath) throws Exception {
        //第二获取下载好的补丁的DexElement
        File srcFile = new File(fileDexPath);
        if (!srcFile.exists()) {
            throw new FileNotFoundException(fileDexPath);
        }
        File targetFile = new File(mDexDir, srcFile.getName());
        if (targetFile.exists()) {
            return;
        }
        //复制到系统可以访问的目录之中. 应用目录的
        copyFileUsingFileChannels(srcFile, targetFile);
        //移动到系统能访问到的目录下, 最终变为ClassLoader
        //ClassLoader读取
        List<File> fixDexFiles = new ArrayList<>();
        fixDexFiles.add(targetFile);
        fixDexFiles(fixDexFiles);
    }

    /**
     * 注入到ClassLoader中
     *
     * @param classLoader classLoader
     * @param dexElement  需要注入的数组
     */
    private void injectDexElement(ClassLoader classLoader, Object dexElement) throws Exception {

        Class<?> classLoaderClass = Class.forName("dalvik.system.BaseDexClassLoader");
        Field pathListField = classLoaderClass.getDeclaredField("pathList");
        pathListField.setAccessible(true);
        Object pathList = pathListField.get(classLoader);

        Class<?> pathListClass = pathList.getClass();
        Field dexElementsField = pathListClass.getDeclaredField("dexElements");
        dexElementsField.setAccessible(true);
        dexElementsField.set(pathList, dexElement);
    }

    /**
     * 合并两个dexElements数组
     *
     * @param arrayLhs
     * @param arrayRhs
     * @return
     */
    private static Object combineArray(Object arrayLhs, Object arrayRhs) {
        Class<?> localClass = arrayLhs.getClass().getComponentType();
        int i = Array.getLength(arrayLhs);
        int j = i + Array.getLength(arrayRhs);
        Object result = Array.newInstance(localClass, j);
        for (int k = 0; k < j; ++k) {
            if (k < i) {
                Array.set(result, k, Array.get(arrayLhs, k));
            } else {
                Array.set(result, k, Array.get(arrayRhs, k - i));
            }
        }
        return result;
    }

    /**
     * 从classLoader中反射获取DexElement
     *
     * @param classLoader 类加载器
     * @return DexElement
     */
    private Object getDexElementByClassLoader(ClassLoader classLoader) throws Exception {
        Class<?> classLoaderClass = Class.forName("dalvik.system.BaseDexClassLoader");
        Field pathListField = classLoaderClass.getDeclaredField("pathList");
        pathListField.setAccessible(true);
        Object pathList = pathListField.get(classLoader);

        Class<?> pathListClass = pathList.getClass();
        Field dexElementsField = pathListClass.getDeclaredField("dexElements");
        dexElementsField.setAccessible(true);
        Object dexElements = dexElementsField.get(pathList);
        return dexElements;
    }

    private static void copyFileUsingFileChannels(File source, File dest) throws IOException {
        try (InputStream in = new FileInputStream(source)) {
            try (OutputStream out = new FileOutputStream(dest)) {
                // Transfer bytes from in to out
                byte[] buf = new byte[1024];
                int len;
                while ((len = in.read(buf)) > 0) {
                    out.write(buf, 0, len);
                }
            }
        }
    }

    /**
     * 加载全部的修复包
     */
    public void loadFixDex() throws Exception {
        File[] dexFiles = mDexDir.listFiles();
        List<File> fixDexFiles = new ArrayList<>();
        for (File dexFile : dexFiles) {
            if (dexFile.getName().endsWith(".dex")) {
                fixDexFiles.add(dexFile);
            }
        }
        fixDexFiles(fixDexFiles);
    }

    /**
     * 修复Dex
     *
     * @param fixDexFiles
     */
    private void fixDexFiles(List<File> fixDexFiles) throws Exception {
        //首先获取已经运行的DexElement
        ClassLoader applicationClassLoader = mContext.getClassLoader();
        Object applicationDexElement = getDexElementByClassLoader(applicationClassLoader);
        File optionDir = new File(mDexDir, "odex");
        if (!optionDir.exists()) {
            optionDir.mkdirs();
        }
        //修复
        for (File fixDexFile : fixDexFiles) {
            //dexpath  dex路径
            //optimizedDirectory 解压路径
            //librarySearchPath so文件
            //parent 父ClassLoader
            ClassLoader fixDexClassLoader = new BaseDexClassLoader(fixDexFile.getAbsolutePath(),
                    optionDir,
                    null,
                    applicationClassLoader);
            // 获取这个classLoader中的Element
            Object fixDexElements = getDexElementByClassLoader(fixDexClassLoader);
            //第三步 把补丁DexElement插入到已经运行的DexElement的最前面
            //合并完成
            applicationDexElement = combineArray(fixDexElements, applicationDexElement);
        }
        //注入原来的类中
        injectDexElement(applicationClassLoader, applicationDexElement);
    }
}
