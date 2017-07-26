package com.and.newsfeed.ui;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.RelativeLayout;

import com.and.newsfeed.MainActivity;
import com.and.newsfeed.R;

/**
 * Created by sristi on 5/5/16.
 */
public class WebViewFragment extends BaseFragment {
    String url;
    boolean isWeb = false;
    RelativeLayout progreesbAr;

    @Override
    public String getCustomTag() {
        return "WebFragment";
    }

    @Override
    public String getActionBarTitle() {
        return null;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.frag_web, container,
                false);

        progreesbAr = (RelativeLayout) rootView.findViewById(R.id.progress_bar);


        mParentActivity = (MainActivity) getActivity();
        mParentActivity.collapseToolbar();
        Bundle bundle = getArguments();
        if (bundle != null && bundle.containsKey("url")) {
            url = bundle.getString("url");
            isWeb = bundle.getBoolean("isWeb");

        }

        WebView mWebView = (WebView) rootView.findViewById(R.id.webView);

        mWebView.setWebViewClient(new CustomWebViewClient());
        mWebView.getSettings().setJavaScriptEnabled(true);
        mWebView.getSettings().setLoadWithOverviewMode(true);
        mWebView.getSettings().setUseWideViewPort(true);
        mWebView.getSettings().setBuiltInZoomControls(true);
        mWebView.loadUrl(url);
      /*  mParentActivity.hideTabbarMenu();
        if(isWeb)

        mParentActivity.setoolbarTitle("Open Source Contribution");
        else
            mParentActivity.setoolbarTitle(" App Store");*/


        return rootView;
    }

    public class CustomWebViewClient extends WebViewClient {
        @Override
        public void onPageStarted(WebView view, String url, Bitmap favicon) {
            progreesbAr.setVisibility(View.VISIBLE);
            super.onPageStarted(view, url, favicon);
        }


        @Override
        public void onLoadResource(WebView view, String url) {
            super.onLoadResource(view, url);
            progreesbAr.setVisibility(View.GONE);

        }

        @Override
        public void onPageFinished(WebView view, String url) {
            super.onPageFinished(view, url);
            view.clearCache(true);
        }

    }
}
