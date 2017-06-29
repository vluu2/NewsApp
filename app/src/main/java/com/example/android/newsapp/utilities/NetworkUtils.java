package com.example.android.newsapp.utilities;

import android.net.Uri;
import android.util.Log;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Scanner;

/**
 * Created by Vincent on 6/20/2017.
 */

public final class NetworkUtils {

    private static final String TAG = NetworkUtils.class.getSimpleName();

    private static final String NEWS_BASE_URL =  "https://newsapi.org/v1/articles?";

    /* Query parameters here */
    private final static String QUERY_PARAM_SOURCE = "source";
    private final static String source = "the-next-web";

    private final static String QUERY_PARAM_SORTBY = "sortBy";
    private final static String sortby = "latest";

    private final static String QUERY_PARAM_APIKEY = "apiKey";
    /* Insert Real API key here */
    private final static String apikey = " Insert API key here ";

    public static URL buildUrl() {
        Uri builtUri = Uri.parse(NEWS_BASE_URL).buildUpon().
                appendQueryParameter(QUERY_PARAM_SOURCE, source).
                appendQueryParameter(QUERY_PARAM_SORTBY, sortby).
                appendQueryParameter(QUERY_PARAM_APIKEY, apikey).
                build();

        URL url = null;

        try {
            url = new URL(builtUri.toString());
        }
        catch (MalformedURLException e) {
            e.printStackTrace();
        }

        Log.v(TAG, "Built URI: " + url);

        return url;
    }

    /* This method goes to the API and grabs the JSON string */
    public static String getResponseFromHttpUrl(URL url) throws IOException {
        HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
        try {
            InputStream in = urlConnection.getInputStream();

            Scanner scanner = new Scanner(in);
            scanner.useDelimiter("\\A");

            boolean hasInput = scanner.hasNext();
            if (hasInput) {
                return scanner.next();
            } else {
                return null;
            }
        } finally {
            urlConnection.disconnect();
        }
    }
}
