package com.sincerity.utilslibrary.view.RecycleView;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.View;
import android.view.ViewGroup;

import com.sincerity.utilslibrary.view.RecycleView.itemtype.IBaseAdapter;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * Created by Sincerity on 2019/9/24.
 * 描述：
 */
public abstract class BaseReAdapter<T> extends RecyclerView.Adapter<BaseViewHolder> {
    private List<T> mObjects; //存放内容
    private ArrayList<ItemView> mHeaderViews = new ArrayList<>();
    private ArrayList<ItemView> mFootViews = new ArrayList<>();
    private Context mContext;
    private IBaseAdapter.OnItemClickListener onItemClickListener;
    private IBaseAdapter.onItemLongClickListener onItemLongClickListener;
    private boolean mNotifyOnChange = true;
    private Object mLock = new Object();

    public void setOnItemClickListener(IBaseAdapter.OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    public void setOnItemLongClickListener(IBaseAdapter.onItemLongClickListener onItemLongClickListener) {
        this.onItemLongClickListener = onItemLongClickListener;
    }

    public BaseReAdapter(List<T> mObjects, Context mContext) {
        init(mContext, mObjects);
    }

    public BaseReAdapter(Context mContext) {
        init(mContext, new ArrayList<T>());
    }

    private void init(Context mContext, List<T> obj) {
        this.mContext = mContext;
        mObjects = new ArrayList<>(obj);
    }

    public void add(T object) {
        if (object != null) {
            synchronized (new Object()) {
                mObjects.add(object);
            }
        }
        if (mNotifyOnChange) {
            notifyItemInserted(getCount() + getHeaderSize());
        }
    }

    public void addAll(Collection<? extends T> collection) {
        if (collection != null) {
            synchronized (mLock) {
                mObjects.addAll(collection);
            }
        }
        int dataCount = collection == null ? 0 : collection.size();
        notifyItemRangeChanged(getCount() + getHeaderSize() - dataCount, dataCount);
    }

    public void addHeaderView(ItemView view) {
        if (view == null) {
            throw new NullPointerException("ItemView can't be null");
        }
        mHeaderViews.add(view);
        notifyItemInserted(mHeaderViews.size() - 1);
    }

    public void addFooterView(ItemView view) {
        if (view == null) {
            throw new NullPointerException("ItemView can't be null");
        }
        mFootViews.add(view);
        notifyItemInserted(mHeaderViews.size() + mObjects.size() + mFootViews.size() - 1);
    }

    public int getHeaderSize() {
        return mHeaderViews.size();
    }

    public int getFooterSize() {
        return mFootViews.size();
    }

    private View createSpViewByType(ViewGroup parent, int viewType) {
        for (ItemView headerView : mHeaderViews) {
            if (headerView.hashCode() == viewType) {
                View view = headerView.onCreateView(parent);
                StaggeredGridLayoutManager.LayoutParams layoutParams;
                if (view == null) {
                    return null;
                }
                if (view.getLayoutParams() != null) {
                    layoutParams = new StaggeredGridLayoutManager.LayoutParams(view.getLayoutParams());
                } else {
                    layoutParams = new StaggeredGridLayoutManager.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                }
                layoutParams.setFullSpan(true);
                view.setLayoutParams(layoutParams);
                return view;
            }
        }
        for (ItemView footView : mFootViews) {
            if (footView.hashCode() == viewType) {
                View view = footView.onCreateView(parent);
                StaggeredGridLayoutManager.LayoutParams layoutParams;
                if (view == null) {
                    return null;
                }
                if (view.getLayoutParams() != null) {
                    layoutParams = new StaggeredGridLayoutManager.LayoutParams(view.getLayoutParams());
                } else {
                    layoutParams = new StaggeredGridLayoutManager.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                            ViewGroup.LayoutParams.WRAP_CONTENT);
                }
                layoutParams.setFullSpan(true);
                view.setLayoutParams(layoutParams);
                return view;
            }

        }
        return null;
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
        //增加对RecyclerArrayAdapter奇葩操作的修复措施
        registerAdapterDataObserver(new FixDataObserver(recyclerView));
    }

    private class FixDataObserver extends RecyclerView.AdapterDataObserver {

        private RecyclerView recyclerView;

        FixDataObserver(RecyclerView recyclerView) {
            this.recyclerView = recyclerView;
        }

        @Override
        public void onItemRangeInserted(int positionStart, int itemCount) {
            if (recyclerView.getAdapter() instanceof BaseReAdapter) {
                BaseReAdapter adapter = (BaseReAdapter) recyclerView.getAdapter();
                if (adapter.getFooterCount() > 0 && adapter.getCount() == itemCount) {
                    recyclerView.scrollToPosition(0);
                }
            }
        }
    }

    public int getFooterCount() {
        return mFootViews.size();
    }

    public int getCount() {
        return mObjects.size();
    }

    @Override
    public final BaseViewHolder onCreateViewHolder(ViewGroup viewGroup, final int i) {
        View view = createSpViewByType(viewGroup, i);
        if (view != null) {
            return new StateViewHolder(view);
        }
        final BaseViewHolder viewHolder = onCreateVH(viewGroup, i);
        if (onItemClickListener != null) {
            viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onItemClickListener.itemClick(viewHolder.itemView,
                            viewHolder.getAdapterPosition() - mHeaderViews.size());
                }
            });
        }
        if (onItemLongClickListener != null) {
            viewHolder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    return onItemLongClickListener.itemLongClick(v, viewHolder.getAdapterPosition() - mHeaderViews.size());
                }
            });
        }
        return viewHolder;
    }


    public abstract BaseViewHolder onCreateVH(ViewGroup parent, int viewType);

    private void onBindVH(BaseViewHolder baseViewHolder, int pos) {
        baseViewHolder.setData(mObjects.get(pos));
    }

    @Override
    public final void onBindViewHolder(BaseViewHolder baseViewHolder, int i) {
        baseViewHolder.itemView.setId(i);
        if (mHeaderViews.size() != 0 && i < mHeaderViews.size()) {
            mHeaderViews.get(i).onBindView(baseViewHolder.itemView);
            return;
        }
        int footPos = i - mHeaderViews.size() - mObjects.size();
        if (footPos >= 0 && mFootViews.size() != 0) {
            mFootViews.get(footPos).onBindView(baseViewHolder.itemView);
            return;
        }

        onBindVH(baseViewHolder, i - mHeaderViews.size());
    }

    @Override
    public int getItemCount() {
        return mObjects.size() + mHeaderViews.size() + mFootViews.size();
    }

    @Override
    public int getItemViewType(int position) {
        if (mHeaderViews.size() != 0) {
            if (position < mHeaderViews.size()) {
                return mHeaderViews.get(position).hashCode();
            }
        }
        if (mFootViews.size() != 0) {
            int i = position - mHeaderViews.size() - mObjects.size();
            if (i >= 0) {
                return mFootViews.get(i).hashCode();
            }
        }
        return getViewType(position - mHeaderViews.size());
    }

    public int getViewType(int position) {
        return 0;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    public interface ItemView {
        View onCreateView(ViewGroup parent);

        void onBindView(View headerView);
    }

    private class StateViewHolder extends BaseViewHolder {
        StateViewHolder(View itemView) {
            super(itemView);
        }
    }
}
