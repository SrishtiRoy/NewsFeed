package com.and.newsfeed.adapter;

import android.content.Context;
import android.net.Uri;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;


import com.and.newsfeed.R;
import com.and.newsfeed.data.ArticleModel;
import com.and.newsfeed.utils.DateUtil;
import com.bumptech.glide.Glide;

import java.util.List;

/**
 * Created by Tarek on 4/24/2015.
 */
public class NewFeedArticleRecyclerAdapter extends RecyclerView.Adapter<NewFeedArticleRecyclerAdapter.SimpleItemViewHolder> {

    private List<ArticleModel> items;
    private Context context;

    // Provide a reference to the views for each data item
    // Provide access to all the views for a data item in a view holder
    public final static class SimpleItemViewHolder extends RecyclerView.ViewHolder {
        ImageView image;
        TextView title;
        TextView subTitle;
        CardView cardView;

        public SimpleItemViewHolder(View itemView) {
            super(itemView);
            image = (ImageView) itemView.findViewById(R.id.thumbnail);
            title = (TextView) itemView.findViewById(R.id.news_title);
            subTitle = (TextView) itemView.findViewById(R.id.published_time);
            cardView = (CardView) itemView.findViewById(R.id.card_view);
        }
    }

    // Provide a suitable constructor (depends on the kind of dataset)x
    public NewFeedArticleRecyclerAdapter(Context ctx,List<ArticleModel> items) {
        this.items = items;
        this.context=ctx;
    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return this.items.size();
    }

    // Create new items (invoked by the layout manager)
    // Usually involves inflating a layout from XML and returning the holder
    @Override
    public SimpleItemViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        View itemView = LayoutInflater.from(viewGroup.getContext()).
                inflate(R.layout.blog_item, viewGroup, false);
        return new SimpleItemViewHolder(itemView);
    }

    // Replace the contents of a view (invoked by the layout manager)
    // Involves populating data into the item through holder
    @Override
    public void onBindViewHolder(SimpleItemViewHolder viewHolder, int position) {

       // viewHolder.image.setImageURI(Uri.parse(items.get(position).getUrlToImage()));
        viewHolder.title.setText(items.get(position).getTitle());
        viewHolder.subTitle.setText(DateUtil.manipulateDateFormat(items.get(position).getPublishedAt()));
        Glide
                .with(context)
                .load(items.get(position).getUrlToImage())
                .centerCrop()
                .placeholder(R.drawable.ic_action_favorite)
                .crossFade()
                .into(viewHolder.image);
       // viewHolder.cardView.setCardBackgroundColor(items.get(position).getBackGroundColor());

    }
}