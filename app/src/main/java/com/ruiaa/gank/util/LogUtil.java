package com.ruiaa.gank.util;


import android.util.Log;

import com.ruiaa.gank.BuildConfig;

/**
 * Created by ruiaa on 2016/9/29.
 */

public class LogUtil {

    public static final String USER_NAME="ruiaa---" ;
    public static final boolean LOG_OPEN = BuildConfig.LOG_OPEN;

    public static void d(String msg){
        if (LOG_OPEN){
            Log.d(USER_NAME+getClassName(),msg);
        }
    }

    public static void i(String msg){
        if (LOG_OPEN){
            Log.i(USER_NAME+getClassName(),msg);
        }
    }

    public static void w(String msg){
        if (LOG_OPEN){
            Log.w(USER_NAME+getClassName(),msg);
        }
    }

    public static void e(String msg){
        if (LOG_OPEN){
            Log.e(USER_NAME+getClassName(),msg);
        }
    }

    public static void w(String msg ,Exception e){
        if (LOG_OPEN){
            Log.w(USER_NAME+getClassName(),msg+e.getMessage());
        }
    }

    public static void e(String msg ,Exception e){
        if (LOG_OPEN){
            Log.e(USER_NAME+getClassName(),msg+e.getMessage());
            e.printStackTrace();
        }
    }

    public static void e(String msg ,Throwable throwable){
        if (LOG_OPEN){
            Log.e(USER_NAME+getClassName(),msg+throwable.getMessage());
            throwable.printStackTrace();
        }
    }

    /**
     * @return 当前的类名(simpleName)
     */
    private static String getClassName() {

        String result;
        StackTraceElement thisMethodStack = Thread.currentThread().getStackTrace()[4];
        result = thisMethodStack.getClassName();
        int lastIndex = result.lastIndexOf(".");
        result = result.substring(lastIndex + 1);

        int i = result.indexOf("$");// 剔除匿名内部类名
        return i == -1 ? result : result.substring(0, i);
    }

    /**
     * 打印 Log 行数位置
     */
    private static String log(String message) {
        StackTraceElement[] stackTrace = Thread.currentThread().getStackTrace();
        StackTraceElement targetElement = stackTrace[5];
        String className = targetElement.getClassName();
        className = className.substring(className.lastIndexOf('.') + 1) + ".java";
        int lineNumber = targetElement.getLineNumber();
        if (lineNumber < 0) lineNumber = 0;
        return "(" + className + ":" + lineNumber + ") " + message;
    }
}
