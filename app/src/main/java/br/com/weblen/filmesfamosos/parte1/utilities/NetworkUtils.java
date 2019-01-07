package br.com.weblen.filmesfamosos.parte1.utilities;

import android.net.Uri;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

import br.com.weblen.filmesfamosos.parte1.BuildConfig;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class NetworkUtils {

    public static URL buildUrlTopRatedMovies() {
        return buildURL(Constants.BASE_URL_TOP_RATED_MOVIES);
    }

    public static URL buildUrlPopularMovies() {
        return buildURL(Constants.BASE_URL_POPULAR_MOVIES);
    }

    public static String buildUrlPosterW185(String posterPath) {
        return Constants.BASE_URL_POSTER_W185 + posterPath;
    }

    public static String buildUrlPosterW342(String posterPath) {
        return Constants.BASE_URL_POSTER_W342 + posterPath;
    }

    private static URL buildURL(String URL) {
        Uri builtUri = Uri.parse(URL).buildUpon()
                .appendQueryParameter(Constants.PARAM_API_KEY, BuildConfig.VALUE_API_KEY)
                .appendQueryParameter(Constants.PARAM_PAGE, Constants.VALUE_PAGE)
                .appendQueryParameter(Constants.PARAM_LANGUAGE, Constants.VALUE_LANGUAGE)
                .build();

        try {
            return new URL(builtUri.toString());

        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        return null;
    }

    @SuppressWarnings("ConstantConditions")
    public static String getResponseFromHttpUrl(URL url) throws IOException {

        Response response;


        OkHttpClient client = new OkHttpClient();

        Request request = new Request.Builder()
                .url(url)
                .build();

        response = client.newCall(request).execute();

        String finalURL = "";

        try {
            finalURL = response.body().string();
        } catch (java.lang.NullPointerException e) {
            e.printStackTrace();
        }

        return finalURL;
    }
}
