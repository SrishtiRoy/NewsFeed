package com.and.newsfeed.data;

import android.os.Parcel;
import android.os.Parcelable;
import android.print.PageRange;
import android.widget.ProgressBar;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by srishtic on 4/14/17.
 */

public class ArticleModel implements Parcelable {

    private String author;
    private String title;
    private String description;
    private String url;
    private String urlToImage;
    private String publishedAt;


    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getUrlToImage() {
        return urlToImage;
    }

    public void setUrlToImage(String urlToImage) {
        this.urlToImage = urlToImage;
    }

    public String getPublishedAt() {
        return publishedAt;
    }

    public void setPublishedAt(String publishedAt) {
        this.publishedAt = publishedAt;
    }


    public static Creator<ArticleModel> getCreator() {
        return CREATOR;
    }


    public static final Creator<ArticleModel> CREATOR = new Creator<ArticleModel>() {
        public ArticleModel createFromParcel(Parcel in) {
            return new ArticleModel(in);
        }

        public ArticleModel[] newArray(int size) {
            return new ArticleModel[size];
        }
    };


    public ArticleModel(JSONObject obj) {
        try {
            author = obj.getString("author");
            title = obj.getString("title");
            url = obj.getString("url");
            description = obj.getString("description");
            publishedAt = obj.getString("publishedAt");
            urlToImage = obj.getString("urlToImage");

        } catch (JSONException e) {
        }
    }

    public ArticleModel(Parcel source) {
        setAuthor(source.readString());
        setDescription(source.readString());
        setUrl(source.readString());
        setTitle(source.readString());
        setUrlToImage(source.readString());
        setPublishedAt(source.readString());


    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(getAuthor());
        dest.writeString(getDescription());
        dest.writeString(getPublishedAt());
        dest.writeString(getUrl());
        dest.writeString(getUrlToImage());
        dest.writeString(getTitle());

    }

}
