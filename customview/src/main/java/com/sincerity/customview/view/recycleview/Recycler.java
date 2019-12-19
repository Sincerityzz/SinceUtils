package com.sincerity.customview.view.recycleview;

import android.view.View;

import java.util.Stack;

/**
 * Created by Sincerity on 2019/12/18.
 * 描述：
 */
public class Recycler {
    private Stack<View>[] views;

    public Recycler(int typeNumber) {
        views = new Stack[typeNumber];
        for (int i = 0; i < typeNumber; i++) {
            views[i] = new Stack<>();
        }
    }

    public void put(View view, int type) {
        views[type].push(view);
    }

    public View get(int position) {
        try {
            return views[position].pop();
        } catch (Exception e) {
            return null;
        }

    }
}
