package com.sincerity.customview.material;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.sincerity.customview.R;
import com.sincerity.utilslibrary.view.RecycleView.adapter.BaseAdapter;
import com.sincerity.utilslibrary.view.RecycleView.adapter.BaseViewHolder;

import java.util.ArrayList;
import java.util.List;

public class RecycleActivity extends AppCompatActivity {
    private RelativeLayout llTitle;
    private int mSuperHeight;
    private int mCurrentPosition;
    private TextView tvTitle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recycle);
        llTitle = findViewById(R.id.ll_title);
        tvTitle = findViewById(R.id.title1);
        Adapter adapter = new Adapter(this, R.layout.item_recycle, getData());
        final RecyclerView view = findViewById(R.id.recycle);
        final LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        view.setLayoutManager(layoutManager);
        view.setHasFixedSize(true);
        view.setAdapter(adapter);
        view.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                //获取悬浮条的高度
                mSuperHeight = llTitle.getHeight();
            }

            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                //监听位置进行调整
                View item = layoutManager.findViewByPosition(mCurrentPosition + 1);
                if (item != null) {
                    if (item.getTop() <= mSuperHeight) {
                        llTitle.setY(-(mSuperHeight - view.getTop()));
                    } else {
                        llTitle.setY(0);
                    }
                }
                if (mCurrentPosition != layoutManager.findFirstVisibleItemPosition()) {
                    mCurrentPosition = layoutManager.findFirstVisibleItemPosition();
                    updateView();
                }
            }
        });
        updateView();
    }

    private void updateView() {
        tvTitle.setText("我是头部" + mCurrentPosition);
    }

    private List getData() {
        List list = new ArrayList();
        for (int i = 0; i < 10; i++) {
            list.add("我是头部" + i);
        }
        return list;
    }

    class Adapter extends BaseAdapter<String> {

        public Adapter(Context context, int mLayoutId, List data) {
            super(context, mLayoutId, data);
        }

        @Override
        protected void setData(BaseViewHolder baseViewHolder, String text, int i) {
            baseViewHolder.setText(R.id.title, text);
        }
    }
}
