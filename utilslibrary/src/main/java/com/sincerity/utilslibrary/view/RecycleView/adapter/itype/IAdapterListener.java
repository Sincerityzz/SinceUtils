package com.sincerity.utilslibrary.view.RecycleView.adapter.itype;


import android.view.View;

/**
 * Created by Sincerity on 2019/10/16.
 * 描述：
 */
public interface IAdapterListener {
    interface OnItemClickListener {
        void itemClick(View view, int p);
    }

    interface onItemLongClickListener {
         Boolean itemLongClick(View view, int p);
    }
}

