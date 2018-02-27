package com.and.newsfeed.dao;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;

import com.and.newsfeed.data.NewsData;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by srishtic on 2/22/18.
 */

@Dao
public interface NewsDao {

    @Query("SELECT * FROM NewsData")
    List<NewsData> getAll();


    @Query("SELECT COUNT(*) from NewsData")
    int countCountries();

    @Insert
    void insertAll(NewsData... news);

    @Delete
    void delete(NewsData news);
}