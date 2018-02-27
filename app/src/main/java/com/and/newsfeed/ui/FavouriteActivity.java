package com.and.newsfeed.ui;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.and.newsfeed.R;
import com.and.newsfeed.adapter.NewFeedArticleRecyclerAdapter;

/**
 * Created by srishtic on 2/22/18.
 */

public class FavouriteActivity extends AppCompatActivity {
    private RecyclerView mRecyclerView;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_blog_transition);
        // Setup layout manager for mBlogList and column count
        final LinearLayoutManager layoutManager = new LinearLayoutManager(this);

        // Control orientation of the mBlogList
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        layoutManager.scrollToPosition(0);
        // Attach layout manager
        mRecyclerView.setLayoutManager(layoutManager);
        NewFeedArticleRecyclerAdapter adapter = new NewFeedArticleRecyclerAdapter(this, mArticleList);
        mRecyclerView.setAdapter(adapter);
    }
}
