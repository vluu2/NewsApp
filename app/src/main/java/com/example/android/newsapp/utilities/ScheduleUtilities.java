package com.example.android.newsapp.utilities;

import android.content.Context;
import android.support.annotation.NonNull;
import com.example.android.newsapp.dispatcher.NewsJob;
import com.firebase.jobdispatcher.Constraint;
import com.firebase.jobdispatcher.Driver;
import com.firebase.jobdispatcher.FirebaseJobDispatcher;
import com.firebase.jobdispatcher.GooglePlayDriver;
import com.firebase.jobdispatcher.Job;
import com.firebase.jobdispatcher.Lifetime;
import com.firebase.jobdispatcher.Trigger;


/**
 * Created by Vincent on 7/25/2017.
 */

public class ScheduleUtilities {
    private static final int SCHEDULE_INTERVAL_MINUTES = 0;
    private static final int SYNC_FLEXTIME_SECONDS = 60;
    private static final String NEWS_JOB_TAG = "news_job_tag";

    // Checks to see if Job has been initialized
    private static boolean mInitialized;

    // schedules a timer for the page to reset after a given amount of time
    synchronized public static void scheduleRefresh(@NonNull final Context context) {
        if(mInitialized) return;

        Driver mDriver = new GooglePlayDriver(context);
        FirebaseJobDispatcher dispatcher = new FirebaseJobDispatcher(mDriver);

        // refreshes the job at at a specified time
        Job constraintRefreshJob = dispatcher.newJobBuilder()
                .setService(NewsJob.class)
                .setTag(NEWS_JOB_TAG)
                .setConstraints(Constraint.ON_ANY_NETWORK)
                .setLifetime(Lifetime.FOREVER)
                .setRecurring(true)
                .setTrigger(Trigger.executionWindow(SCHEDULE_INTERVAL_MINUTES, SCHEDULE_INTERVAL_MINUTES + SYNC_FLEXTIME_SECONDS))
                .setReplaceCurrent(true)
                .build();

        dispatcher.schedule(constraintRefreshJob);
        mInitialized = true;
    }
}
