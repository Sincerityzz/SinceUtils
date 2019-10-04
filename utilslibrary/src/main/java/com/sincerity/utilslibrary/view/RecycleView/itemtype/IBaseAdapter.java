package com.sincerity.utilslibrary.view.RecycleView.itemtype;

import android.view.View;

/**
 * Created by Sincerity on 2019/3/29.
 * 描述：
 */
public interface IBaseAdapter {
    interface OnItemClickListener {
        void itemClick(View view, int p);
    }

    interface onItemLongClickListener {
        Boolean itemLongClick(View view, int p);
    }
}
