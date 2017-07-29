package com.example.android.newsapp;

import android.content.Intent;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.database.Cursor;
import android.net.Uri;
import android.preference.PreferenceManager;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.AsyncTaskLoader;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import com.example.android.newsapp.dispatcher.GetNews;
import com.example.android.newsapp.utilities.DatabaseUtils;
import com.example.android.newsapp.utilities.ScheduleUtilities;
import com.example.android.newsapp.model.Contract;
import com.example.android.newsapp.model.DBHelper;


public class MainActivity extends AppCompatActivity
        implements LoaderManager.LoaderCallbacks<Void>, NewsAdapter.ItemClickListener {

    static final String TAG = "mainactivity";

    private RecyclerView rv;
    private Cursor cursor;
    private NewsAdapter newsAdapter;
    private SQLiteDatabase newDB;

    private static final int NEWS_LOADER = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        rv = (RecyclerView) findViewById(R.id.recycler_view);

        rv.setLayoutManager(new LinearLayoutManager(this));

        rv.setAdapter(newsAdapter);

        // Check to see if app has been installed yet
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(this);
        boolean isFirst = sp.getBoolean("isFirst", true);

        // If first installation, create a database
        if(isFirst) {
            load();
            SharedPreferences.Editor editor = sp.edit();
            editor.putBoolean("isFirst", false);
            editor.commit();

        }

        // Begins the Job Dispatcher
        ScheduleUtilities.scheduleRefresh(this);

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
            load();
        }
        return true;
    }

    @Override
    protected void onStart() {
        super.onStart();
        newDB = new DBHelper(MainActivity.this).getReadableDatabase();
        cursor = DatabaseUtils.getAll(newDB);
        newsAdapter = new NewsAdapter(cursor, this);
        rv.setAdapter(newsAdapter);
    }

    @Override
    protected void onStop() {
        super.onStop();
        newDB.close();
        cursor.close();
    }

    @Override
    public Loader<Void> onCreateLoader(int id, final Bundle args) {
        return new AsyncTaskLoader<Void>(this) {

            @Override
            protected void onStartLoading() {
                super.onStartLoading();
            }
            @Override
            public Void loadInBackground() {
                GetNews.getArticles(MainActivity.this);
                return null;
            }
        };
    }

    @Override
    public void onLoadFinished(Loader<Void> loader, Void data) {
        newDB = new DBHelper(MainActivity.this).getReadableDatabase();
        cursor = DatabaseUtils.getAll(newDB);
        newsAdapter = new NewsAdapter(cursor, this);
        rv.setAdapter(newsAdapter);
        newsAdapter.notifyDataSetChanged();
    }

    @Override
    public void onLoaderReset(Loader<Void> loader) {}

    // Move cursor to proper position and retrieves the URL the user wants
    @Override
    public void onItemClick(Cursor cursor, int clickedItemIndex) {
        cursor.moveToPosition(clickedItemIndex);

        String url = cursor.getString(cursor.getColumnIndex(Contract.TABLE_ARTICLES.COLUMN_NAME_URL));
        Log.d(TAG, String.format("URL: %s", url));

        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setData(Uri.parse(url));
        startActivity(intent);
    }

    public void load() {
        LoaderManager lm = getSupportLoaderManager();
        lm.restartLoader(NEWS_LOADER, null, this).forceLoad();
    }

}
