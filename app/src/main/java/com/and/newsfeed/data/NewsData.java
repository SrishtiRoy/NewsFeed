package com.and.newsfeed.data;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;

/**
 * Created by srishtic on 2/22/18.
 */
/**
 * Created by magdamiu on 30/05/17.
 */

@Entity(tableName = "NewsData")
public class NewsData {
    @PrimaryKey(autoGenerate = true)
    private int uid;

    @ColumnInfo(name = "title")
    private String title;

    @ColumnInfo(name = "url")
    private String url;


    public int getUid() {
        return uid;
    }

    public void setUid(int uid) {
        this.uid = uid;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;

    }

    public String getUrl() {
        return title;
    }

    public void setUrl(String url) {
        this.title = url;

    }
}