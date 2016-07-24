package com.carpediem.homer.perfectitemscontainer;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ListView;

/**
 * Created by homer on 16-7-22.
 */
public class PerfectListView extends ListView {
    /**
     * 功能:1 上拉加载，下拉刷新
     * 功能:2 HeaderView的zoom效果
     */

    private View mDropView;
    /**
     *
     * @param context
     */
    public PerfectListView(Context context) {
        super(context);
        init(context);
    }

    public PerfectListView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public PerfectListView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context context) {
        //设置下拉刷新等view
        setClipToPadding(true);
        NormalDropDownView dropView = new NormalDropDownView(context);
        dropView.setText("dddd");
        mDropView = dropView;
        mDropView.setPadding(0,-1*mDropView.getMeasuredHeight(),0,0);
        addHeaderView(mDropView);
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        return super.onInterceptTouchEvent(ev);
    }

    private float mLastY;
    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        switch (ev.getActionMasked()) {
            case MotionEvent.ACTION_DOWN:
                ev.getY();
                mLastY = ev.getY();
                break;
            case MotionEvent.ACTION_MOVE:
                float y = ev.getY();
                if (y-mLastY > 10) {
                    Log.e("test","y is "+y);
                    int padding = mDropView.getPaddingTop();
                    int newPaddingTop = (int)(padding + y - mLastY);
                    mDropView.setPadding(0,newPaddingTop,0,0);
                    mLastY = y;
                }
                break;
            case MotionEvent.ACTION_UP:
                break;
        }
        return super.onTouchEvent(ev);
    }



}
