package com.example.android.newsapp;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.android.newsapp.model.NewsItem;

import java.util.ArrayList;

/**
 * Created by Vincent on 6/29/2017.
 */

public class NewsAdapter extends RecyclerView.Adapter<NewsAdapter.ItemHolder> {

    private ArrayList<NewsItem> newsItems;
    ItemClickListener listener;

    public NewsAdapter(ArrayList<NewsItem> newsItems, ItemClickListener listener) {
        this.newsItems = newsItems;
        this.listener = listener;
    }

    public interface ItemClickListener {
        void onItemClick(int clickedItemIndex);
    }

    // Displays each ViewHolders on screen
    @Override
    public ItemHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // Retrieves context of the current activity and displays to screen
        Context context = parent.getContext();

        // Retrieves views from the xml file
        LayoutInflater inflater = LayoutInflater.from(context);

        // True -> No changes to view
        // False -> child views go on top
        boolean shouldAttachToParentImmediately = false;

        View view = inflater.inflate(R.layout.news_articles, parent, shouldAttachToParentImmediately);

        ItemHolder holder = new ItemHolder(view);

        return holder;
    }

    // Displays information of the article on screen at a specific position
    @Override
    public void onBindViewHolder(ItemHolder holder, int position) {
        holder.bind(position);
    }

    @Override
    public int getItemCount() {
        return newsItems.size();
    }

    public class ItemHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView mTitleText;
        TextView mDescriptionText;
        TextView mTimeText;

        public ItemHolder(View view) {
            super(view);

            // Gets references of id's from article.xml file
            mTitleText = (TextView) view.findViewById(R.id.title);
            mDescriptionText = (TextView) view.findViewById(R.id.description);
            mTimeText = (TextView) view.findViewById(R.id.time);

            view.setOnClickListener(this);
        }

        // Retrieves position of a specific article and setups the views of that article
        public void bind(int pos) {
            // Retrieves position of specific article / item
            NewsItem news = newsItems.get(pos);

            // Sets the text of each view in specific positions on screen
            mTitleText.setText(news.getTitle());
            mDescriptionText.setText(news.getDescription());
            mTimeText.setText(news.getPublishedAt());
        }

        @Override
        public void onClick(View v) {
            int pos = getAdapterPosition();
            listener.onItemClick(pos);
        }
    }
}