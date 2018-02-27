package com.and.newsfeed.ui;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ArgbEvaluator;
import android.animation.ObjectAnimator;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateInterpolator;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.and.newsfeed.R;
import com.and.newsfeed.adapter.NewFeedArticleRecyclerAdapter;
import com.and.newsfeed.adapter.NewsFeedSourceListAdapter;
import com.and.newsfeed.data.ArticleModel;
import com.and.newsfeed.data.SourceModel;
import com.and.newsfeed.http.HTTPResponseListener;
import com.and.newsfeed.http.HttpGet;
import com.and.newsfeed.interfaces_feed.OnBackPressed;
import com.and.newsfeed.utils.NewFeedApi;
import com.bumptech.glide.Glide;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;


/**
 * Created by srishtic on 3/21/17.
 */

public class NewsFeedListFragment extends  BaseFragment  implements View.OnClickListener,OnBackPressed,HTTPResponseListener{


    public final static String TAG_NAME = "NewsFeedDetailFragment";
    private RecyclerView horizontal_recycler_view;

    private ArrayList<ArticleModel> mArticleList;

    private static final int SHORT_ANIMATION_DURATION = 200;
    private static final int DELAY_ANIMATION_DURATION = 40;
    private static final int LIKE_BTN_ANIMATION_DURATION = 120;

    private RecyclerView mRecyclerView;
    private ImageView mImageView;
    private TextView titleTextView, subTitleTextView;
    private View tintView;
    private View mBackgroundView;
          private RelativeLayout  mContainer;
    private ScrollView mContentLayout;
    private  String mId;

