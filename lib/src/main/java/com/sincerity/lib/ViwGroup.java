package com.sincerity.lib;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Sincerity on 2019/12/13.
 * 描述：手写View事件分发
 */
public class ViwGroup extends View {
    List<View> childList = new ArrayList<>();
    private View[] mChildren = new View[0];
    private TouchTarget mFirstTouchTarget;
    private String name;

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    ViwGroup(int left, int top, int right, int bottom) {
        super(left, top, right, bottom);
    }

    public void addView(View view) {
        if (view == null) {
            return;
        }
        childList.add(view);
        mChildren = (View[]) childList.toArray(new View[childList.size()]);
    }

    //事件分发的入口
    public boolean dispatchTouchEvent(MotionEvent ev) {
        //down事件 就是基础的控件赋值
        TouchTarget mTouchTarget = null;
        boolean handled = false;
        boolean intercept = onInterceptTouchEvent(ev); //是否拦截事件
        int actionMasked = ev.getActionMasked(); //获取时间类型
        if (actionMasked != MotionEvent.ACTION_CANCEL && !intercept) {
            if (actionMasked == MotionEvent.ACTION_DOWN) {//Down事件
                final View[] children = mChildren;
                //耗时 最后一个添加进ViewGroup的View接受到事件的概率较大
                for (int i = children.length - 1; i >= 0; i--) {
                    View child = mChildren[i];
                    if (!child.isContainer(ev.getX(), ev.getY())) {
                        continue; //不能接受事件
                    }
                    //能接受事件 分发给它事件
                    if (dispatchTransFormTouchEvent(ev, child)) {
                        //Message的链表结构 双向链表
                        handled = true;
                        mTouchTarget = addTouchTarget(child);
                        break;
                    }
                }
            }
            //这里就是Move事件 而move事件不会在进行循环查找View 通过一个双向的链表来取出Down中缓存的Target对象
            //通过Target直接去进行事件的分发操作
            //当前的View的dispatchTransFormTouchEvent
            if (mFirstTouchTarget == null) {
                handled = dispatchTransFormTouchEvent(ev, null);
            }
        }
        return handled;
    }

    private TouchTarget addTouchTarget(View child) {
        TouchTarget target = TouchTarget.obtain(child);//实现了回收池策略
        target.next = mFirstTouchTarget;
        mFirstTouchTarget = target;
        return target;
    }

    private static final class TouchTarget {
        public View child;//当前缓存的View
        //回收池对象  单链表 单链表通过一个一个值 就可以获取所有的值 只有一个对象
        private static TouchTarget mRecycleBin;
        private static final Object xRecycleLock = new Object[0];
        public TouchTarget next;
        private static int mRecycleSize;

        /**
         * 链表实现的回收池
         *
         * @param view 自View
         * @return
         */
        public static TouchTarget obtain(View view) {
            TouchTarget target;
            synchronized (xRecycleLock) { //对象锁
                if (mRecycleBin == null) {
                    target = new TouchTarget();
                } else {
                    target = mRecycleBin;
                }
                mRecycleBin = target.next;
                mRecycleSize--;
                target.next = null;
            }
            target.child = view;
            return target;
        }

        /**
         * 对象池的回收池回收对象
         */
        public void recycle() {
            if (child == null) {
                throw new IllegalStateException("对象已经被回收");
            }
            synchronized (xRecycleLock) {
                //
                if (mRecycleSize < 32) {
                    next = mRecycleBin;
                    mRecycleBin = this;
                    mRecycleSize += 1;
                }
            }
        }
    }

    //分发处理
    private boolean dispatchTransFormTouchEvent(MotionEvent ev, View child) {
        boolean handled = false;
        if (child != null) {
            handled = child.dispatchTouchEvent(ev);
        } else {
            //super 指 View 即viewGroup的父类
            handled = super.dispatchTouchEvent(ev);
        }
        return handled;
    }

    //事件拦截]
    public boolean onInterceptTouchEvent(MotionEvent event) {
        return false;
    }
}
