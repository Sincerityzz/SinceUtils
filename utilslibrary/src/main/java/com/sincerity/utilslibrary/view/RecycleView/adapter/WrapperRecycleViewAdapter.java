package com.sincerity.utilslibrary.view.RecycleView.adapter;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import android.util.SparseArray;
import android.view.View;
import android.view.ViewGroup;

import static androidx.recyclerview.widget.GridLayoutManager.SpanSizeLookup;

/**
 * Created by Sincerity on 2019/10/16.
 * 描述：
 */
public class WrapperRecycleViewAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    //不包含头部的Adapter
    private RecyclerView.Adapter mAdapter;
    //头部和底部的集合 必须要用Map集合进行标识 if key是int value是obj google 推荐sparseArray
    private SparseArray<View> mHeaders, mFooters;
    private static int BASEHEADER_KEY = 0x00059;
    private static int BASEFOOTER_KEY = 0x0000059;

    //添加头部 和 底部
    //移除头部和底部

    public WrapperRecycleViewAdapter(RecyclerView.Adapter adapter) {
        this.mAdapter = adapter;
        mHeaders = new SparseArray<>();
        mFooters = new SparseArray<>();
    }

    @NonNull
    @Override

    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        // viewType 分为头 中间 尾部
        if (mHeaders.indexOfKey(viewType) >= 0) {
            //头部
            return createHeaderViewHolder(mHeaders.get(viewType));
        } else if (mFooters.indexOfKey(viewType) >= 0) {
            //尾部
            return createFooterViewHolder(mHeaders.get(viewType));
        }

        return mAdapter.onCreateViewHolder(viewGroup, viewType);
    }

    private RecyclerView.ViewHolder createFooterViewHolder(View view) {
        return new RecyclerView.ViewHolder(view) {
        };
    }

    private RecyclerView.ViewHolder createHeaderViewHolder(View view) {
        return new RecyclerView.ViewHolder(view) {
        };
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, int position) {
        //头部都不用去处理
        int numHeaders = mHeaders.size();
        if (position < numHeaders) {
            return;
        }

        //Adapter
        final int adjPosition = position - numHeaders;
        int adapterCount = 0;
        if (mAdapter != null) {
            adapterCount = mAdapter.getItemCount();
            if (adjPosition < adapterCount) {
                mAdapter.onBindViewHolder(viewHolder, adjPosition);
            }
        }
        //Footer
    }

    @Override
    public int getItemCount() {
        return mAdapter.getItemCount() + mHeaders.size() + mFooters.size();
    }

    @Override
    public int getItemViewType(int position) {
        int numHeaders = mHeaders.size();
        //Header
        //根据位置返回ViewType
        if (position < numHeaders) {
            return mHeaders.keyAt(position);
        }
        //Adapter
        final int adjPosition = position - numHeaders;
        int adapterCount = 0;
        if (mAdapter != null) {
            adapterCount = mAdapter.getItemCount();
            if (adjPosition < adapterCount) {
                return mAdapter.getItemViewType(adjPosition);
            }
        }
        //Footer
        return mFooters.keyAt(adjPosition - adapterCount);
    }

    public void addHeaderView(View view) {
        if (mHeaders.indexOfValue(view) == -1) {
            //区分view
            mHeaders.put(BASEHEADER_KEY++, view);
        }
    }

    public void addFooterView(View view) {
        if (mFooters.indexOfValue(view) == -1) {
            //区分view
            mFooters.put(BASEFOOTER_KEY++, view);
        }
    }

    public void removeHeaderView(View view) {
        if (mHeaders.indexOfValue(view) >= 0) {
            mHeaders.removeAt(mHeaders.indexOfValue(view));
        }
    }

    public void removeFooterView(View view) {
        if (mFooters.indexOfValue(view) >= 0) {
            mFooters.removeAt(mFooters.indexOfValue(view));
        }
    }

    public class GridSpanSizeLookup extends SpanSizeLookup {
        private int mMaxCount;

        GridSpanSizeLookup(int maxCount) {
            this.mMaxCount = maxCount;
        }

        @Override
        public int getSpanSize(int position) {
            if (mHeaders.size() != 0) {
                if (position < mHeaders.size()) {
                    return mMaxCount;
                }
            }
            if (mFooters.size() != 0) {
                int i = position - mFooters.size() - mAdapter.getItemCount();
                if (i >= 0) {
                    return mMaxCount;
                }
            }
            return 1;
        }
    }

    public GridSpanSizeLookup obtainGridSpanSizeLookUp(int maxCount) {
        return new GridSpanSizeLookup(maxCount);
    }

    //兼容显示问题
    @Override
    public void onAttachedToRecyclerView(@NonNull RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
        registerAdapterDataObserver(new FixDataObserver(recyclerView));
    }

    public int getFooterCount() {
        return mFooters.size();
    }

    public int getHeaderCount() {
        return mHeaders.size();
    }

    public int getCount() {
        return mAdapter.getItemCount();
    }

    private class FixDataObserver extends RecyclerView.AdapterDataObserver {

        private RecyclerView recyclerView;

        FixDataObserver(RecyclerView recyclerView) {
            this.recyclerView = recyclerView;
        }

        @Override
        public void onItemRangeInserted(int positionStart, int itemCount) {
            if (recyclerView.getAdapter() instanceof WrapperRecycleViewAdapter) {
                WrapperRecycleViewAdapter adapter = (WrapperRecycleViewAdapter) recyclerView.getAdapter();
                if (adapter.getFooterCount() > 0 && adapter.getCount() == itemCount) {
                    recyclerView.scrollToPosition(0);
                }
            }
        }
    }
}
