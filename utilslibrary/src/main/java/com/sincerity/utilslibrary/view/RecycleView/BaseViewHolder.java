package com.sincerity.utilslibrary.view.RecycleView;

import android.support.annotation.LayoutRes;
import android.support.v7.widget.RecyclerView;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Checkable;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.sincerity.utilslibrary.view.RecycleView.itemtype.IViewHolder;

/**
 * Created by Sincerity on 2019/9/24.
 * 描述：
 */
public class BaseViewHolder <T> extends RecyclerView.ViewHolder implements IViewHolder<BaseViewHolder> {
    private SparseArray<View> sparseArray;

    public BaseViewHolder(View itemView) {
        super(itemView);
        sparseArray = new SparseArray<>();
    }

    public BaseViewHolder(ViewGroup view, @LayoutRes int res) {
        super(LayoutInflater.from(view.getContext()).inflate(res, view, false));
        sparseArray = new SparseArray<>();
    }


    @SuppressWarnings("unchecked")
    public <T extends View> T getView(int viewId) {
        View view = sparseArray.get(viewId);
        if (view == null) {
            view = itemView.findViewById(viewId);
            sparseArray.put(viewId, view);
        }
        return (T) view;
    }

    @Override
    public BaseViewHolder setText(int viewId, String text) {
        View view = getView(viewId);
        if (view instanceof TextView) {
            ((TextView) view).setText(text);
        }
        if (view instanceof Button) {
            ((Button) view).setText(text);
        }
        return this;
    }

    @Override
    public BaseViewHolder setImgResource(int viewId, int resId) {
        View view = getView(viewId);
        if (view instanceof ImageView) {
            ((ImageView) view).setImageResource(resId);
        }
        if (view instanceof ImageButton) {
            ((ImageButton) view).setImageResource(resId);
        }
        return this;
    }

    /**
     * 设置是否可见
     */
    @Override
    public BaseViewHolder setVisible(int viewId, boolean visible) {
        View view = getView(viewId);
        view.setVisibility(visible ? View.VISIBLE : View.GONE);
        return this;
    }

    public void setData(T data) {

    }

    /**
     * 是否选中
     */
    @Override
    public BaseViewHolder setChecked(int viewId, boolean ischecked) {
        Checkable view = getView(viewId);
        view.setChecked(ischecked);
        return this;
    }

//    //事件相关
//    @Override
//    public BaseViewHolder setOnClickListener(int viewId) {
//        View view = getView(viewId);
//        if (view != null) {
//            view.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    adapterWrapper.getmOnItemChildClickListener().itemClick(v, getAdapterPosition());
//                }
//            });
//        }
//        return this;
//    }
//
//    @Override
//    public BaseViewHolder setOnTouchListener(int viewId) {
//        View view = getView(viewId);
//        if (view != null) {
//            view.setOnTouchListener(new View.OnTouchListener() {
//                @Override
//                public boolean onTouch(View v, MotionEvent event) {
//                    return false;
//                }
//            });
//        }
//        return this;
//    }
//
//    @Override
//    public BaseViewHolder setOnLongClickListener(int viewId) {
//        View view = getView(viewId);
//        if (view != null) {
//            view.setLongClickable(true);
//            view.setOnLongClickListener(new View.OnLongClickListener() {
//                @Override
//                public boolean onLongClick(View v) {
//                    return  adapterWrapper.getOnItemLongClickListener().
//                            itemLongClick(v, getAdapterPosition());
//                }
//            });
//        }
//
//        return this;
//    }
}
