package com.carpediem.homer.perfectitemscontainer;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.ListView;

/**
 * Created by homer on 16-7-22.
 */
public class PerfectListView extends ListView {
    /**
     * 功能:1 上拉加载，下拉刷新
     * 功能:2 HeaderView的zoom效果
     */

    /**
     *
     * @param context
     */
    public PerfectListView(Context context) {
        super(context);
    }

    public PerfectListView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public PerfectListView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    private void init(Context context) {
        //设置下拉刷新等view

    }


    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        return super.onTouchEvent(ev);
    }

}
