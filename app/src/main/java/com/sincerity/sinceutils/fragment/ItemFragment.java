package com.sincerity.sinceutils.fragment;

import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.sincerity.sinceutils.R;

/**
 * Created by Sincerity on 2019/9/6.
 * 描述：
 */
public class ItemFragment extends Fragment {
    private static String tag = "ITEMTAG";
    private String mText;
    private View mView;

    TextView mTextView;

    public static Fragment newInstance(String item) {
        ItemFragment  mFragment = new ItemFragment();
        Bundle bundle = new Bundle();
        bundle.putString(tag, item);
        mFragment.setArguments(bundle);
        return mFragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mText = getArguments().getString(tag);
        }

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mView = LayoutInflater.from(getActivity()).inflate(R.layout.fragment_item, container, false);
        mTextView = mView.findViewById(R.id.item_text);
        mTextView.setText(mText);
        return mView;
    }

}
