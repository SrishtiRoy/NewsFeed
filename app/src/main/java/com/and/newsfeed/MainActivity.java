package com.and.newsfeed;

import android.content.res.Configuration;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.and.newsfeed.interfaces_feed.OnBackPressed;
import com.and.newsfeed.ui.BaseFragment;
import com.and.newsfeed.ui.FragmentDrawer;
import com.and.newsfeed.ui.NewFeedSourceFragment;
import com.and.newsfeed.ui.NewsFeedListFragment;

import java.util.ArrayList;
import java.util.List;


public class MainActivity extends AppCompatActivity implements View.OnClickListener, FragmentDrawer.FragmentDrawerListener {


    private ArrayList<Transaction> mTransactionList = new ArrayList<>();
    private FragmentManager mFragmentManager;
    private static MainActivity activityInstance;
    private boolean mOnPause = false;
    private static final Object lock = new Object();
    ActionBarDrawerToggle toggle;
    DrawerLayout drawerLayout;
    public AppBarLayout mAppBarLayout;
    private CoordinatorLayout coordinatorLayout;
    private CollapsingToolbarLayout collapsingToolbarLayout;
    public ImageView mBackDropView;
    public LinearLayout avatarLayout;
    public boolean isDetailView=false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        initializeViews();

        toggle = new ActionBarDrawerToggle(this, drawerLayout, R.string.app_name, R.string.app_name) {

            public void onDrawerClosed(View view) {

            }

            public void onDrawerOpened(View drawerView) {
                supportInvalidateOptionsMenu();
            }
        };
        toggle.setDrawerIndicatorEnabled(true);
        drawerLayout.setDrawerListener(toggle);
        toggle.syncState();


