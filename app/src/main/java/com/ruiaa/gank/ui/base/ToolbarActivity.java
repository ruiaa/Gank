package com.ruiaa.gank.ui.base;

import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.Toolbar;
import android.util.TypedValue;
import android.view.MenuItem;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.DecelerateInterpolator;

import com.ruiaa.gank.R;

import java.lang.reflect.Field;

/**
 * Created by ruiaa on 2016/10/21.
 */

public abstract class ToolbarActivity extends BaseActivity {

    private AppBarLayout appBarLayout;
    private Toolbar toolbar;
    protected boolean toolbarIsHidden = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onContentChanged() {
        super.onContentChanged();
        initToolbar();
    }

    /*
     * toolbar
     */
    protected void initToolbar() {
        appBarLayout = (AppBarLayout) findViewById(R.id.app_bar_layout);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        if (appBarLayout == null || toolbar == null) {
            return;
        }
        toolbar.setOnClickListener(v -> onClickToolbar());
        setSupportActionBar(toolbar);
        if (canTurnBack()) {
            ActionBar actionBar = getSupportActionBar();
            if (actionBar != null) actionBar.setDisplayHomeAsUpEnabled(true);
        }
        if (Build.VERSION.SDK_INT >= 21) {
            appBarLayout.setElevation(10.6f);
        }
    }

    protected void onClickToolbar() {
    }

    protected void hideOrShowToolbar() {
        appBarLayout.animate()
                .translationY(toolbarIsHidden ? 0 : -appBarLayout.getHeight())
                .setInterpolator(new DecelerateInterpolator(2))
                .start();
        toolbarIsHidden = !toolbarIsHidden;
    }

    public void setTitle(String s) {
        super.setTitle(s);
    }

    protected void setAppBarAlpha(float alpha) {
        appBarLayout.setAlpha(alpha);
    }




    public void setStatusBarTranslucent() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            Window window = getWindow();
            window.setFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS, WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            //window.setFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION, WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
            //设置根布局的内边距
            //window.getDecorView().setPadding(0,getStatusBarHeight(), 0, 0);
            //appBarLayout.setPadding(0,getStatusBarHeight(),0,0);

            // 创建TextView
            // 获得根视图并把TextView加进去。
/*          TextView textView = new TextView(this);
            LinearLayout.LayoutParams lParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, getStatusBarHeight());
            textView.setBackgroundColor(ResUtil.getColor(R.color.colorPrimary));
            textView.setLayoutParams(lParams);
            appBarLayout.getParent().addView(textView,0);*/
        }
    }

    // 获取手机状态栏高度
    public int getStatusBarHeight() {
        Class<?> c = null;
        Object obj = null;
        Field field = null;
        int x = 0, statusBarHeight = 0;
        try {
            c = Class.forName("com.android.internal.R$dimen");
            obj = c.newInstance();
            field = c.getField("status_bar_height");
            x = Integer.parseInt(field.get(obj).toString());
            statusBarHeight = getResources().getDimensionPixelSize(x);
        } catch (Exception e1) {
            e1.printStackTrace();
        }
        return statusBarHeight;
    }

    // 获取ActionBar的高度
    public int getActionBarHeight() {
        TypedValue tv = new TypedValue();
        int actionBarHeight = 0;
        if (getTheme().resolveAttribute(android.R.attr.actionBarSize, tv, true))// 如果资源是存在的、有效的
        {
            actionBarHeight = TypedValue.complexToDimensionPixelSize(tv.data, getResources().getDisplayMetrics());
        }
        return actionBarHeight;
    }



    /*
     * 判断是否显示左上角的返回箭头
     */
    protected boolean canTurnBack() {
        return false;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
            return true;
        } else {
            return super.onOptionsItemSelected(item);
        }
    }

}
