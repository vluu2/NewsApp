package com.example.android.newsapp;

import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.os.Bundle;
import android.os.AsyncTask;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import com.example.android.newsapp.model.NewsItem;
import com.example.android.newsapp.utilities.NetworkUtils;

import java.net.URL;
import java.io.IOException;
import java.util.ArrayList;

import org.json.JSONException;

public class MainActivity extends AppCompatActivity {

    static final String TAG = "mainactivity";
    private RecyclerView rv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        rv = (RecyclerView) findViewById(R.id.recycler_view);

        rv.setLayoutManager(new LinearLayoutManager(this));

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        /* Use AppCompatActivity's method getMenuInflater to get a handle on the menu inflater */
        MenuInflater inflater = getMenuInflater();
        /* Use the inflater's inflate method to inflate our menu layout to this menu */
        inflater.inflate(R.menu.menu, menu);
        /* Return true so that the menu is displayed in the Toolbar */
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.search) {
            FetchNewsTask task = new FetchNewsTask();
            task.execute();
        }
        return true;
    }

    class FetchNewsTask extends AsyncTask<URL, Void, ArrayList<NewsItem>> {

        String searchQuery = "the-next-web";
        String sortBy = "latest";
        // TODO: Insert Working API Key here
        String apiKey = "";

        @Override
        protected ArrayList<NewsItem> doInBackground(URL... params) {

            ArrayList<NewsItem> newsList = null;
            URL newsUrl = NetworkUtils.buildUrl(searchQuery, sortBy, apiKey);
            Log.d(TAG, "url: " + newsUrl.toString());

            try {
                String jsonResponse = NetworkUtils.getResponseFromHttpUrl(newsUrl);
                newsList = NetworkUtils.parseJSON(jsonResponse);

            } catch (IOException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            }

            return newsList;
        }

        @Override
        protected void onPostExecute(final ArrayList<NewsItem> listofNews) {
            super.onPostExecute(listofNews);

            if (listofNews != null) {
                NewsAdapter adapter = new NewsAdapter(listofNews, new NewsAdapter.ItemClickListener() {
                    @Override
                    public void onItemClick(int clickedItemIndex) {
                        String url = listofNews.get(clickedItemIndex).getUrl();

                        Log.d(TAG, String.format("URL CLICKED: %s", url));

                    }
                });
                rv.setAdapter(adapter);
            }
        }
    }
}
