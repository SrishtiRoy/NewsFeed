package com.and.newsfeed.http;
import org.json.JSONObject;

public interface HTTPResponseListener {

    Boolean setPostStatus(JSONObject finalResult, String postUrl,
                          int responseCode);

    Boolean setGetStatus(JSONObject finalResult, String getUrl, int responseCode);

}
