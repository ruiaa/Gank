package com.ruiaa.gank.util;

import android.content.Context;
import android.widget.Toast;

/**
 * Created by chenji on 2016/9/29.
 */

public class ToastUtil {

    private static Context context;

    public static void register(Context appContext) {
        context = appContext.getApplicationContext();
    }

    public static void showShort(int resId) {
        Toast.makeText(context, resId, Toast.LENGTH_SHORT).show();
    }


    public static void showShort(String message) {
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
    }


    public static void showLong(int resId) {
        Toast.makeText(context, resId, Toast.LENGTH_LONG).show();
    }


    public static void showLong(String message) {
        Toast.makeText(context, message, Toast.LENGTH_LONG).show();
    }


    public static void showLongX2(String message) {
        showLong(message);
        showLong(message);
    }


    public static void showLongX2(int resId) {
        showLong(resId);
        showLong(resId);
    }

    public static void showLongXn(String message,int count) {
        for(;count>0;count--) {
            showLong(message);
        }
    }

    public static void showLongXn(int resId,int count) {
        for(;count>0;count--) {
            showLong(resId);
        }
    }
}
