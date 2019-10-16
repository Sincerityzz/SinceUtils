package com.sincerity.utilslibrary.view.RecycleView.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.View;

/**
 * Created by Sincerity on 2019/10/16.
 * 描述：
 */
public class WrapperRecycleView extends RecyclerView {
    private WrapperRecycleViewAdapter mAdapter;
    private AdapterDataObserver mDataObserver = new AdapterDataObserver() {
        @Override
        public void onChanged() {
            super.onChanged();
            mAdapter.notifyDataSetChanged();
        }

        @Override
        public void onItemRangeChanged(int positionStart, int itemCount) {
//            super.onItemRangeChanged(positionStart, itemCount);
            mAdapter.notifyItemRangeChanged(positionStart, itemCount);
        }

        @Override
        public void onItemRangeChanged(int positionStart, int itemCount, @Nullable Object payload) {
//            super.onItemRangeChanged(positionStart, itemCount, payload);
            mAdapter.notifyItemRangeChanged(positionStart, itemCount, payload);
        }

        @Override
        public void onItemRangeInserted(int positionStart, int itemCount) {
//            super.onItemRangeInserted(positionStart, itemCount);
            mAdapter.notifyItemRangeInserted(positionStart, itemCount);
        }

        @Override
        public void onItemRangeRemoved(int positionStart, int itemCount) {
//            super.onItemRangeRemoved(positionStart, itemCount);
            mAdapter.notifyItemRangeRemoved(positionStart, itemCount);
        }

        @Override
        public void onItemRangeMoved(int fromPosition, int toPosition, int itemCount) {
//            super.onItemRangeMoved(fromPosition, toPosition, itemCount);
            mAdapter.notifyItemRangeRemoved(fromPosition, toPosition);
        }
    };

    public WrapperRecycleView(@NonNull Context context) {
        this(context, null);
    }

    public WrapperRecycleView(@NonNull Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public WrapperRecycleView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override
    public void setAdapter(@Nullable Adapter adapter) {
        //重新设置Adapter时候需要取消注册的观察者模式
        if (mAdapter != null && mDataObserver != null) {
            adapter.unregisterAdapterDataObserver(mDataObserver);
        }
        if (adapter instanceof WrapperRecycleViewAdapter) {
            this.mAdapter = (WrapperRecycleViewAdapter) adapter;
        } else {
            this.mAdapter = new WrapperRecycleViewAdapter(adapter);
            adapter.registerAdapterDataObserver(mDataObserver);
        }
        //列表的adapter和mAdapter不同步 需要二个adapter关联起来 使用观察者模式
        super.setAdapter(this.mAdapter);
    }

    public void addHeaderView(View view) {
        if (mAdapter != null) {
            mAdapter.addHeaderView(view);
        }

    }

    public void addFooterView(View view) {
        if (mAdapter != null) {
            mAdapter.addFooterView(view);
        }
    }

    public void removeHeaderView(View view) {
        if (mAdapter != null) {
            mAdapter.removeHeaderView(view);
        }
    }

    public void removeFooterView(View view) {
        if (mAdapter != null) {
            mAdapter.removeFooterView(view);
        }
    }
}
