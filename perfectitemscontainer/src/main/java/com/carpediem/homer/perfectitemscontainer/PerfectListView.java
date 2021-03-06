package com.carpediem.homer.perfectitemscontainer;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ValueAnimator;
import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

/**
 * Created by homer on 16-7-22.
 */
public class PerfectListView extends ListView {

    int PULL_DOWN = 1;
    int PENDING_REFRESH = 2;
    int REFRESH = 3;
    int DONE = 4;
    /**
     * 功能:1 上拉加载，下拉刷新
     * 功能:2 HeaderView的zoom效果
     */
    private float mMaxPullDownDistance = 100;
    private float mTouchSlop;
    private View mDropView;
    private DropDownStateListener mStateListener;

    private int mDropDownState = DONE;
    private boolean mIsRecord = false;
    private DropDownManager mDropDownManager;
    private int mOriginDropDownViewHeight;
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
        ViewConfiguration configuration = ViewConfiguration.get(context);
        mTouchSlop = configuration.getScaledTouchSlop();
        setClipToPadding(true);
        NormalDropDownView dropView = new NormalDropDownView(context);
        dropView.setText("dddd");
        mDropView = dropView;
        measureView(dropView);
        mOriginDropDownViewHeight = dropView.getMeasuredHeight();
        mDropView.setPadding(0,-1*mOriginDropDownViewHeight,0,0);
        addHeaderView(mDropView);
        //这个时候应该还乜有meausre,layout呢。
    }

    private void measureView(View view) {
        ViewGroup.LayoutParams params = view.getLayoutParams();
        if (params == null) {
            params = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                        ViewGroup.LayoutParams.WRAP_CONTENT);
        }
        int widthSpec = ViewGroup.getChildMeasureSpec(0,0,params.width);
        int tmHeight = params.height;
        int mode = MeasureSpec.EXACTLY;
        if (tmHeight <= 0) {
            tmHeight = 0;
            mode = MeasureSpec.UNSPECIFIED;
        }
        int childHeightSpec = MeasureSpec.makeMeasureSpec(tmHeight,mode);
        view.measure(widthSpec,childHeightSpec);
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        return super.onInterceptTouchEvent(ev);
    }

    private float mLastY;
    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        int action = ev.getActionMasked();
        Log.e("test",action+"");
        boolean skipProcess = mDropDownState == REFRESH ||
                (action != MotionEvent.ACTION_DOWN && !mIsRecord);
        if (skipProcess) {
            return super.onTouchEvent(ev);
        }
        switch (action) {
            case MotionEvent.ACTION_DOWN:
                ev.getY();
                mLastY = ev.getY();
                mIsRecord = true;
                break;
            case MotionEvent.ACTION_MOVE:
                float y = ev.getY();
                handleMoveAction(y);
                break;
            case MotionEvent.ACTION_CANCEL:
                cancel();
                break;
            case MotionEvent.ACTION_UP:
                Log.e("test","action up "+mDropDownState+"");
                if (mDropDownState == PENDING_REFRESH) {
                    mDropDownState = REFRESH;
                    processRefreshAction();
                } else if (mDropDownState == PULL_DOWN) {
                    //返回动画
                    mDropDownState = DONE;
                    startSpringBackAnimation();
                }
                break;
        }
        return super.onTouchEvent(ev);
    }
    private void handleMoveAction(float scrollY) {
        if (Math.abs(scrollY - mLastY) > mTouchSlop) {
            int padding = mDropView.getPaddingTop();
            int newPaddingTop = (int)(padding + scrollY - mLastY);
            if (newPaddingTop > mMaxPullDownDistance) {
                tryPendingRefresh();
            } else {
                mDropDownState = PULL_DOWN;
            }
            mDropView.setPadding(0, newPaddingTop, 0, 0);
            mLastY = scrollY;
        }
    }
    private void tryPendingRefresh() {
        if (mDropDownState == PENDING_REFRESH) {
            return;
        }
        Log.e("test","tryPendingRefresh");
        ((TextView)mDropView).setText("释放刷新");
        mDropDownState = PENDING_REFRESH;
        if(checkStateListener()) {
            mStateListener.onScrollOver();
        }
    }
    private void processRefreshAction() {
        Log.e("test","processRefreshAction");
        if (checkStateListener()) {
            mStateListener.onRefresh();

        }
        ((TextView)mDropView).setText("正在刷新");
        postDelayed(new Runnable() {
            @Override
            public void run() {
                finishRefresh();
            }
        },1000);
    }
    public void finishRefresh() {
        Log.e("test","finishRefresh");
        startSpringBackAnimation();
    }

    private void startSpringBackAnimation() {
        // padding占height的，所以，你一直改变padding,导致header变大啦...
        int toValue = -1 * mOriginDropDownViewHeight;
        int fromValue = mDropView.getPaddingTop();
        Log.e("test","startSpringBackAnimation from "+fromValue + " to "+toValue);
        if (fromValue < toValue) {
            //数据流有问题偶，使用状态机模型应该可以查看出这种错误。
            Log.e("test","startSpringBackAnimation return");
            mDropDownState = DONE;
            mIsRecord = false;
            return;
        }

        ValueAnimator animation = ValueAnimator.ofInt(fromValue,toValue);
        animation.setDuration(1000);
        animation.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                int paddingTop = (int)animation.getAnimatedValue();
                mDropView.setPadding(0,paddingTop,0,0);
            }
        });
        animation.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                finishDropDown();
            }
        });
        animation.start();
    }

    private void finishDropDown() {
        if (checkStateListener()) {
            mStateListener.onFinish();
        }
        Log.e("test","finishDropDown");
        mDropDownState = DONE;
        ((TextView)mDropView).setText("下拉刷新");
    }
    private void cancel() {

    }
    private boolean checkStateListener() {
        return mStateListener != null;
    }
    public void setDropDownStateListener(DropDownStateListener listener) {
        if (listener == null) {
            return;
        }
        mStateListener = listener;
    }

    public interface DropDownStateListener {
        void onScrollDown();
        void onScrollOver();
        void onSpringBack();
        void onRefresh();
        void onFinish();
    }
}
