package com.ruiaa.gank.model;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ruiaa on 2016/10/21.
 */

public class DayData {

    public boolean error;
    public Result results;
    public List<String> category;

    public class Result {
        @SerializedName("福利") public List<Gank> meiziList;
        @SerializedName("休息视频") public List<Gank> videoList;
        @SerializedName("Android") public List<Gank> androidList;
        @SerializedName("iOS") public List<Gank> iosList;
        @SerializedName("前端") public List<Gank> frontList;
        @SerializedName("拓展资源") public List<Gank> expandResList;
        @SerializedName("瞎推荐") public List<Gank> recommendList;
        @SerializedName("App") public List<Gank> appList;
    }

    public static List<Gank> resultToGankList(Result results) {
        List<Gank> mGankList=new ArrayList<>();
        if (results.androidList != null) mGankList.addAll(results.androidList);
        if (results.iosList != null) mGankList.addAll(results.iosList);
        if (results.frontList != null) mGankList.addAll(results.frontList);
        if (results.appList != null) mGankList.addAll(results.appList);
        if (results.expandResList != null) mGankList.addAll(results.expandResList);
        if (results.recommendList != null) mGankList.addAll(results.recommendList);
        if (results.videoList != null) mGankList.addAll(0, results.videoList);
        return mGankList;
    }
}
