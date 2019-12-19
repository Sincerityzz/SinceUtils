package com.sincerity.lib;

/**
 * Created by Sincerity on 2019/12/13.
 * 描述：模拟事件
 */
public class Activity {
    public static void main(String[] args) {
        ViwGroup view = new ViwGroup(0, 0, 1080, 1920);
        view.setName("顶级容器");
        ViwGroup viwGroup = new ViwGroup(0, 0, 500, 500);
        viwGroup.setName("第二容器");
        View view1 = new View(0, 0, 200, 200);
        viwGroup.addView(view1);
        view.addView(viwGroup);

        //添加事件
        view.setmOnTouchListener((view23, event) -> {
            System.out.println("顶级容器的onTouch");
            return false;
        });
        viwGroup.setmOnTouchListener((view22, event) -> {
            System.out.println("第二容器的onTouch");
            return false;
        });
        view1.setmOnTouchListener((view2, event) -> {
            System.out.println("view1的onTouch");
            return true;
        });
        view1.setmOnClickListener(view24 -> {
            System.out.println("view1的onClick");
            return false;
        });
        MotionEvent event = new MotionEvent(100, 100);
        event.setActionMasked(MotionEvent.ACTION_DOWN);
        view.dispatchTouchEvent(event);
    }
}
