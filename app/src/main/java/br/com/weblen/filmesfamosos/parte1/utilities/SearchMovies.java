package br.com.weblen.filmesfamosos.parte1.utilities;

import android.os.AsyncTask;

import com.google.gson.Gson;

import java.io.IOException;
import java.net.URL;

import br.com.weblen.filmesfamosos.parte1.models.MovieCollection;

public class SearchMovies extends AsyncTask<URL, Void, MovieCollection> {

    private final AsyncTaskSearchMovies delegate;
    private       MovieCollection       movieCollection;

    public SearchMovies(AsyncTaskSearchMovies asyncTaskDelegate, MovieCollection movies){
        this.delegate = asyncTaskDelegate;
        this.movieCollection = movies;
    }

    @Override
    public MovieCollection doInBackground(URL... urls) {

        String jsonResult;

        try {
            jsonResult = NetworkUtils.getResponseFromHttpUrl(urls[0]);
            movieCollection = new Gson().fromJson(jsonResult, MovieCollection.class);
        } catch (IOException e) {
            e.printStackTrace();
        }

        return movieCollection;
    }

    @Override
    public void onPostExecute(MovieCollection movies) {
        super.onPostExecute(movies);

        if (movies != null) {
            try {
                delegate.processFinish(movies);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
