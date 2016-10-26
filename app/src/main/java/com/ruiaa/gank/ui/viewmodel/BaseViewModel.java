package com.ruiaa.gank.ui.viewmodel;

import com.ruiaa.gank.util.RxManager;

import rx.Subscription;

/**
 * Created by ruiaa on 2016/10/22.
 */

public abstract class BaseViewModel {

    private RxManager rxManager=new RxManager();

    public void addSubscription(Subscription s){
        rxManager.add(s);
    }

    public void destroy() {
        rxManager.clear();
    }

}
