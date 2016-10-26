package com.ruiaa.gank.ui.base;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.ruiaa.gank.util.RxManager;

import rx.Subscription;

/**
 * Created by ruiaa on 2016/10/21.
 */

public class BaseActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initTheme();
    }



    /*
     *
     * 设置主题
     *
     */
    public void initTheme(){

    }

    public void saveThemeChoose(){

    }



    /*
     *
     * 管理RxJava的subscription的释放
     *
     */
    private RxManager rxManager=new RxManager();

    public void addSubscription(Subscription s){
        rxManager.add(s);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        rxManager.clear();
    }
}
