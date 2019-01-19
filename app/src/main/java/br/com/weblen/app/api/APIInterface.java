package br.com.weblen.app.api;

import br.com.weblen.app.models.MovieCollection;
import br.com.weblen.app.models.TrailerCollection;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

import static br.com.weblen.app.utilities.Constants.API_KEY;
import static br.com.weblen.app.utilities.Constants.API_PAGE;
import static br.com.weblen.app.utilities.Constants.MOVIE_ID;

public interface APIInterface {

    @GET("/3/movie/top_rated")
    Call<MovieCollection> doGetTopRatedMovies(@Query(API_KEY) String api_key, @Query(API_PAGE) String api_page);

    @GET("/3/movie/popular")
    Call<MovieCollection> doGetPopularMovies(@Query(API_KEY) String api_key, @Query(API_PAGE) String api_page);

    @GET("3/movie/{id}/videos")
    Call<TrailerCollection> doGetMovieTrailers(@Path(value = MOVIE_ID, encoded = true) String movieId, @Query(API_KEY) String api_key);

    //@GET("/movie/{id}/reviews")
    //Call<MovieCollection> doGetMovieReviews(@Query(API_KEY) String api_key);
}
