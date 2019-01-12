package br.com.weblen.app.utilities;

import br.com.weblen.app.models.MovieCollection;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface APIInterface {

    @GET("/3/movie/top_rated")
    Call<MovieCollection> doGetTopRatedMovies(@Query("api_key") String api_key);

    @GET("/3/movie/popular")
    Call<MovieCollection> doGetPopularMovies(@Query("api_key") String api_key);
}
