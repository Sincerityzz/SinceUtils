package com.sincerity.customview.animator;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Sincerity on 2019/12/13.
 * 描述：VSYNC
 */
public class VSYNCManager {
    private static final VSYNCManager instance = new VSYNCManager();

    public static VSYNCManager getInstance() {
        return instance;
    }

    private List<AnimatorFrameCallBack> list = new ArrayList<>();

    public void add(AnimatorFrameCallBack animatorFrameCallBack) {
        list.add(animatorFrameCallBack);
    }

    public VSYNCManager() {
        new Thread(runnable).start();
    }

    private Runnable runnable = new Runnable() {
        @Override
        public void run() {
            while (true) {
                try {
                    Thread.sleep(16);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                for (AnimatorFrameCallBack animatorFrameCallBack : list) {
                    animatorFrameCallBack.doAnimatorFrame(System.currentTimeMillis());
                }
            }
        }
    };

    interface AnimatorFrameCallBack {
        boolean doAnimatorFrame(long time);
    }
}
