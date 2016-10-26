package com.ruiaa.gank.ui.viewmodel;

import com.ruiaa.gank.http.GankApi;
import com.ruiaa.gank.http.GankRetrofit;
import com.ruiaa.gank.model.DayData;
import com.ruiaa.gank.model.Gank;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by ruiaa on 2016/10/23.
 */

public class DayModel extends BaseViewModel {

    private Date date;
    private final List<Gank> gankList;
    private GankApi gankApi;

    private String videoUrl;

    public DayModel(Date date) {
        this.date = date;
        gankList = new ArrayList<>();
        gankApi = GankRetrofit.getGankApi();
    }

    public Observable<String> loadGank() {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        return gankApi
                .getGankData(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH) + 1, calendar.get(Calendar.DAY_OF_MONTH))
                .subscribeOn(Schedulers.io())
                .map(dayData -> dayData.results)
                .observeOn(Schedulers.computation())
                .map(results -> {
                    if (results.videoList!=null&&!results.videoList.isEmpty()) {
                        videoUrl = results.videoList.get(0).url;
                    }
                    return DayData.resultToGankList(results);
                })
                .map(ganks -> {
                    setLabel(ganks);
                    gankList.addAll(ganks);
                    return "ok";
                })
                .observeOn(AndroidSchedulers.mainThread());
    }

    private void setLabel(List<Gank> ganks){
        if (ganks!=null&&!ganks.isEmpty()){
            int size=ganks.size();
            ganks.get(0).label=ganks.get(0).type;
            for (int i=0;i<size-1;i++){
                ganks.get(i+1).label= ganks.get(i).type.equals(ganks.get(i+1).type)
                        ? null:ganks.get(i+1).type;
            }
        }
    }

    public List<Gank> getGankList() {
        return gankList;
    }

    public String getVideoUrl() {
        return videoUrl;
    }
}
