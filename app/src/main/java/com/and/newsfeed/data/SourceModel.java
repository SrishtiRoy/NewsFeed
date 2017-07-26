package com.and.newsfeed.data;

import android.os.Parcel;
import android.os.Parcelable;

import org.json.JSONException;
import org.json.JSONObject;

public class SourceModel implements Parcelable {


    private String mName;
    private String mSortBy;
    private String mId;
    private boolean favourite;


    public String getmSortBy() {
        return mSortBy;
    }

    public void setmSortBy(String mSortBy) {
        this.mSortBy = mSortBy;
    }


    public String getmId() {
        return mId;
    }

    public void setmId(String mId) {
        this.mId = mId;
    }


    public String getmName() {
        return mName;
    }

    public void setmName(String mName) {
        this.mName = mName;
    }


    public static Creator<SourceModel> getCreator() {
        return CREATOR;
    }


    public static final Creator<SourceModel> CREATOR = new Creator<SourceModel>() {
        public SourceModel createFromParcel(Parcel in) {
            return new SourceModel(in);
        }

        public SourceModel[] newArray(int size) {
            return new SourceModel[size];
        }
    };


    public SourceModel(JSONObject obj) {
        try {
            mName = obj.getString("name");
            mSortBy = obj.getString("sortBysAvailable");
            mId = obj.getString("id");

        } catch (JSONException e) {
        }
    }

    public SourceModel(Parcel source) {
        setmName(source.readString());
        setmSortBy(source.readString());

        setmId(source.readString());


    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(getmName());
        dest.writeString(getmSortBy());
        dest.writeString(getmId());

    }


}

