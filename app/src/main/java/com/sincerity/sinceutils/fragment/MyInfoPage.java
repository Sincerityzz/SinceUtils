package com.sincerity.sinceutils.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.sincerity.sinceutils.R;
import com.sincerity.utilslibrary.view.RecycleView.adapter.BaseAdapter;
import com.sincerity.utilslibrary.view.RecycleView.adapter.BaseViewHolder;
import com.sincerity.utilslibrary.view.RecycleView.adapter.WrapperRecycleView;
import com.sincerity.utilslibrary.view.RecycleView.adapter.itype.MultTypeSupport;

import java.util.ArrayList;
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
        List<ChartData> list = new ArrayList<>();
        for (int i = 0; i < 30; i++) {
            if (i % 3 == 0) {
                list.add(new ChartData("我的聊天内容" + i, true));
            } else {
                list.add(new ChartData("朋友的聊天内容" + i, false));
            }
        }
        LinearLayoutManager manager = new LinearLayoutManager(getActivity());
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
