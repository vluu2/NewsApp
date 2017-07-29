package com.example.android.newsapp.dispatcher;

import com.firebase.jobdispatcher.JobService;
import com.firebase.jobdispatcher.JobParameters;
import android.os.AsyncTask;
import android.widget.Toast;

/**
 * Created by Vincent on 7/26/2017.
 */

public class NewsJob extends JobService {
    private AsyncTask mBackgroundTask;

    @Override
    public boolean onStartJob(final JobParameters job) {
        mBackgroundTask = new AsyncTask() {

            // Lets user know that the activity has been refreshed
            @Override
            protected void onPreExecute() {
                Toast.makeText(NewsJob.this, "News Refreshed!", Toast.LENGTH_SHORT).show();
                super.onPreExecute();
            }
            @Override
            protected Object doInBackground(Object[] objects) {
                GetNews.getArticles(NewsJob.this);
                return null;
            }
            @Override
            protected void onPostExecute(Object object) {
                jobFinished(job, false);
                super.onPostExecute(object);
            }
        };

        mBackgroundTask.execute();

        return true;
    }

    // Stop the job based on conditions
    @Override
    public boolean onStopJob(JobParameters job) {
        if(mBackgroundTask != null) {
            mBackgroundTask.cancel(false);
        }
        return true;
    }
}