    private boolean isOpen;
    private int position;
    private Rect startBounds;
    private Rect finalBounds;
    private  FancyButton btnUrlink;
    private  String mHtmlString="";
    private FloatingActionButton fab;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.activity_blog_transition, container, false);


        initViews(view);

        // Setup layout manager for mBlogList and column count
        final LinearLayoutManager layoutManager = new LinearLayoutManager(mParentActivity);

        // Control orientation of the mBlogList
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        layoutManager.scrollToPosition(0);
        // Attach layout manager
        mRecyclerView.setLayoutManager(layoutManager);

        // Bind adapter to recycler
        mArticleList = new ArrayList<>();
        Bundle bundle = getArguments();
        if (bundle != null && bundle.containsKey("id")) {
            mId = bundle.getString("id");
        }
        mRecyclerView
                .addOnItemTouchListener(new RecyclerItemClickListener(
                        mParentActivity,
                        new RecyclerItemClickListener.OnItemClickListener() {
                            @Override
                            public void onItemClick(View itemView, int position) {

                                if (!isOpen) {
                                    NewsFeedListFragment.this.position = position;
                                    mParentActivity.isDetailView=false;
                                    openBlogInDetails(itemView);

                                }

                            }
                        }));





        fetchSuggestions();
        return view;
    }


    @Override
    public String getCustomTag() {
        return TAG_NAME;
    }


    @Override
    public String getActionBarTitle() {
        return "";
    }



    class MyAsyncTask extends AsyncTask<Void, Void, Void> {

        @Override
        protected Void doInBackground(Void... params) {

            //extractDataFromURL();
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            //load extracted data into webview
            //mWebView.loadData("<html>" + mHtmlString + "</html>", "text/html; charset=utf-8", "UTF-8");
            subTitleTextView.setText(mArticleList.get(position).getDescription()+mHtmlString);

            super.onPostExecute(aVoid);
        }
    }




    private void openBlogInDetails(View itemView) {
        mParentActivity.isDetailView=true;

        // Now is open
        this.isOpen = true;

        //set Scroll View to the top
        mContentLayout.setScrollY(0);
        Glide
                .with(mParentActivity)
                .load(mArticleList.get(position).getUrlToImage())
                .centerCrop()
                .placeholder(R.drawable.ic_action_favorite)
                .crossFade()
                .into(mImageView);

        subTitleTextView.setText(mArticleList.get(position).getDescription()+mHtmlString);
        //+mParentActivity.getResources().getString(R.string.big_text));

        // Init Rect
        startBounds = new Rect();
        finalBounds = new Rect();

        // Setting the bound to startRect "startBounds"
        startBounds.left = itemView.getLeft();
        startBounds.right = itemView.getRight();
        startBounds.top = itemView.getTop();
        startBounds.bottom = itemView.getBottom();

        // Setting the bound to endRect "finalBounds"
        finalBounds.left = mContainer.getLeft();
        finalBounds.right = mContainer.getRight();
        finalBounds.top = mContainer.getTop();
        finalBounds.bottom = mContainer.getBottom();

        // Calculate scaling factor
        float startScaleX = (float) startBounds.width() / finalBounds.width();
        float startScaleY = (float) startBounds.height() / finalBounds.height();



        // Prepare the views before starting animation

        mContentLayout.setVisibility(View.VISIBLE);
        mBackgroundView.setVisibility(View.VISIBLE);
        mImageView.setVisibility(View.VISIBLE);

        mBackgroundView.setPivotX(0);
        mBackgroundView.setPivotY(0);
        mBackgroundView.setX(startBounds.left);
        mBackgroundView.setY(startBounds.top);
        mBackgroundView.setScaleX(startScaleX);
        mBackgroundView.setScaleY(startScaleY);



        // backgroundView Color Animator
        ObjectAnimator backgroundViewColor = ObjectAnimator.ofObject(
                mBackgroundView, "backgroundColor", new ArgbEvaluator(), Color.CYAN, Color.WHITE);;

        // backgroundView X point Animator
        ObjectAnimator backgroundViewX = ObjectAnimator
                .ofFloat(mBackgroundView, View.X, finalBounds.left);

        // backgroundView Y point Animator
        ObjectAnimator backgroundViewY = ObjectAnimator
                .ofFloat(mBackgroundView, View.Y, finalBounds.top);

        // backgroundView width scaling Animator
        ObjectAnimator backgroundViewScaleX = ObjectAnimator
                .ofFloat(mBackgroundView, View.SCALE_X, 1f);

        // backgroundView height scaling Animator
        ObjectAnimator backgroundViewScaleY = ObjectAnimator
                .ofFloat(mBackgroundView, View.SCALE_Y, 1f);

        // Set of animators to play all of animators together.
        AnimatorSet backgroundViewAnimatorSet  = new AnimatorSet();
        backgroundViewAnimatorSet.setInterpolator(new AccelerateInterpolator());
        backgroundViewAnimatorSet.setDuration(SHORT_ANIMATION_DURATION);
        backgroundViewAnimatorSet.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
            }

            @Override
            public void onAnimationCancel(Animator animation) {
                super.onAnimationCancel(animation);
            }
        });
        backgroundViewAnimatorSet.playTogether(backgroundViewColor, backgroundViewX,
                backgroundViewY, backgroundViewScaleX, backgroundViewScaleY);

        //Start animation
        backgroundViewAnimatorSet.start();


        // contentLayout Alpha Animator
        ObjectAnimator mContentLayoutAlpha = ObjectAnimator.ofFloat(mContentLayout, View.ALPHA, 1f);

        // imageView Alpha Animator
        ObjectAnimator mImageViewAlpha = ObjectAnimator.ofFloat(mImageView, View.ALPHA, 1f);

        // Set of animators to play all of animators together.
        AnimatorSet mContentLayoutAnimatorSet  = new AnimatorSet();
        mContentLayoutAnimatorSet.setInterpolator(new AccelerateInterpolator());
        mContentLayoutAnimatorSet.setStartDelay(DELAY_ANIMATION_DURATION);
        mContentLayoutAnimatorSet.setDuration(SHORT_ANIMATION_DURATION);
        mContentLayoutAnimatorSet.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                // Animate LikeButton after contentLayout completely shown
                animateLikeButton();
            }
            @Override
            public void onAnimationCancel(Animator animation) {
                super.onAnimationCancel(animation);
            }
        });
        mContentLayoutAnimatorSet.playTogether(mContentLayoutAlpha, mImageViewAlpha);

        //Start animation
        mContentLayoutAnimatorSet.start();

    }

    private void animateLikeButton() {
        //btnLike.animate().setDuration(LIKE_BTN_ANIMATION_DURATION).scaleX(1f).scaleY(1f);
    }


    private void closeBlogDetails() {
        mParentActivity.isDetailView=false;

        // Now is closed
        this.isOpen = false;

        // Calculate scaling factor
        float startScaleX = (float) startBounds.width() / finalBounds.width();
        float startScaleY = (float) startBounds.height() / finalBounds.height();

        // contentLayout Alpha Animator
        ObjectAnimator contentLayoutAlpha = ObjectAnimator.ofFloat(mContentLayout, View.ALPHA, 0f);

        // imageView Alpha Animator
        ObjectAnimator imageViewAlpha = ObjectAnimator.ofFloat(mImageView, View.ALPHA, 0f);

        // Set of animators to play all of animators together.
        AnimatorSet mContentLayoutAnimatorSet  = new AnimatorSet();
        mContentLayoutAnimatorSet.setInterpolator(new AccelerateInterpolator());
        mContentLayoutAnimatorSet.setStartDelay(0);
        mContentLayoutAnimatorSet.setDuration(SHORT_ANIMATION_DURATION);
        mContentLayoutAnimatorSet.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                mContentLayout.setVisibility(View.GONE);
                mImageView.setVisibility(View.GONE);
            }
            @Override
            public void onAnimationCancel(Animator animation) {
                super.onAnimationCancel(animation);
                mContentLayout.setVisibility(View.GONE);
                mImageView.setVisibility(View.GONE);
            }
        });
        mContentLayoutAnimatorSet.playTogether(contentLayoutAlpha, imageViewAlpha);

        //Start animation
        mContentLayoutAnimatorSet.start();


        // backgroundView Color Animator
        ObjectAnimator backgroundViewColor = ObjectAnimator.ofObject(
                mBackgroundView, "backgroundColor", new ArgbEvaluator(),
                Color.WHITE, Color.CYAN);

        // backgroundView X point Animator
        ObjectAnimator backgroundViewX = ObjectAnimator
                .ofFloat(mBackgroundView, View.X, startBounds.left);

        // backgroundView Y point Animator
        ObjectAnimator backgroundViewY = ObjectAnimator
                .ofFloat(mBackgroundView, View.Y, startBounds.top);

        // backgroundView width scaling Animator
        ObjectAnimator backgroundViewScaleX = ObjectAnimator
                .ofFloat(mBackgroundView, View.SCALE_X, startScaleX);

        // backgroundView height scaling Animator
        ObjectAnimator backgroundViewScaleY = ObjectAnimator
                .ofFloat(mBackgroundView, View.SCALE_Y, startScaleY);

        // Set of animators to play all of animators together.
        AnimatorSet backgroundViewAnimatorSet = new AnimatorSet();
        backgroundViewAnimatorSet.setInterpolator(new AccelerateInterpolator());
        backgroundViewAnimatorSet.setStartDelay(DELAY_ANIMATION_DURATION);
        backgroundViewAnimatorSet.setDuration(SHORT_ANIMATION_DURATION);
        backgroundViewAnimatorSet.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                mBackgroundView.setVisibility(View.GONE);
            }

            @Override
            public void onAnimationCancel(Animator animation) {
                super.onAnimationCancel(animation);
                mBackgroundView.setVisibility(View.GONE);
            }
        });
        backgroundViewAnimatorSet.playTogether(backgroundViewColor, backgroundViewX, backgroundViewY,
                backgroundViewScaleX, backgroundViewScaleY);

        //Start animation
        backgroundViewAnimatorSet.start();

    }






   /* @Override
    public void onBackPressed() {

        if (this.isOpen) {
            closeBlogDetails();
        } else {
            super.onBackPressed();
        }

    }
*/
    private void initViews(View view) {
        mContainer = (RelativeLayout)view.findViewById(R.id.container);
        mBackgroundView = (View)view.findViewById(R.id.backgroundView);
        mContentLayout  = (ScrollView)view. findViewById(R.id.contentLayout);
        mRecyclerView = (RecyclerView)view. findViewById(R.id.recyclerView);
        tintView =( View)view.findViewById(R.id.tintViewFront);
        mImageView = (ImageView)view. findViewById(R.id.imageView);
        titleTextView = (TextView)view. findViewById(R.id.title);
        subTitleTextView = (TextView) view.findViewById(R.id.description_text);
        btnUrlink = (FancyButton) view.findViewById(R.id.btn_url);
        btnUrlink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Bundle bundle=new Bundle();
                bundle.putString("url", mArticleList.get(position).getUrl());
                mParentActivity.showNextView(new WebViewFragment(), bundle);
            }
        });

    }

    @Override
    public Boolean setGetStatus(JSONObject finalResult, String getUrl, int responseCode) {
        //if (getUrl.equalsIgnoreCase(NewFeedApi.GET_FEED_API)) {
            if (finalResult != null) {



                try {

                    JSONArray jsonArray = finalResult.getJSONArray("articles");
                    if (jsonArray.length() > 0) {
                        for (int i = 0; i < jsonArray.length(); i++) {

                            JSONObject obj = jsonArray.getJSONObject(i);
                            ArticleModel appObj = new ArticleModel(obj);

                            if(!mArticleList.contains(appObj.getTitle()))
                                mArticleList.add(appObj);


                        }
                        Log.e("logtag", "" + mArticleList.size());
                    }
                  //  mProgreesBarLayout.setVisibility(View.GONE);
                    NewFeedArticleRecyclerAdapter   adapter = new NewFeedArticleRecyclerAdapter(mParentActivity, mArticleList);
                    mRecyclerView.setAdapter(adapter);
                }

                catch (JSONException e) {
                }
            }
       // }
        return false;
    }

    @Override
    public Boolean setPostStatus(JSONObject finalResult, String postUrl, int responseCode) {
        return null;
    }

    private void fetchSuggestions() {
        //if (Util.haveNetworkConnection(mParentActivity)) {
       // mProgreesBarLayout.setVisibility(View.VISIBLE);
        String api=getUrlByID(mId);
        HttpGet getSuggestionRequest = new HttpGet(this, api);
        getSuggestionRequest.run(api);
        //}



    }

    public  String getUrlByID(final String id) {
        return NewFeedApi.GET_FEED_API.replace("bloomberg", id);
    }

    @Override
    public void onClick(View v) {



    }

    @Override
    public void onBackPressed() {
     if   (mParentActivity.isDetailView)
        closeBlogDetails();
    }
}
