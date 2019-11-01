package com.sincerity.sinceutils.fragment;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.view.ViewCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.sincerity.sinceutils.R;
import com.sincerity.utilslibrary.view.RecycleView.adapter.BaseAdapter;
import com.sincerity.utilslibrary.view.RecycleView.adapter.BaseViewHolder;
import com.sincerity.utilslibrary.view.RecycleView.adapter.WrapperRecycleView;
import com.sincerity.utilslibrary.view.RecycleView.adapter.itype.MultTypeSupport;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

/**
 * Created by Sincerity on 2019/9/6.
 * 描述：
 */
public class MyInfoPage extends Fragment {
    WrapperRecycleView mRecycleView;
    private View mView;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mView = LayoutInflater.from(getActivity()).inflate(R.layout.fragment_info, container, false);
        initViews();
        return mView;
    }

    private void initViews() {
        mRecycleView = mView.findViewById(R.id.rv_infoMain);
        final List<ChartData> list = new ArrayList<>();
        for (int i = 0; i < 30; i++) {
            if (i % 3 == 0) {
                list.add(new ChartData("我的聊天内容" + i, true));
            } else {
                list.add(new ChartData("朋友的聊天内容" + i, false));
            }
        }
        final LinearLayoutManager manager = new LinearLayoutManager(getActivity());
        manager.setOrientation(LinearLayoutManager.VERTICAL);
        mRecycleView.setLayoutManager(manager);
        mRecycleView.addItemDecoration(new DividerItemDecoration(getActivity(), DividerItemDecoration.VERTICAL));
        final Adapter mAdapter = new Adapter(getActivity(), list);
//        final WrapperRecycleViewAdapter adapter = new WrapperRecycleViewAdapter(mAdapter);
        mRecycleView.setAdapter(mAdapter);
        final View mHeaderView = LayoutInflater.from(getActivity()).inflate(R.layout.header_view, mRecycleView, false);
        mRecycleView.addHeaderView(mHeaderView);
        mHeaderView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mRecycleView.removeHeaderView(mHeaderView);
                mAdapter.notifyItemRemoved(0);
            }
        });
        ItemTouchHelper helper = new ItemTouchHelper(new ItemTouchHelper.Callback() {
            @Override
            public int getMovementFlags(@NonNull RecyclerView recyclerView, @NonNull
                    RecyclerView.ViewHolder viewHolder) {
                //移动的标记
                int swipeFlags = 0;
                int dragFlags = 0;
                //GridLayoutManager 是继承 LinearLayoutManager
                if (recyclerView.getLayoutManager() instanceof GridLayoutManager) {
                    dragFlags = ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT | ItemTouchHelper.UP | ItemTouchHelper.DOWN;
                } else {
                    dragFlags = ItemTouchHelper.UP | ItemTouchHelper.DOWN;
                    swipeFlags = ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT;
                }
                //拖动
                return makeMovementFlags(dragFlags, swipeFlags);
            }

            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder viewHolder1) {
                //原来的位置
                int fontPosition = viewHolder.getAdapterPosition();
                //目标位置
                int targetPosition = viewHolder1.getAdapterPosition();
                //替换的只是位置 并没有替换数据
                mAdapter.notifyItemMoved(fontPosition, targetPosition);
                //数据的替换
                if (fontPosition > targetPosition) {
                    for (int i = fontPosition; i < targetPosition; i++) {
                        Collections.swap(list, i, i + 1);
                    }
                } else {
                    for (int i = targetPosition; i < fontPosition; i++) {
                        Collections.swap(list, i, i - 1);
                    }
                }
                return false;
            }

            @Override
            public void onSelectedChanged(@Nullable RecyclerView.ViewHolder viewHolder, int actionState) {
                super.onSelectedChanged(viewHolder, actionState);
                //状态发生改变 正常  侧滑 拖动
                if (actionState != ItemTouchHelper.ACTION_STATE_IDLE) {
                    viewHolder.itemView.setBackgroundColor(Color.GREEN);
                }
                //显示列表的复用

            }

            @Override
            public void clearView(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder) {
                super.clearView(recyclerView, viewHolder);
                //消除界面复用的问题 动画执行完毕
                viewHolder.itemView.setBackgroundColor(Color.WHITE);
                ViewCompat.setTranslationX(viewHolder.itemView, 0);
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int i) {
                //侧滑执行删除完成 ,需要更新Adapter
                int position = viewHolder.getAdapterPosition(); //当前侧滑的位置
                list.remove(position);
                mAdapter.notifyDataSetChanged();
                super.onSelectedChanged(viewHolder, i);
            }
        });
        //必须绑定RecycleView
        helper.attachToRecyclerView(mRecycleView);
    }

    class Adapter extends BaseAdapter<ChartData> {

        public Adapter(Context context, List data) {
            super(context, data, new MultTypeSupport<ChartData>() {
                @Override
                public int getLayoutId(ChartData item) {
                    if (item.isMe) {
                        return R.layout.item_right;
                    }
                    return R.layout.item_left;
                }
            });
        }

        public void addAll(Collection collection) {

        }

        @Override
        protected void setData(BaseViewHolder baseViewHolder, ChartData data, int i) {
            if (data.isMe) {
                baseViewHolder.setText(R.id.item_tv_right, data.chartContent);
            } else {
                baseViewHolder.setText(R.id.item_tv_left, data.chartContent);
            }

        }
    }
}

class ChartData {
    String chartContent;
    boolean isMe;

    public ChartData(String chartContent, boolean isMe) {
        this.chartContent = chartContent;
        this.isMe = isMe;
    }
}
