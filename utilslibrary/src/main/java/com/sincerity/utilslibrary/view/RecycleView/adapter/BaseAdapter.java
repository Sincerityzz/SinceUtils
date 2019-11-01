package com.sincerity.utilslibrary.view.RecycleView.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.sincerity.utilslibrary.view.RecycleView.adapter.itype.IAdapterListener;
import com.sincerity.utilslibrary.view.RecycleView.adapter.itype.IAdapterListener.OnItemClickListener;
import com.sincerity.utilslibrary.view.RecycleView.adapter.itype.IAdapterListener.onItemLongClickListener;
import com.sincerity.utilslibrary.view.RecycleView.adapter.itype.MultTypeSupport;

import java.util.Collection;
import java.util.List;

/**
 * Created by Sincerity on 2019/10/8.
 * 描述： 万能的RecycleView的适配器
 * 1. 包含基础的适配器封装 如(设置文本,设置图片加载的方式...)
 * 2. 多布局适配
 * 3. 添加的点击事件
 * 4. 上拉刷新和下拉加载
 * 5. 头部和尾部的添加
 */
public abstract class BaseAdapter<T> extends RecyclerView.Adapter<BaseViewHolder> {
    private int mLayoutId;
    private List<T> mData;
    private LayoutInflater mInflater;
    private OnItemClickListener onItemClickListener;
    private onItemLongClickListener onItemLongClickListener;
    private MultTypeSupport mTypeSupport;

    public void setOnItemLongClickListener(IAdapterListener.onItemLongClickListener onItemLongClickListener) {
        this.onItemLongClickListener = onItemLongClickListener;
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    public BaseAdapter(Context context, int mLayoutId, List<T> data) {
        this.mLayoutId = mLayoutId;
        this.mData = data;
        mInflater = LayoutInflater.from(context);
    }

    public BaseAdapter(Context context, List<T> data, MultTypeSupport typeSupport) {
        this(context, -1, data);
        this.mTypeSupport = typeSupport;
    }

    public void addAll(Collection c) {
        if (mData.size() > 0) {
            mData.clear();
        }
        mData.addAll(c);
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public BaseViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        //实例化View 3种

        /**
         *  1. View.inflate(context,mLayoutId,null)
         *  2. LayoutInflater.from(context).inflate(mLayoutId,viewGroup);
         *  3.mInflater.inflate(mLayoutId, viewGroup, false);
         */
        if (mTypeSupport != null) {
            //多布局
            mLayoutId = i;
        }
        View view = mInflater.inflate(mLayoutId, viewGroup, false);
        return new BaseViewHolder(view);
    }

    @Override
    public int getItemViewType(int position) {
        if (mTypeSupport != null) {
            //多布局需要返回的layoutId
            return mTypeSupport.getLayoutId(mData.get(position));
        }
        return super.getItemViewType(position);
    }

    @Override
    public void onBindViewHolder(@NonNull BaseViewHolder baseViewHolder, final int i) {
        setData(baseViewHolder, mData.get(i), i);
        if (onItemClickListener != null) {
            baseViewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onItemClickListener.itemClick(v, i);
                }
            });
        }
        if (onItemLongClickListener != null) {
            baseViewHolder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    return onItemLongClickListener.itemLongClick(v, i);
                }
            });
        }

    }

    /**
     * 传递出去需要的参数
     *
     * @param baseViewHolder viewHolder
     * @param t              当前的参数
     * @param i              当前位置
     */
    protected abstract void setData(BaseViewHolder baseViewHolder, T t, int i);

    @Override
    public int getItemCount() {
        return mData == null ? 0 : mData.size();
    }
}
