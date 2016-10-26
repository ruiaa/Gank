package com.ruiaa.gank.ui.viewmodel;

import com.ruiaa.gank.http.GankApi;
import com.ruiaa.gank.http.GankRetrofit;
import com.ruiaa.gank.model.Category;
import com.ruiaa.gank.model.CategoryData;
import com.ruiaa.gank.model.Gank;
import com.ruiaa.gank.util.DateUtil;

import java.util.ArrayList;
import java.util.List;

import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by ruiaa on 2016/10/22.
 */

public class CategoryModel extends BaseViewModel {

    private GankApi gankApi;
    private List<Gank> gankList;
    private Category category;
    private int page;

    public CategoryModel(Category category) {
        this.category = category;
        gankApi = GankRetrofit.getGankApi();
        gankList = new ArrayList<>();
        page = 0;
    }

    public Observable<String> loadMore() {
        page = page + 1;
        return getCategoryData(page)
                .subscribeOn(Schedulers.io())
                .observeOn(Schedulers.computation())
                .map(categoryData -> {
                    setLabel(gankList.isEmpty()?null:gankList.get(gankList.size()-1),
                            categoryData.results);
                    gankList.addAll(categoryData.results);
                    return "ok";
                })
                .observeOn(AndroidSchedulers.mainThread());
    }

    public Observable<String> refresh() {
        if (gankList.isEmpty()){
            page=0;
            return loadMore();
        }else {
            return getCategoryData(0)
                    .subscribeOn(Schedulers.io())
                    .observeOn(Schedulers.computation())
                    .map(categoryData -> {
                        if (categoryData.results.get(0).url.equals(gankList.get(0).url)){
                            return "no";
                        }else {
                            gankList.add(0,categoryData.results.get(0));
                            return "yes";
                        }

                    })
                    .observeOn(AndroidSchedulers.mainThread());
        }
    }

    public List<Gank> getGankList() {
        return gankList;
    }

    private Observable<CategoryData> getCategoryData(int page) {
        switch (category) {
            case MEIZI: {
                return Observable.zip(
                        gankApi.getMeiziData(page),
                        gankApi.getVideoData(page),
                        (meiziList, videoList) -> {
                            return meiziList.replaceDesc(videoList);
                        });
            }
            case VIDEO: {
                return gankApi.getVideoData(page);
            }
            case ANDROID: {
                return gankApi.getAndroidData(page);
            }
            case IOS: {
                return gankApi.getiOSData(page);
            }
            case FRONT: {
                return gankApi.getFrontData(page);
            }
            case EXPAND: {
                return gankApi.getExpandResData(page);
            }
            case RECOMMEND: {
                return gankApi.getRecommendData(page);
            }
            case APP: {
                return gankApi.getAppData(page);
            }
            default: {
                return gankApi.getAndroidData(page);
            }
        }
    }

    private void setLabel(Gank last,List<Gank> ganks){
        if (ganks!=null&&!ganks.isEmpty()){
            int size=ganks.size();
            if (last!=null){
                ganks.get(0).label= DateUtil.isTheSameDay(last.publishedAt,ganks.get(0).publishedAt)
                        ? null:DateUtil.dateNoYear(ganks.get(0).publishedAt);
            }else {
                ganks.get(0).label=DateUtil.dateNoYear(ganks.get(0).publishedAt);
            }
            for (int i=0;i<size-1;i++){
                ganks.get(i+1).label= DateUtil.isTheSameDay(ganks.get(i).publishedAt,ganks.get(i+1).publishedAt)
                        ? null:DateUtil.dateNoYear(ganks.get(i+1).publishedAt);
            }
        }
    }
    @Override
    public void destroy() {
        super.destroy();
    }

}
