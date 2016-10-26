package com.ruiaa.gank.http;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by ruiaa on 2016/10/21.
 */

public class GankRetrofit {

    private static final String HOST = "http://gank.io/api/";
    private static GankApi gankApi=null;

    private GankRetrofit(){
    }

    private static void createGankApi(){
        Gson gson=new GsonBuilder()
                .setDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'")
                .create();
        Retrofit retrofit=new Retrofit.Builder()
                .baseUrl(HOST)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .build();
        gankApi=retrofit.create(GankApi.class);
    }

    public static GankApi getGankApi(){
        if (gankApi==null){
            synchronized(GankRetrofit.class){
                if (gankApi==null){
                    createGankApi();
                }
            }
        }
        return gankApi;
    }
}
