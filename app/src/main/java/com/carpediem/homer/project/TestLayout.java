package com.carpediem.homer.project;

import android.content.Context;
import android.support.v4.widget.ViewDragHelper;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.LinearLayout;

/**
 * Created by homer on 16-7-24.
 */
public class TestLayout extends LinearLayout {
    private ViewDragHelper mDragger;
    public TestLayout(Context context) {
        super(context);
        init(context);
    }

    public TestLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public TestLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }
    private void init(Context context) {
        mDragger = ViewDragHelper.create(this,1.0f,new ViewDragHelper.Callback() {
            @Override
            public boolean tryCaptureView(View child, int pointerId) {
                return true;
            }

            @Override
            public int clampViewPositionHorizontal(View child, int left, int dx) {
                return left;
            }

            @Override
            public int clampViewPositionVertical(View child, int top, int dy) {
                return top;
            }

            @Override
            public int getViewHorizontalDragRange(View child) {
                return 0;
            }

            @Override
            public int getViewVerticalDragRange(View child) {
                return 0;
            }
        });
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        Log.e("test","onInterceptTouchEvent");
        boolean result = false;
        switch (ev.getAction()) {
            case MotionEvent.ACTION_MOVE:
                result = mDragger.shouldInterceptTouchEvent(ev);
            default:

        }
        boolean ok = mDragger.shouldInterceptTouchEvent(ev);
        return ok;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        Log.e("test","onTouchEvent");
        mDragger.processTouchEvent(event);
        return true;
    }
}
