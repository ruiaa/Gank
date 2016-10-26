package com.ruiaa.gank.model;

import android.support.annotation.NonNull;

import java.util.Date;

/**
 * Created by ruiaa on 2016/10/21.
 */

public class Gank {

    public long id;
    public String type;
    public String url;
    public String who;
    public String desc;
    public boolean used;
    public Date createdAt;
    public Date updatedAt;
    public Date publishedAt;

    public String label;

    public Gank replaceDesc(@NonNull Gank another){
        this.desc=another.desc;
        return this;
    }
}
