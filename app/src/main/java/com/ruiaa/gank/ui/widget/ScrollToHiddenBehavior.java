package com.ruiaa.gank.ui.widget;

import android.content.Context;
import android.support.design.widget.CoordinatorLayout;
import android.util.AttributeSet;
import android.view.View;

import com.ruiaa.gank.R;

import static android.support.v4.view.ViewCompat.SCROLL_AXIS_VERTICAL;

/**
 * Created by ruiaa on 2016/10/26.
 */

public class ScrollToHiddenBehavior extends CoordinatorLayout.Behavior<View> {

    int targetId = -1;
    View targetView = null;
    boolean first = true;
    float targetHeight=0;

    public ScrollToHiddenBehavior(Context context, AttributeSet attrs) {
        super(context, attrs);
        /*TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.ScrollToScaleBehavior);
        targetId = a.getResourceId(R.styleable.ScrollToScaleBehavior_targetView, -1);
        a.recycle();*/
        targetId= R.id.day_video_layout;
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
            targetHeight=targetView.getHeight();
        }
        return nestedScrollAxes == SCROLL_AXIS_VERTICAL;
    }

    @Override
    public void onNestedPreScroll(CoordinatorLayout coordinatorLayout, View child, View target, int dx, int dy, int[] consumed) {
        float y=child.getY()-dy;
        if (dy>0){
            if (y>-targetHeight){
                child.setY(y);
            }else {
                child.setY(targetHeight);
            }
        }else {
            if (y<0){
                child.setY(y);
            }else {
                child.setY(0);
            }
        }
    }

}
