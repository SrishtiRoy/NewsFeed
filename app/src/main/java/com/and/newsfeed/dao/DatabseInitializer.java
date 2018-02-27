package com.and.newsfeed.dao;

import android.os.AsyncTask;
import android.support.annotation.NonNull;

import com.and.newsfeed.data.NewsData;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by srishtic on 2/22/18.
 */

public class DatabseInitializer {


    private static final String TAG = DatabseInitializer.class.getName();
   public static List<NewsData> newList=new ArrayList<>();
    public static void addDataAsync(@NonNull final AppDatabase db, NewsData data) {
        AddbAsync task = new AddbAsync(db, data);
        task.execute();
    }

    public static List<NewsData> getDataAsync(@NonNull final AppDatabase db) {
        GetData task = new GetData(db);
        task.execute();
        return newList;



    }

    private static NewsData addNews(final AppDatabase db, NewsData news) {
        db.newDao().insertAll(news);
        return news;
    }

    private static List<NewsData> getArticle(AppDatabase db) {


        List<NewsData> newList = db.newDao().getAll();
        return newList;
    }

    private static class AddbAsync extends AsyncTask<Void, Void, Void> {

        private final AppDatabase mDb;
        private final NewsData mData;

        AddbAsync(AppDatabase db, NewsData data) {
            mDb = db;
            mData = data;
        }

        @Override
        protected Void doInBackground(final Void... params) {
            addNews(mDb, mData);
            return null;

        }
    }

    private static class GetData extends AsyncTask<Void, Void, Void> {

        private final AppDatabase mDb;

        GetData(AppDatabase db) {
            mDb = db;
        }

        @Override
        protected Void doInBackground(final Void... params) {
            newList=getDataAsync(mDb);
            return null;

        }
    }

}
