package com.and.newsfeed.utils;

import android.graphics.Bitmap;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.and.newsfeed.MainActivity;

/**
 * Created by shohrab on 7/6/2016.
 */
public class MyWebViewClient extends WebViewClient {

    private MainActivity mMainActivity;

    MyWebViewClient (MainActivity mainActivity){
        mMainActivity = mainActivity;
    }

    @Override
    public boolean shouldOverrideUrlLoading(WebView view, String url) {
        view.loadUrl(url);
        return true;
    }

}
