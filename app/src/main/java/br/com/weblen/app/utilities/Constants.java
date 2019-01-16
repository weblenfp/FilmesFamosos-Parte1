package br.com.weblen.app.utilities;

import android.net.Uri;

public final class Constants {
    public static final String BASE_URL             = "https://api.themoviedb.org/";
    public static final String BASE_URL_POSTER_W185 = "http://image.tmdb.org/t/p/w185/";
    public static final String BASE_URL_POSTER_W342 = "http://image.tmdb.org/t/p/w342/";
    public static final String MOVIES               = "movies";

    public static final String AUTHORITY        = "br.com.weblen.app";
    public static final String PATH_MOVIES      = "movies";
    public static final Uri    BASE_CONTENT_URI = Uri.parse("content://" + AUTHORITY);

    public class searchType {
        public static final int BY_POPULAR   = 0;
        public static final int BY_TOP_RATED = 1;
    }
}
