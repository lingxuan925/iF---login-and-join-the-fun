package com.example.lingxuan925.anif;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;


public class MyViewPager extends ViewPager {

    int preX = 0;

    public MyViewPager(Context context) {
        super(context);
    }

    public MyViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public void setCurrentItem(int item) {
        super.setCurrentItem(item, false);
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent event) {

        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            preX = (int) event.getX();
        } else {
            if (Math.abs((int) event.getX() - preX) > 10) {
                return true;
            } else {
                preX = (int) event.getX();
            }
        }
        return super.onInterceptTouchEvent(event);
    }

}
