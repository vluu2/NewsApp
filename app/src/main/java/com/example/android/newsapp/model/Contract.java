package com.example.android.newsapp.model;

import android.provider.BaseColumns;

/**
 * Created by Vincent on 7/27/2017.
 */

public class Contract {

    public static class TABLE_ARTICLES implements BaseColumns {

        // Name of the table
        public static final String TABLE_NAME = "newsarticles";

        // Names of the attributes in the table
        public static final String COLUMN_NAME_TITLE = "title";
        public static final String COLUMN_NAME_AUTHOR = "author";
        public static final String COLUMN_NAME_PUBLISHED_AT = "published_date";
        public static final String COLUMN_NAME_DESCRIPTION = "description";
        public static final String COLUMN_NAME_URL = "article_url";
        public static final String COLUMN_NAME_IMGURL = "image_url";

    }

}
