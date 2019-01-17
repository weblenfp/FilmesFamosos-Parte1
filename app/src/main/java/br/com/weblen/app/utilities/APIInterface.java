package br.com.weblen.app.utilities;

import br.com.weblen.app.models.MovieCollection;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

import static br.com.weblen.app.utilities.Constants.API_KEY;
import static br.com.weblen.app.utilities.Constants.API_PAGE;

public interface APIInterface {

    @GET("/3/movie/top_rated")
    Call<MovieCollection> doGetTopRatedMovies(@Query(API_KEY) String api_key, @Query(API_PAGE) String api_page);

    @GET("/3/movie/popular")
    Call<MovieCollection> doGetPopularMovies(@Query(API_KEY) String api_key, @Query(API_PAGE) String api_page);
}
