package com.and.newsfeed.http;

import android.annotation.SuppressLint;
import android.os.AsyncTask;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.zip.GZIPInputStream;

import javax.net.ssl.SSLPeerUnverifiedException;


public class HttpGet extends AsyncTask<String, Void, JSONObject> {

    private long startTime;
    private HTTPResponseListener mListener;
    private String mRef = "";
    private int mResponseCode = -1;
    private int mRetrialCount;
    private int mMaxRetrials = 2;
    private long totaltime;
    private StringBuilder query;

    public HttpGet(HTTPResponseListener listener, String callRef) {
        mListener = listener;
        mRef = callRef;
        mRetrialCount = 0;
    }

    @Override
    protected JSONObject doInBackground(String... params) {
        JSONObject finalResult = null;
        URL url = null;
        android.os.Process
                .setThreadPriority(android.os.Process.THREAD_PRIORITY_URGENT_DISPLAY);
        long currenttime = System.currentTimeMillis();
        HttpURLConnection conn = null;
        try {

            if (params[0].contains("?")) {

            }

            url = new URL(params[0]);

            conn = (HttpURLConnection) url.openConnection();


            conn.setConnectTimeout(20000);
            conn.setReadTimeout(20000);

            conn.setRequestMethod("GET");


            conn.setRequestProperty("Accept-Encoding", "gzip");

            startTime = System.currentTimeMillis();
            conn.connect();
            try {
                mResponseCode = conn.getResponseCode();
                totaltime = System.currentTimeMillis() - startTime;

            } catch (SSLPeerUnverifiedException e) {

                return null;
            } catch (IOException e) {


                return null;
            }

            if (conn.getInputStream() != null) {
                InputStream stream = null;
                String encoding = conn.getContentEncoding();
                if ("gzip".equals(encoding)) {
                    stream = new GZIPInputStream(conn.getInputStream());
                } else {
                    stream = conn.getInputStream();
                }

                BufferedReader reader = new BufferedReader(
                        new InputStreamReader(stream, "UTF-8"));

                StringBuilder builder = new StringBuilder();
                String line = null;
                while ((line = reader.readLine()) != null) {
                    builder.append(line);
                }


                if (builder != null && builder.length() > 0) {
                    try {
                        finalResult = new JSONObject(builder.toString());
                    } catch (JSONException js) {
                        finalResult = new JSONObject();
                        finalResult.put("array",
                                new JSONArray(builder.toString()));

                    }
                }


                if (finalResult != null) {

                }
                reader.close();
                reader = null;
                stream.close();
                stream = null;
            } else {
            }

            // String urlRef = mRef;

            conn.disconnect();
            conn = null;
            query = null;
        } catch (Exception e) {
            int len = e.getStackTrace().length;
            StackTraceElement[] throwStackTrace = new StackTraceElement[len + 1];
            System.arraycopy(e.getStackTrace(), 0, throwStackTrace, 0, len);
            if (url != null && params.length > 0) {
                try {
                    url = new URL(params[0]);
                } catch (MalformedURLException e1) {
                    // TODO Auto-generated catch block
                }
                throwStackTrace[len] = new StackTraceElement("HttpGet",
                        url.getPath(), url.getPath(), 137);
            }
            e.setStackTrace(throwStackTrace);

           /* Crashlytics.setString("fc_url", mRef);
            Crashlytics.setInt("response_code", mResponseCode);
            Crashlytics.logException(e);*/

            // Reset the result in case of exception
            conn = null;

            if (++mRetrialCount < mMaxRetrials) {
                doInBackground(params);
            }
        }
        return finalResult;
    }

    @Override
    protected void onPostExecute(JSONObject finalResult) {
        super.onPostExecute(finalResult);


        if (mListener != null) {
            mListener.setGetStatus(finalResult, mRef, mResponseCode);
        }
        finalResult = null;
        // System.gc();
    }

    @SuppressLint("NewApi")
    public void run(String... params) {
        executeOnExecutor(AsyncThreadPool.get().getExecutor(), params);

    }
}