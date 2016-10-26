package com.ruiaa.gank.http;


import com.ruiaa.gank.GankConfig;
import com.ruiaa.gank.model.CategoryData;
import com.ruiaa.gank.model.DayData;

import retrofit2.http.GET;
import retrofit2.http.Path;
import rx.Observable;

/**
 * Created by ruiaa on 2016/10/21.
 */

public interface GankApi {

    //http://gank.io/api/day/2016/10/21
    @GET("day/{year}/{month}/{day}")
    Observable<DayData> getGankData(
            @Path("year") int year,
            @Path("month") int month,
            @Path("day") int day
    );

    //分类
    //福利 | Android | iOS | 休息视频 | 拓展资源 | 前端 | all
    @GET("data/{category}/"+GankConfig.SIZE_CATEGORY_EVERY_PAGE+"/{page}")
    Observable<CategoryData> getCategoryData(@Path("category")String category, @Path("page")int page);

    @GET("data/福利/"+ GankConfig.SIZE_MEIZI_EVERY_PAGE+"/{page}")
    Observable<CategoryData> getMeiziData(@Path("page")int page);

    @GET("data/福利/1/1")
    Observable<CategoryData> getFirstMeiziData();

    @GET("data/休息视频/"+GankConfig.SIZE_CATEGORY_EVERY_PAGE+"/{page}")
    Observable<CategoryData> getVideoData(@Path("page")int page);

    @GET("data/Android/"+GankConfig.SIZE_CATEGORY_EVERY_PAGE+"/{page}")
    Observable<CategoryData> getAndroidData(@Path("page")int page);

    @GET("data/iOS/"+GankConfig.SIZE_CATEGORY_EVERY_PAGE+"/{page}")
    Observable<CategoryData> getiOSData(@Path("page")int page);

    @GET("data/前端/"+GankConfig.SIZE_CATEGORY_EVERY_PAGE+"/{page}")
    Observable<CategoryData> getFrontData(@Path("page")int page);

    @GET("data/拓展资源/"+GankConfig.SIZE_CATEGORY_EVERY_PAGE+"/{page}")
    Observable<CategoryData> getExpandResData(@Path("page")int page);

    @GET("data/瞎推荐/"+GankConfig.SIZE_CATEGORY_EVERY_PAGE+"/{page}")
    Observable<CategoryData> getRecommendData(@Path("page")int page);

    @GET("data/App/"+GankConfig.SIZE_CATEGORY_EVERY_PAGE+"/{page}")
    Observable<CategoryData> getAppData(@Path("page")int page);

    @GET("data/all/"+GankConfig.SIZE_CATEGORY_EVERY_PAGE+"/{page}")
    Observable<CategoryData> getAllData(@Path("page")int page);

}
