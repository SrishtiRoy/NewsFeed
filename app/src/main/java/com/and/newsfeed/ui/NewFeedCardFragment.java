package com.and.newsfeed.ui;

import android.content.res.Resources;
import android.graphics.Rect;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.Toast;


import com.and.newsfeed.R;
import com.and.newsfeed.adapter.NewsFeedSourceListAdapter;
import com.and.newsfeed.data.SourceModel;
import com.and.newsfeed.http.AsyncThreadPool;
import com.and.newsfeed.http.HTTPResponseListener;
import com.and.newsfeed.http.HttpGet;
import com.and.newsfeed.utils.NewFeedApi;
import com.and.newsfeed.utils.Util;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;


/**
 * Created by sristi on 12/5/16.
 */
public class NewFeedCardFragment extends BaseFragment implements HTTPResponseListener {

    public final static String TAG_NAME = "NewFeedCardFragment";
    private String arrOrg[];
    private RecyclerView mRecyclerView;
    private ArrayList<SourceModel> mSourceModelList;
    private NewsFeedSourceListAdapter adapter;
    public Handler mUIHandler;
    public AsyncThreadPool mThreadPool;
    private RelativeLayout mProgreesBarLayout;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.flat_suggestion_list, container, false);
        initializeViews(view);
        fetchSuggestions();
        mParentActivity.collapseToolbar();
        mParentActivity.avatarLayout.setVisibility(View.GONE);

        return view;
    }


    private void initializeViews(View view) {


        Toolbar toolbar = (Toolbar) view.findViewById(R.id.toolbar);
        mParentActivity.setSupportActionBar(toolbar);
        mSourceModelList = new ArrayList<>();
        mRecyclerView = (RecyclerView) view.findViewById(R.id.suggestion_list);
        mProgreesBarLayout = (RelativeLayout) view.findViewById(R.id.progress_circular_parent);

        final RecyclerView.LayoutManager mLayoutManager = new GridLayoutManager(mParentActivity, 2);
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.addItemDecoration(new NewFeedCardFragment.GridSpacingItemDecoration(2, dpToPx(10), true));
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());

        mUIHandler = new Handler(Looper.getMainLooper());
        AsyncThreadPool.init(mUIHandler);
        mThreadPool = AsyncThreadPool.get();
        mRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
            }
        });
    }



    @Override
    public Boolean setGetStatus(JSONObject finalResult, String getUrl, int responseCode) {
        if (getUrl.equalsIgnoreCase(NewFeedApi.GET_SOURCE_API)) {
            if (finalResult != null) {



                try {

                   JSONArray jsonArray = finalResult.getJSONArray("sources");
                    if (jsonArray.length() > 0) {
                        for (int i = 0; i < jsonArray.length(); i++) {

                            JSONObject obj = jsonArray.getJSONObject(i);
                            SourceModel appObj = new SourceModel(obj);

                            if(!mSourceModelList.contains(appObj.getmName()))
                                mSourceModelList.add(appObj);


                        }
                        Log.e("logtag", "" + mSourceModelList.size());
                    }
                    mProgreesBarLayout.setVisibility(View.GONE);


                    Collections.shuffle(mSourceModelList);


                    adapter = new NewsFeedSourceListAdapter(mParentActivity, mSourceModelList);
                    mRecyclerView.setAdapter(adapter);
                }

                catch (JSONException e) {
                }
            }
        }
        return false;
    }

    @Override
    public Boolean setPostStatus(JSONObject finalResult, String postUrl, int responseCode) {
        return null;
    }

    private void fetchSuggestions() {
        //if (Util.haveNetworkConnection(mParentActivity)) {
            mProgreesBarLayout.setVisibility(View.VISIBLE);
            HttpGet getSuggestionRequest = new HttpGet(this, NewFeedApi.GET_SOURCE_API);
            getSuggestionRequest.run(NewFeedApi.GET_SOURCE_API);
        //}



    }


    public class GridSpacingItemDecoration extends RecyclerView.ItemDecoration {

        private int spanCount;
        private int spacing;
        private boolean includeEdge;

        public GridSpacingItemDecoration(int spanCount, int spacing, boolean includeEdge) {
            this.spanCount = spanCount;
            this.spacing = spacing;
            this.includeEdge = includeEdge;
        }

        @Override
        public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
            int position = parent.getChildAdapterPosition(view); // item position
            int column = position % spanCount; // item column

            if (includeEdge) {
                outRect.left = spacing - column * spacing / spanCount; // spacing - column * ((1f / spanCount) * spacing)
                outRect.right = (column + 1) * spacing / spanCount; // (column + 1) * ((1f / spanCount) * spacing)

                if (position < spanCount) { // top edge
                    outRect.top = spacing;
                }
                outRect.bottom = spacing; // item bottom
            } else {
                outRect.left = column * spacing / spanCount; // column * ((1f / spanCount) * spacing)
                outRect.right = spacing - (column + 1) * spacing / spanCount; // spacing - (column + 1) * ((1f /    spanCount) * spacing)
                if (position >= spanCount) {
                    outRect.top = spacing; // item top
                }
            }
        }
    }

    /**
     * Converting dp to pixel
     */
    private int dpToPx(int dp) {
        Resources r = getResources();
        return Math.round(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, r.getDisplayMetrics()));
    }


    @Override
    public String getCustomTag() {
        return TAG_NAME;
    }

    @Override
    public String getActionBarTitle() {
        return "HAgentSuggestionCardFragment";
    }
}

