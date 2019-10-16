package com.sincerity.sinceutils.fragment;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.AppCompatTextView;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.sincerity.sinceutils.R;
import com.sincerity.sinceutils.utils.AppUtils;
import com.sincerity.utilslibrary.view.indicator.ColorTrackTextView;
import com.sincerity.utilslibrary.view.indicator.IndicatorAdapter;
import com.sincerity.utilslibrary.view.indicator.TrackIndicatorView;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * Created by Sincerity on 2019/9/6.
 * 描述：
 */
public class HomePage extends Fragment {
    private View mView;
    private TrackIndicatorView mIndicatorContainer;

    private ViewPager mViewPager;
    private AppCompatButton mBtnIMEI;
    private AppCompatTextView mTvShow;
    private String[] items = {"直播", "推荐", "直播", "推荐", "直播", "推荐", "直播", "推荐", "直播", "推荐"};
    private List<ColorTrackTextView> mIndicators;
    private String TAG = "HomePage";

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mView = LayoutInflater.from(getActivity()).inflate(R.layout.fragment_home, container, false);
        mViewPager = mView.findViewById(R.id.mViewPager);
        mIndicatorContainer = mView.findViewById(R.id.indicator_view);
        mBtnIMEI = mView.findViewById(R.id.btnIMEI);
        mTvShow = mView.findViewById(R.id.tv_show);
        mBtnIMEI.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TelephonyManager telephonyManager = (TelephonyManager) getActivity().getSystemService(Context.TELEPHONY_SERVICE);
                //获取IMEI号
                if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
                    // TODO: Consider calling
                    //    ActivityCompat#requestPermissions
                    // here to request the missing permissions, and then overriding
                    //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                    //                                          int[] grantResults)
                    // to handle the case where the user grants the permission. See the documentation
                    // for ActivityCompat#requestPermissions for more details.
                    return;
                }
                String imei = telephonyManager.getDeviceId();
                mTvShow.setText("IMEI" + getDeviceId(getActivity()) + "\n" +
                        "IMEI" + imei + "\n"
                        + "IMEI" + (new AppUtils(getActivity()).getUniqueID()));
            }
        });
        mIndicators = new ArrayList<>();
        initIndicator();
        initViewPager();
        return mView;
    }

    @SuppressLint("HardwareIds")
    public static String getDeviceId(Context mContext) {
        String m_szDevIDShort = "35" + Build.BOARD.length() % 10
                + Build.BRAND.length() % 10 + Build.CPU_ABI.length() % 10
                + Build.DEVICE.length() % 10 + Build.DISPLAY.length() % 10
                + Build.HOST.length() % 10 + Build.ID.length() % 10
                + Build.MANUFACTURER.length() % 10 + Build.MODEL.length() % 10
                + Build.PRODUCT.length() % 10 + Build.TAGS.length() % 10
                + Build.TYPE.length() % 10 + Build.USER.length() % 10;// 13 位

        String serial = "serial";// 默认serial可随便定义
        try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                if (ActivityCompat.checkSelfPermission(mContext,
                        Manifest.permission.READ_PHONE_STATE) == PackageManager.PERMISSION_GRANTED) {
                    // 由于 Android Q 唯一标识符权限的更改会导致
                    // android.os.Build.getSerial() 返回 unknown,
                    // 但是 m_szDevIDShort 是由硬件信息拼出来的，所以仍然保证了UUID 的唯一性和持久性。
                    serial = android.os.Build.getSerial();// Android Q 中返回 unknown
                }
            } else {
                serial = Build.SERIAL;
            }
        } catch (Exception ignored) {
        }
        return new UUID(m_szDevIDShort.hashCode(), serial.hashCode()).toString();
    }

    /**
     * 初始化ViewPager
     */
    private void initViewPager() {
        mViewPager.setAdapter(new FragmentPagerAdapter(getChildFragmentManager()) {
            @Override
            public Fragment getItem(int position) {
                return ItemFragment.newInstance(items[position]);
            }

            @Override
            public int getCount() {
                return items.length;
            }

            @Override
            public void destroyItem(ViewGroup container, int position, Object object) {

            }
        });
        // 默认一进入就选中第一个
        ColorTrackTextView left = mIndicators.get(0);
        left.setDirection(ColorTrackTextView.Direction.DIRECTION_LEFT);
        left.setCurrentProgress(1);
        /**
         * 添加一个切换的监听那个setOnPageChangeListener过时了
         * 这个看源码去吧
         */
        mViewPager.addOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset,
                                       int positionOffsetPixels) {
                Log.e(TAG, "position --> " + position + " positionOffset --> " + positionOffset);
                if (positionOffset > 0) {
                    // 获取左边
                    ColorTrackTextView left = mIndicators.get(position);
                    // 设置朝向
                    left.setDirection(ColorTrackTextView.Direction.DIRECTION_RIGHT);
                    // 设置进度  positionOffset 是从 0 一直变化到 1 不信可以看打印
                    left.setCurrentProgress(1 - positionOffset);
                    // 获取右边
                    ColorTrackTextView right = mIndicators.get(position + 1);
                    right.setDirection(ColorTrackTextView.Direction.DIRECTION_LEFT);
                    right.setCurrentProgress(positionOffset);
                }
            }
        });
    }

    /**
     * 初始化可变色的指示器
     */
    private void initIndicator() {
//        for (int i = 0; i < items.length; i++) {
//            // 动态添加颜色跟踪的TextView
//            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
//                    ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
//            params.weight = 1;
//            ColorTrackTextView colorTrackTextView = new ColorTrackTextView(getActivity());
//            // 设置两种颜色
//            colorTrackTextView.setFontColor(Color.BLACK);
//            colorTrackTextView.setBehindColor(Color.GREEN);
//            colorTrackTextView.setText(items[i]);
//            colorTrackTextView.setLayoutParams(params);
//            // 把新的加入LinearLayout容器
//            mIndicatorContainer.addView(colorTrackTextView);
//            // 加入集合
//            mIndicators.add(colorTrackTextView);
//        }
        mIndicatorContainer.setAdapter(new IndicatorAdapter<ColorTrackTextView>() {
            @Override
            public int getCount() {
                return items.length;
            }

            @Override
            public ColorTrackTextView getView(int position, ViewGroup convertView) {
                ColorTrackTextView colorTrackTextView = new ColorTrackTextView(getActivity());
                // 设置两种颜色
                colorTrackTextView.setFontColor(Color.BLACK);
                colorTrackTextView.setBehindColor(Color.GREEN);
                colorTrackTextView.setText(items[position]);
                mIndicators.add(colorTrackTextView);
                return colorTrackTextView;
            }

            @Override
            public void highlightTheCurrent(ColorTrackTextView view) {
                view.setDirection(ColorTrackTextView.Direction.DIRECTION_RIGHT);
                view.setCurrentProgress(1);
//                ColorTrackTextView colorTrackTextView = new ColorTrackTextView(getActivity());
//                colorTrackTextView.setBehindColor(Color.GREEN);
            }

            @Override
            public void resetColor(ColorTrackTextView view) {
                view.setDirection(ColorTrackTextView.Direction.DIRECTION_RIGHT);
                view.setCurrentProgress(0);
            }

            @Override
            public View getTrackView() {
                View view = new View(getActivity());
                view.setBackgroundColor(Color.RED);
                view.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,3));
                return view;
            }
        }, mViewPager,false);
    }
}
