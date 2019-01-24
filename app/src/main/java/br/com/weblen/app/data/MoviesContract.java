package br.com.weblen.app.data;

import android.net.Uri;
import android.provider.BaseColumns;

import br.com.weblen.app.utilities.Constants;

public class MoviesContract {

    public static final class MoviesEntry implements BaseColumns {

        public static final Uri CONTENT_URI = Constants.BASE_CONTENT_URI.buildUpon().appendPath(Constants.PATH_MOVIES).build();

        public static final String TABLE_NAME = "movies";
        public static final String COLUMN_VOTE_COUNT = "vote_count";
        public static final String COLUMN_MOVIE_ID = "movie_id";
        public static final String COLUMN_VOTE_AVERAGE = "vote_average";
        public static final String COLUMN_TITLE = "title";
        public static final String COLUMN_POPULARITY = "popularity";
        public static final String COLUMN_POSTER_PATH = "poster_path";
        public static final String COLUMN_ORIGINAL_LANG = "original_language";
        public static final String COLUMN_ORIGINAL_TITLE = "original_title";
        public static final String COLUMN_BACKDROP_PATH = "backdrop_path";
        public static final String COLUMN_OVERVIEW = "overview";
        public static final String COLUMN_RELEASE_DATE = "release_date";
    }
}
