package com.example.android.newsapp.dispatcher;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import com.example.android.newsapp.model.NewsItem;
import com.example.android.newsapp.model.DBHelper;
import com.example.android.newsapp.utilities.NetworkUtils;
import com.example.android.newsapp.utilities.DatabaseUtils;

import org.json.JSONException;

import java.util.ArrayList;
import java.net.URL;
import java.io.IOException;

/**
 * Created by Vincent on 7/26/2017.
 */

public class GetNews {

    // Retrieve articles from the API and store them into the database
    public static void getArticles(Context context) {

        ArrayList<NewsItem> results = null;
        URL url = NetworkUtils.buildUrl();

        SQLiteDatabase db = new DBHelper(context).getWritableDatabase();
        try {
            DatabaseUtils.deleteAll(db);

            String json = NetworkUtils.getResponseFromHttpUrl(url);
            results = NetworkUtils.parseJSON(json);

            DatabaseUtils.bulkInsert(db, results);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
