package com.sincerity.sinceutils.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.sincerity.sinceutils.R;
import com.sincerity.sinceutils.image.BaseImageLoader;
import com.sincerity.utilslibrary.view.RecycleView.adapter.BaseAdapter;
import com.sincerity.utilslibrary.view.RecycleView.adapter.BaseViewHolder;
import com.sincerity.utilslibrary.view.RecycleView.adapter.itype.IAdapterListener;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Sincerity on 2019/9/6.
 * 描述：
 */
public class FoundPage extends Fragment {

    RecyclerView mRecycleView;
    private View mView;
    private ListView mList;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mView = LayoutInflater.from(getActivity()).inflate(R.layout.fragment_found, container, false);
        initViews();
        return mView;
    }

    private void initViews() {
        mRecycleView = mView.findViewById(R.id.rv_foundMain);
        List<String> list = new ArrayList<>();
        for (int i = 0; i < 30; i++) {
            list.add(i + "Since");
        }
        GridLayoutManager layoutManager = new GridLayoutManager(getActivity(), 2);
        mRecycleView.setLayoutManager(layoutManager);

//        mRecycleView.addItemDecoration(new GridLayoutDecoration(getActivity(), R.drawable.line_view));
        mRecycleView.addItemDecoration(new DividerItemDecoration(getActivity(), 1));
        Adapter mAdapter = new Adapter(getActivity(), R.layout.item_found, list);
        mRecycleView.setAdapter(mAdapter);
    }

}

class Adapter extends BaseAdapter<String> {

    Adapter(Context context, int mLayoutId, List<String> data) {
        super(context, mLayoutId, data);
    }

    @Override
    protected void setData(BaseViewHolder baseViewHolder, String s, int i) {
        //绑定数据
        baseViewHolder.setText(R.id.item_tv_main, s);
        baseViewHolder.setImgFromPath(R.id.item_img_main, new BaseImageLoader("https://ss0.bdstatic.com/5aV1bjqh_Q23odCf/static/superman/img/logo/logo_white_fe6da1ec.png"));
//        TextView mTvMain = baseViewHolder.getView(R.id.item_tv_main);
//        mTvMain.setText(s);
//        ((TextView) baseViewHolder.itemView.findViewById(R.id.item_tv_main)).setText(s);
        setOnItemClickListener(new IAdapterListener.OnItemClickListener() {
            @Override
            public void itemClick(View view, int p) {

            }
        });
    }

}