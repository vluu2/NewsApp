package com.example.android.newsapp;

import android.content.Context;
import android.database.Cursor;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.android.newsapp.model.Contract;
import com.example.android.newsapp.model.NewsItem;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * Created by Vincent on 6/29/2017.
 */

public class NewsAdapter extends RecyclerView.Adapter<NewsAdapter.ItemHolder> {

    private ArrayList<NewsItem> newsItems;
    ItemClickListener listener;
    Cursor cursor;
    private Context context;


    public NewsAdapter(Cursor cursor, ItemClickListener listener) {
        this.cursor = cursor;
        this.listener = listener;
    }

    public interface ItemClickListener {
        void onItemClick(Cursor cursor, int clickedItemIndex);
    }

    @Override
    public ItemHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        context = parent.getContext();

        LayoutInflater inflater = LayoutInflater.from(context);

        boolean shouldAttachToParentImmediately = false;

        View view = inflater.inflate(R.layout.news_articles, parent, shouldAttachToParentImmediately);

        ItemHolder holder = new ItemHolder(view);

        return holder;
    }

    @Override
    public void onBindViewHolder(ItemHolder holder, int position) {
        holder.bind(position);
    }

    @Override
    public int getItemCount() {
        // Return number of rows in the cursor
        return cursor.getCount();
    }

    public class ItemHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView mTitleText;
        TextView mDescriptionText;
        TextView mTimeText;
        ImageView imageView;

        public ItemHolder(View view) {
            super(view);

            mTitleText = (TextView) view.findViewById(R.id.title);
            mDescriptionText = (TextView) view.findViewById(R.id.description);
            mTimeText = (TextView) view.findViewById(R.id.time);
            imageView = (ImageView) view.findViewById(R.id.image);

            view.setOnClickListener(this);
        }

        public void bind(int pos) {
            // Retrieves position of the article
            cursor.moveToPosition(pos);

            mTitleText.setText(cursor.getString(cursor.getColumnIndex(Contract.TABLE_ARTICLES.COLUMN_NAME_TITLE)));
            mDescriptionText.setText(cursor.getString(cursor.getColumnIndex(Contract.TABLE_ARTICLES.COLUMN_NAME_DESCRIPTION)));
            mTimeText.setText(cursor.getString(cursor.getColumnIndex(Contract.TABLE_ARTICLES.COLUMN_NAME_PUBLISHED_AT)));

            Picasso.with(context).load(cursor.getString(cursor.getColumnIndex(Contract.TABLE_ARTICLES.COLUMN_NAME_IMGURL)));
        }

        @Override
        public void onClick(View v) {
            int pos = getAdapterPosition();
            listener.onItemClick(cursor, pos);
        }
    }
}