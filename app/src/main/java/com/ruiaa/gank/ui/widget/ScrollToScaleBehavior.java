package com.ruiaa.gank.ui.widget;

import android.content.Context;
import android.support.design.widget.CoordinatorLayout;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.View;
import android.widget.LinearLayout;

import com.ruiaa.gank.R;
import com.ruiaa.gank.util.LogUtil;

import static android.support.v4.view.ViewCompat.SCROLL_AXIS_VERTICAL;

/**
 * Created by ruiaa on 2016/10/26.
 */

public class ScrollToScaleBehavior extends CoordinatorLayout.Behavior<View> {

    private int targetId = -1;
    private View targetView = null;
    private RecyclerView scrollView=null;
    private boolean first = true;
    private int targetHeight = 0;
    private LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(0, 0);

    public ScrollToScaleBehavior(Context context, AttributeSet attrs) {
        super(context, attrs);
        /*TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.ScrollToScaleBehavior);
        targetId = a.getResourceId(R.styleable.ScrollToScaleBehavior_targetView, -1);
        a.recycle();*/
        targetId = R.id.day_video_layout;
    }

    @Override
    public boolean onStartNestedScroll(CoordinatorLayout coordinatorLayout, View child, View directTargetChild, View target, int nestedScrollAxes) {
        //return super.onStartNestedScroll(coordinatorLayout, child, directTargetChild, target, nestedScrollAxes);
        if (targetId == -1) {
            return false;
        }
        if (first) {
            first = false;
            targetView = child.findViewById(targetId);
            scrollView=(RecyclerView)target;
            layoutParams.width = targetView.getWidth();
            targetHeight = targetView.getHeight();
        }
        if (targetView == null) {
            return false;
        } else {
            return nestedScrollAxes == SCROLL_AXIS_VERTICAL;
        }
    }

    @Override
    public void onNestedPreScroll(CoordinatorLayout coordinatorLayout, View child, View target, int dx, int dy, int[] consumed) {
        super.onNestedPreScroll(coordinatorLayout, child, target, dx, dy, consumed);
        int h = targetView.getHeight() - dy;
        if (dy > 0) {
            layoutParams.height = h > 0 ? h : 0;
        } else {
            layoutParams.height = h < targetHeight ? h : targetHeight;
        }
        targetView.setLayoutParams(layoutParams);
    }

    @Override
    public void onNestedScroll(CoordinatorLayout coordinatorLayout, View child, View target, int dxConsumed, int dyConsumed, int dxUnconsumed, int dyUnconsumed) {
        super.onNestedScroll(coordinatorLayout, child, target, dxConsumed, dyConsumed, dxUnconsumed, dyUnconsumed);
        LogUtil.i("onNestedScroll--dyConsumed"+dyConsumed);
        LogUtil.i("onNestedScroll--dyUnconsumed"+dyUnconsumed);
    }

    @Override
    public boolean onNestedPreFling(CoordinatorLayout coordinatorLayout, View child, View target, float velocityX, float velocityY) {
        return super.onNestedPreFling(coordinatorLayout, child, target, velocityX, velocityY);
    }
}
