package com.panshi.hujin2.iloan.view;

import android.content.Context;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.ViewConfiguration;

import com.panshi.hujin2.iloan.util.DebugLog;

/***
 * @brief 只在竖直方向才能下拉刷新的控件
 * Created by Administrator on 2018/8/4 0004.
 */

public class VerticalSwiprRefreshLayout extends SwipeRefreshLayout {
    private int mTouchSlop;
    // 上一次触摸时的X坐标
    private float mPrevX;
    private float mPrevY;

    public VerticalSwiprRefreshLayout(Context context) {
        super(context);
    }

    public VerticalSwiprRefreshLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        // 触发移动事件的最短距离，如果小于这个距离就不触发移动控件
        mTouchSlop = ViewConfiguration.get(context).getScaledTouchSlop();
    }
    @Override
    public boolean onInterceptTouchEvent(MotionEvent event) {

        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                mPrevX = event.getX();
                mPrevY = event.getY();
                break;

            case MotionEvent.ACTION_MOVE:
                final float eventX = event.getX();
                final float eventY = event.getY();
                float xDiff = Math.abs(eventX - mPrevX);
                float yDiff = Math.abs(eventY - mPrevY);
                // Log.d("refresh" ,"move----" + eventX + "   " + mPrevX + "   " + mTouchSlop);
                // 增加60的容差，让下拉刷新在竖直滑动时就可以触发
                DebugLog.i("wang","===xdiff||"+xDiff+"==="+"slop||"+mTouchSlop);
                if (xDiff > mTouchSlop&&yDiff<mTouchSlop+300) {
                    return false;
                }

        }

        return super.onInterceptTouchEvent(event);
    }
}