       // collapseToolbar();
    }




    private void initializeViews()
    {
        activityInstance = this;
        setContentView(R.layout.activity_main);
        mFragmentManager = getSupportFragmentManager();
        drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("News Feed");
        toolbar.setTitleTextColor(Color.WHITE);

        setSupportActionBar(toolbar);

        showNextView(new NewFeedSourceFragment());
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        // Sync the toggle state after onRestoreInstanceState has occurred.
        toggle.syncState();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        // Pass any configuration change to the drawer toggles
        toggle.onConfigurationChanged(newConfig);
    }


    @Override
    public void onBackPressed() {

        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START);
        } else {
            //super.onBackPressed();
        }
        if (mFragmentManager.getBackStackEntryCount() == 0) {
            finish();

        }
        if(mFragmentManager.getBackStackEntryCount()>0)
        {
            if (mFragmentManager != null) {
                {

                    NewsFeedListFragment myFragment = (NewsFeedListFragment)getFragmentByTagName(NewsFeedListFragment.TAG_NAME);
                    if (myFragment != null && myFragment.isVisible()  && isDetailView) {

                        ((OnBackPressed) getFragmentByTagName(NewsFeedListFragment.TAG_NAME)).onBackPressed();


                        return;
                    }
                    else
                    {

                        pop();
                    }
                }

            }
        }
    }


    @Override
    public void onDrawerItemSelected(View view, int position) {
        displayView(position);
    }

    private void displayView(int position) {
        Fragment fragment = null;
        String title = getString(R.string.app_name);
        switch (position) {
            case 0:
                break;
            case 1:

                break;
            case 2:

                break;
            default:
                break;
        }
    }

    public synchronized void addFragment(BaseFragment fragment, Bundle bundle) {
        fragment.setArguments(bundle);
        addFragment(fragment);
    }


    public synchronized void addFragment(BaseFragment fragment) {


        if (mFragmentManager != null) {
            if (!mOnPause) {
                if (getTopFragment() != null
                        && getTopFragment().getCustomTag().equals(fragment.getCustomTag()))
                    return;
                mFragmentManager.beginTransaction()
                        .setCustomAnimations(R.anim.slide_in_right, R.anim.slide_out_left, android.R.anim.slide_in_left, android.R.anim.slide_out_right)
                        .replace(R.id.fragment_container,
                                fragment, fragment.getCustomTag())
                        .addToBackStack(fragment.getCustomTag())
                        .commit();
                mFragmentManager.executePendingTransactions();
            } else
                mTransactionList.add(new FragmentPush(fragment));
        }
    }


    public synchronized void pop() {
        if (mFragmentManager != null) {
            if (!mOnPause && mFragmentManager.getBackStackEntryCount() > 0)
                mFragmentManager.popBackStackImmediate();
            else if (mTransactionList.size() > 0
                    && mTransactionList.get(mTransactionList.size() - 1) instanceof FragmentPush)
                mTransactionList.remove(mTransactionList.size() - 1);
            else if (mFragmentManager.getBackStackEntryCount() > 0)
                mTransactionList.add(new FragmentPop());
        }
    }

    @Nullable
    public synchronized Fragment getFragmentByTagName(String tagName) {
        List<Fragment> allFragments = mFragmentManager.getFragments();
        for (Fragment fragment : allFragments) {
            if (fragment != null && fragment.getTag() != null && fragment.getTag().equals(tagName)) {
                return fragment;
            }
        }
        return null;
    }


    public synchronized void popUntil(String tag) {
        if (mFragmentManager != null) {
            if (!mOnPause && mFragmentManager.getBackStackEntryCount() > 0)
                mFragmentManager.popBackStackImmediate(tag, 0);
            else if (mFragmentManager.getBackStackEntryCount() > 0)
                mTransactionList.add(new FragmentPopUntil(tag));
        }
    }

    @Nullable
    public BaseFragment getTopFragment() {

        if (mFragmentManager != null) {
            Fragment fragment = mFragmentManager
                    .findFragmentById(R.id.fragment_container);
            if (fragment instanceof BaseFragment)
                return (BaseFragment) fragment;
        }
        return null;
    }

    abstract class Transaction {
        public abstract void performTransaction();
    }

    public class FragmentPush extends Transaction {
        BaseFragment fragment;

        FragmentPush(BaseFragment frag) {
            fragment = frag;
        }

        @Override
        public void performTransaction() {
            addFragment(fragment);
        }
    }


    public class FragmentAddWithoutBackStack extends Transaction {
        BaseFragment fragment;

        FragmentAddWithoutBackStack(BaseFragment frag) {
            fragment = frag;
        }

        @Override
        public void performTransaction() {
            addFragmentWithOutBackStack(fragment);
        }
    }

    public class FragmentPop extends Transaction {
        @Override
        public void performTransaction() {
            pop();
        }
    }


    public class FragmentPopUntil extends Transaction {
        String mTag;

        FragmentPopUntil(String tag) {
            mTag = tag;
        }

        @Override
        public void performTransaction() {
            popUntil(mTag);
        }
    }


    @Override
    protected void onPostResume() {
        super.onPostResume();
        mOnPause = false;
        while (!mTransactionList.isEmpty())
            mTransactionList.remove(0).performTransaction();
    }


    public synchronized void addFragmentWithOutBackStack(BaseFragment fragment) {
        if (mFragmentManager != null) {
            if (!mOnPause) {
                mFragmentManager.beginTransaction()
                        .replace(R.id.fragment_container,
                                fragment, fragment.getCustomTag())
                        .commit();
                mFragmentManager.executePendingTransactions();
            } else
                mTransactionList.add(new FragmentAddWithoutBackStack(fragment));
        }
    }

    @NonNull
    public String getTopFragmentName() {
        if (getTopFragment() != null)
            return getTopFragment().getTag();
        return "";
    }

    public synchronized boolean isFragmentOnStack(String tagName) {
        synchronized (lock) {
            List<Fragment> allFragments = mFragmentManager.getFragments();
            for (Fragment fragment : allFragments) {
                if (fragment != null && fragment.getTag() != null && fragment.getTag().equals(tagName)) {
                    return true;
                }
            }
        }
        return false;
    }

    public static MainActivity getInstance() {
        return activityInstance;
    }


    public void showNextView(BaseFragment fragment, Bundle bundle) {
        fragment.setArguments(bundle);
        showNextView(fragment);
    }

    public void showNextView(BaseFragment fragment, Bundle bundle,
                             boolean disableAnimation) {
        fragment.setArguments(bundle);
        addFragment(fragment);
    }

    public void showNextView(BaseFragment fragment, boolean disableAnimation) {
        showNextView(fragment);
    }


    public void showNextView(BaseFragment fragment) {

        addFragment(fragment);
    }


    @Override
    public void onClick(View v) {

    }


}



