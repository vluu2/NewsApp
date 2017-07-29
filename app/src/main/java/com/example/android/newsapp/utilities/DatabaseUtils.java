package com.example.android.newsapp.utilities;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.android.newsapp.model.NewsItem;

import java.util.ArrayList;

import static com.example.android.newsapp.model.Contract.TABLE_ARTICLES.*;

/**
 * Created by Vincent on 7/27/2017.
 */

public class DatabaseUtils {

    // Retrieves all items in the table by most recent update
    public static Cursor getAll(SQLiteDatabase db) {
        Cursor cursor = db.query(
                TABLE_NAME,
                null,
                null,
                null,
                null,
                null,
                COLUMN_NAME_PUBLISHED_AT + " DESC"
        );
        return cursor;
    }

    // Insert multiple articles into db at once
    public static void bulkInsert(SQLiteDatabase db, ArrayList<NewsItem> articles) {
        db.beginTransaction();
        try {
            for(NewsItem news : articles) {
                ContentValues cv = new ContentValues();
                cv.put(COLUMN_NAME_AUTHOR, news.getAuthor());
                cv.put(COLUMN_NAME_TITLE, news.getTitle());
                cv.put(COLUMN_NAME_DESCRIPTION, news.getDescription());
                cv.put(COLUMN_NAME_URL, news.getUrl());
                cv.put(COLUMN_NAME_IMGURL, news.getUrlToImage());
                cv.put(COLUMN_NAME_PUBLISHED_AT, news.getPublishedAt());
                db.insert(TABLE_NAME, null, cv);
            }
            db.setTransactionSuccessful();
        } finally {
            db.endTransaction();
            db.close();
        }
    }

    // Deletes the entire table
    public static void deleteAll(SQLiteDatabase db) {
        db.delete(TABLE_NAME, null, null);
    }

}
