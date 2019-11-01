package com.sincerity.sinceutils.fragment;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.RecyclerView;

import com.sincerity.sinceutils.R;
import com.sincerity.sinceutils.image.BaseImageLoader;
import com.sincerity.utilslibrary.view.RecycleView.adapter.BaseAdapter;
import com.sincerity.utilslibrary.view.RecycleView.adapter.BaseViewHolder;
import com.sincerity.utilslibrary.view.RecycleView.adapter.WrapperRecycleView;
import com.sincerity.utilslibrary.view.RecycleView.adapter.itype.IAdapterListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by Sincerity on 2019/9/6.
 * 描述：
 */
public class FoundPage extends Fragment {

    WrapperRecycleView mRecycleView;
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
        final List<String> list = new ArrayList<>();
        for (int i = 0; i < 30; i++) {
            list.add(i + "Since");
        }
        GridLayoutManager layoutManager = new GridLayoutManager(getActivity(), 2);
        mRecycleView.setLayoutManager(layoutManager);
//        mRecycleView.setGridLayoutSpanSizeLookUp(2);
//        mRecycleView.addItemDecoration(new GridLayoutDecoration(getActivity(), R.drawable.line_view));
        mRecycleView.addItemDecoration(new DividerItemDecoration(getActivity(), 1));
        final Adapter mAdapter = new Adapter(getActivity(), R.layout.item_found, list);
        mRecycleView.setAdapter(mAdapter);
        ItemTouchHelper helper = new ItemTouchHelper(new ItemTouchHelper.Callback() {
//            @Override
//            public int getMovementFlags(@NonNull RecyclerView recyclerView, @NonNull
//                    RecyclerView.ViewHolder viewHolder) {
//                //移动的标记
//                int swipeFlags = 0;
//                int dragFlags = 0;
//                //GridLayoutManager 是继承 LinearLayoutManager
//                if (recyclerView.getLayoutManager() instanceof GridLayoutManager) {
//                    dragFlags = ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT | ItemTouchHelper.UP | ItemTouchHelper.DOWN;
//                } else {
//                    dragFlags = ItemTouchHelper.UP | ItemTouchHelper.DOWN;
//                    swipeFlags = ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT;
//                }
//                //拖动
//                return makeMovementFlags(dragFlags, swipeFlags);
//            }
//
//            @Override
//            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder viewHolder1) {
//                //原来的位置
//                int fontPosition = viewHolder.getAdapterPosition();
//                //目标位置
//                int targetPosition = viewHolder1.getAdapterPosition();
//                //数据的替换
//                if (fontPosition > targetPosition) {
//                    for (int i = fontPosition; i < targetPosition; i++) {
//                        Collections.swap(list, i, i + 1);
//                    }
//                } else {
//                    for (int i = targetPosition; i < fontPosition; i++) {
//                        Collections.swap(list, i, i - 1);
//                    }
//                }
//                Log.e("admin",fontPosition+"-----------"+targetPosition);
//                mAdapter.notifyItemMoved(fontPosition, targetPosition);
//                return true;
//            }
//
//            @Override
//            public void onSelectedChanged(@Nullable RecyclerView.ViewHolder viewHolder, int actionState) {
//                super.onSelectedChanged(viewHolder, actionState);
//                //状态发生改变 正常  侧滑 拖动
//                if (actionState != ItemTouchHelper.ACTION_STATE_IDLE)
//                    viewHolder.itemView.setBackgroundColor(Color.GREEN);
//            }
//
//            @Override
//            public void clearView(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder) {
//                super.clearView(recyclerView, viewHolder);
//                //消除界面复用的问题 动画执行完毕
//                viewHolder.itemView.setBackgroundColor(Color.WHITE);
//            }
//
//            @Override
//            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int i) {
//                //侧滑执行删除完成 ,需要更新Adapter
//                int position = viewHolder.getAdapterPosition(); //当前侧滑的位置
//                mAdapter.notifyItemRemoved(position);
//                list.remove(position);
//            }

            /**
             * 这个方法是设置是否滑动时间，以及拖拽的方向，所以在这里需要判断一下是列表布局还是网格布局，
             * 如果是列表布局的话则拖拽方向为DOWN和UP，如果是网格布局的话则是DOWN和UP和LEFT和RIGHT
             *
             * @param recyclerView
             * @param viewHolder
             * @return
             */
            @Override
            public int getMovementFlags(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder) {

                if (recyclerView.getLayoutManager() instanceof GridLayoutManager) {
                    final int dragFlags = ItemTouchHelper.UP | ItemTouchHelper.DOWN |
                            ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT;
                    final int swipeFlags = 0;
                    return makeMovementFlags(dragFlags, swipeFlags);
                } else {
                    final int dragFlags = ItemTouchHelper.UP | ItemTouchHelper.DOWN;
                    final int swipeFlags = 0;
                    return makeMovementFlags(dragFlags, swipeFlags);
                }
            }


            /**
             * onMove（）方法则是我们在拖动的时候不断回调的方法，在这里我们需要将正在拖拽的item和集合的item进行交换元素，然后在通知适配器更新数据
             *
             * @param recyclerView
             * @param viewHolder
             * @param target
             * @return
             */
            @Override
            public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {

                //得到当拖拽的viewHolder的Position
                int fromPosition = viewHolder.getAdapterPosition();
                //拿到当前拖拽到的item的viewHolder
                int toPosition = target.getAdapterPosition();

//                if(list.get(fromPosition).getLocked() || list.get(toPosition).getLocked()){
//                    // 不可拖拽（包括 不可拖拽到其他地方 或者 其他地方拖拽到 这里 ）
//                    return false;
//                }else{

                    if (fromPosition < toPosition) {
                        for (int i = fromPosition; i < toPosition; i++) {
                            Collections.swap(list, i, i + 1);
                        }
                    } else {
                        for (int i = fromPosition; i > toPosition; i--) {
                            Collections.swap(list, i, i - 1);
                        }
                    }
                    mAdapter.notifyItemMoved(fromPosition, toPosition);
                    return true;

//                }

            }

            @Override
            public void onSwiped(RecyclerView.ViewHolder viewHolder, int actionState) {
        /*
        ACTION_STATE_IDLE：闲置状态
        ACTION_STATE_SWIPE：滑动状态
        ACTION_STATE_DRAG：拖拽状态*/

                if (actionState != ItemTouchHelper.ACTION_STATE_IDLE) {
                    viewHolder.itemView.setBackgroundColor(Color.LTGRAY);
                }
                super.onSelectedChanged(viewHolder, actionState);
            }


            /**
             * 手指松开的时候还原
             *
             * @param recyclerView
             * @param viewHolder
             */
            @Override
            public void clearView(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder) {
                super.clearView(recyclerView, viewHolder);
                viewHolder.itemView.setBackgroundColor(Color.WHITE);
//                mEditFinishTabList = datas;  // 得到重新排序后最终的元素集合
                mAdapter.addAll(list);

                // 监听 结束拖拽：
     /*    mTvTabEdit.setTextColor(getResources().getColor(R.color.colorAccent));
        mTabHint.setText(mChannelHint);
        mTvTabEdit.setText(mEdit);*/

            }

        });
        //必须绑定RecycleView
        helper.attachToRecyclerView(mRecycleView);
        final View mHeaderView = LayoutInflater.from(getActivity()).inflate(R.layout.header_view, mRecycleView, false);
        mRecycleView.addHeaderView(mHeaderView);
        mRecycleView.setGridLayoutSpanSizeLookUp(2);
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