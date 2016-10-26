package com.ruiaa.gank.model;

import android.support.annotation.NonNull;

import java.util.List;

/**
 * Created by ruiaa on 2016/10/21.
 */

public class CategoryData {
    public boolean error;
    public List<Gank> results;

    public CategoryData replaceDesc(@NonNull CategoryData another){
        int size=Math.min(this.results.size(),another.results.size());
        for (int i = 0; i < size; i++) {
            this.results.get(i).desc = another.results.get(i).desc;
        }
        return this;
    }
}
