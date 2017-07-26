package com.and.newsfeed.ui;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.animation.Animation;

import com.and.newsfeed.MainActivity;


public abstract class BaseFragment extends Fragment {

    protected MainActivity mParentActivity;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        setHasOptionsMenu(true);
        return super.onCreateView(inflater, container, savedInstanceState);
    }




    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        if (activity instanceof MainActivity)
            mParentActivity = (MainActivity) activity;

    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser && getActivity() instanceof MainActivity) {
            mParentActivity = MainActivity.getInstance();
            mParentActivity.getWindow().setSoftInputMode(
                    WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

        }
    }



    @Override
    public Animation onCreateAnimation(int transit, boolean enter, int nextAnim) {

        return super.onCreateAnimation(transit, enter, nextAnim);
    }

    public abstract String getCustomTag();






    public abstract String getActionBarTitle();



    }
