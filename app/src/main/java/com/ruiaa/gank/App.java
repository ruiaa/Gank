package com.ruiaa.gank;

import android.app.Application;
import android.content.Context;

import com.ruiaa.gank.util.ConvertUtil;
import com.ruiaa.gank.util.ResUtil;
import com.ruiaa.gank.util.StringStyles;
import com.ruiaa.gank.util.ToastUtil;

/**
 * Created by ruiaa on 2016/10/21.
 */

public class App extends Application {

    public static Context appContext;

    @Override
    public void onCreate() {
        super.onCreate();
        appContext=this;

        //util
        ToastUtil.register(appContext);
        ResUtil.register(appContext);
        StringStyles.register(appContext);
        ConvertUtil.register(appContext);
    }
}
