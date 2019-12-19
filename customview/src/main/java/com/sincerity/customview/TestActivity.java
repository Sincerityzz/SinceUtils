package com.sincerity.customview;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.sincerity.customview.view.recycleview.RecycleView;

public class TestActivity extends AppCompatActivity {
    //    private WatchAndClockView view;
//    private Calendar calender;
    private TextView mTvShow;
    private RecycleView recycleView;
    Handler handler = new Handler() {
        @Override
        public void handleMessage(@NonNull Message msg) {
            super.handleMessage(msg); recycleView.setAdapter(new RecycleView.Adapter() {
                @Override
                public View onCreateViewHolder(int position, View convertView, ViewGroup parent) {
                    convertView = TestActivity.this.getLayoutInflater().inflate(R.layout.item_view, parent, false);
                    TextView textView = convertView.findViewById(R.id.tvTest);
                    textView.setText("di" + position + "行");
                    return convertView;
                }

                @Override
                public View onBindViewHolder(int position, View convertView, ViewGroup parent) {
                    TextView textView = convertView.findViewById(R.id.tvTest);
                    textView.setText("测试数据" + position);
                    return convertView;
                }

                @Override
                public int getItemViewType(int row) {
                    return 0;
                }

                @Override
                public int getItemViewTypeCount() {
                    return 1;
                }

                @Override
                public int getCount() {
                    return 300000;
                }

                @Override
                public int getLineHeight() {
                    return 200;
                }
            });
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);
        recycleView = findViewById(R.id.recycler);
        recycleView.setAdapter(new RecycleView.Adapter() {
            @Override
            public View onCreateViewHolder(int position, View convertView, ViewGroup parent) {
                convertView = TestActivity.this.getLayoutInflater().inflate(R.layout.item_view, parent, false);
                TextView textView = convertView.findViewById(R.id.tvTest);
                textView.setText("di" + position + "行");
                return convertView;
            }

            @Override
            public View onBindViewHolder(int position, View convertView, ViewGroup parent) {
                TextView textView = convertView.findViewById(R.id.tvTest);
                textView.setText("测试数据" + position);
                return convertView;
            }

            @Override
            public int getItemViewType(int row) {
                return 0;
            }

            @Override
            public int getItemViewTypeCount() {
                return 1;
            }

            @Override
            public int getCount() {
                return 30;
            }

            @Override
            public int getLineHeight() {
                return 200;
            }
        });handler.sendEmptyMessageDelayed(1,5000);
//        mTvShow = findViewById(R.id.id_showText);
//        ObjectAnimator animator = ObjectAnimator.ofFloat(mTvShow, "scaleX", 1f, 2f);
//        MyObjectAnimator anim = MyObjectAnimator.ofFloat(mTvShow, "scaleX", 1f, 2f, 5f);
//        anim.setInterpolator(new LinearInterpolator());
//        anim.setmDuration(5000);
//        anim.start();
//        MailListView view = findViewById(R.id.mail_view);
////        view = findViewById(R.id.step_view);
//        calender = Calendar.getInstance();
//        view.setTime(calender.get(Calendar.HOUR_OF_DAY), calender.get(Calendar.MINUTE), calender.get(Calendar.SECOND)).start();
//        view.setTime(calender.get(Calendar.HOUR_OF_DAY), calender.get(Calendar.MINUTE), calender.get(Calendar.SECOND)).start();
//       setRunningView();
//        view.setLetterTouchListener(new MailListView.LetterTouchListener() {
//            @Override
//            public void touch(CharSequence letter, boolean isTouch) {
//
//                if (isTouch) {
//                    mTvShow.setVisibility(View.VISIBLE);
//                    mTvShow.setText(letter);
//                } else {
//                    mTvShow.setVisibility(View.GONE);
//                }
//            }
//        });
    }

//        private void setRunningView() {
//        final CustomRunningView view = findViewById(R.id.step_view);
//        view.setStepMax(19999);
//        ValueAnimator animator = ObjectAnimator.ofFloat(0, 15368);
//        animator.setDuration(1500);
//        animator.setInterpolator(new DecelerateInterpolator()); //插值器
//        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
//            @Override
//            public void onAnimationUpdate(ValueAnimator animation) {
//                float value = (float) animation.getAnimatedValue();
//                view.setCurrentStep((int) value);
//            }
//        });
//        animator.start();
//    }
}
