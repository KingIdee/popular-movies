package com.idee.android.popularmovies.data.provider;

import android.net.Uri;
import android.provider.BaseColumns;

/**
 * Created by idee on 5/2/17.
 */

public class MovieContract {

    public static final String CONTENT_AUTHORITY = "com.idee.android.popularmovies";
    public final static Uri BASE_CONTENT_URL = Uri.parse("content://"+ CONTENT_AUTHORITY);
    public final static String MOVIE_PATH = "movie";

    public static class MovieEntry implements BaseColumns {

        public static Uri MOVIE_URI = BASE_CONTENT_URL.buildUpon().appendPath(MOVIE_PATH).build();
        public final static String TABLE_NAME = "movie";

        public static final String COLUMN_MOVIE_AVERAGE = "average";
        public final static String COLUMN_MOVIE_TITLE = "title";
        public final static String COLUMN_MOVIE_ID = "movie_id";
        public final static String COLUMN_MOVIE_OVERVIEW = "overview";

    }

    public static class TrailersEntry implements BaseColumns {
        public final static String TABLE_NAME = "trailers";

        public final static String MOVIE_ID = "movie_id";
        public final static String KEY = "key";
    }

    public static class ReviewsEntry implements BaseColumns {
        public final static String TABLE_NAME = "reviews";

        public final static String MOVIE_ID = "movie_id";
        public final static String AUTHOR = "author";
        public final static String CONTENT = "content";
    }

}
