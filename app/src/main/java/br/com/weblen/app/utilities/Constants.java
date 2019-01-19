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
    public static final String API_KEY          = "api_key";
    public static final String API_PAGE         = "page";
    public static final String MOVIE_ID         = "id";

    public static final String error_message         = "Ocorreu um problema com sua requisição!";
    public static final String not_found_message     = "Nenhum filme encontrado!";
    public static final String no_connection_message = "Sem conexão";

    public static final String YOUTUBE_URL = "https://www.youtube.com/watch?v=";
}
