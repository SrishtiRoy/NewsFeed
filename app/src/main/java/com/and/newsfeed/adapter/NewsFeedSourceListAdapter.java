package com.and.newsfeed.adapter;

import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;


import com.and.newsfeed.MainActivity;
import com.and.newsfeed.R;
import com.and.newsfeed.data.SourceModel;
import com.and.newsfeed.ui.NewsFeedListFragment;
import com.and.newsfeed.ui.WebViewFragment;
import com.and.newsfeed.utils.ColorGenerator;
import com.and.newsfeed.utils.DrawableProvider;
import com.and.newsfeed.utils.TextDrawable;

import java.util.List;




public class NewsFeedSourceListAdapter extends RecyclerView.Adapter<NewsFeedSourceListAdapter.HAgentViewHolder> {

    private MainActivity mContext;
    private List<SourceModel> sourceModelList;
    private ColorGenerator mColorGenerator = ColorGenerator.MATERIAL;
    private TextDrawable.IBuilder mDrawableBuilder;
    private DrawableProvider mProvider;
    public class HAgentViewHolder extends RecyclerView.ViewHolder  {
        public TextView title, count;
        public ImageView thumbnail, overflow;

        public HAgentViewHolder(View view) {
            super(view);
            title = (TextView) view.findViewById(R.id.title);
            count = (TextView) view.findViewById(R.id.count);
            thumbnail = (ImageView) view.findViewById(R.id.thumbnail);
            overflow = (ImageView) view.findViewById(R.id.overflow);

        }


    }


    public NewsFeedSourceListAdapter(MainActivity mContext, List<SourceModel> sourceModelList) {
        this.mContext = mContext;
        mProvider = new DrawableProvider(mContext);

        this.sourceModelList = sourceModelList;
        mDrawableBuilder = TextDrawable.builder()
                .beginConfig()
                .withBorder(4)
                .endConfig()
                .roundRect(10);
    }

    @Override
    public HAgentViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.album_card, parent, false);

        return new HAgentViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final HAgentViewHolder holder, final  int position) {
        SourceModel sourceModel = sourceModelList.get(position);

        TextDrawable drawable = mDrawableBuilder.build(String.valueOf(sourceModel.getmName().charAt(0)), mColorGenerator.getColor(sourceModel.getmName()));
        holder.thumbnail.setImageDrawable(drawable);
        holder.title.setText(sourceModel.getmName());

        //zholder.count.setText(sourceModel.getmState() + "");
        holder.overflow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //showPopupMenu(holder.overflow);
            }
        });

        holder.thumbnail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle=new Bundle();
                bundle.putString("id", sourceModelList.get(position).getmId());
                mContext.showNextView(new NewsFeedListFragment(),bundle);

            }
        });
    }

   /* private void showPopupMenu(View view) {
        // inflate menu
        PopupMenu popup = new PopupMenu(mContext, view);
        MenuInflater inflater = popup.getMenuInflater();
        inflater.inflate(R.menu.menu_album, popup.getMenu());
        popup.setOnMenuItemClickListener(new MyMenuItemClickListener());
        popup.show();
    }

    *//**
     * Click listener for popup menu items
     *//*
    class MyMenuItemClickListener implements PopupMenu.OnMenuItemClickListener {

        public MyMenuItemClickListener() {
        }

        @Override
        public boolean onMenuItemClick(MenuItem menuItem) {
            switch (menuItem.getItemId()) {
                case R.id.action_add_favourite:
                    Toast.makeText(mContext, "Add to favourite", Toast.LENGTH_SHORT).show();
                    return true;

                default:
            }
            return false;
        }
    }*/

    @Override
    public int getItemCount() {
        return sourceModelList.size();
    }
}
