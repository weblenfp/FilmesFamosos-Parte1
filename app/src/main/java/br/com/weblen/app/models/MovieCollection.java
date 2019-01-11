package br.com.weblen.app.models;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

@SuppressWarnings("unused")
public class MovieCollection {

    /*JSON Example
      "page": 1,
      "total_results": 7814,
      "total_pages": 391,
      "results": [
        {
          "vote_count": 982,
          "id": 19404,
          "video": false,
          "vote_average": 9.1,
          "title": "Dilwale Dulhania Le Jayenge",
          "popularity": 30.918332,
          "poster_path": "/uC6TTUhPpQCmgldGyYveKRAu8JN.jpg",
          "original_language": "hi",
          "original_title": "Dilwale Dulhania Le Jayenge",
          "genre_ids": [
            35,
            18,
            10749
          ],
          "backdrop_path": "/nl79FQ8xWZkhL3rDr1v2RFFR6J0.jpg",
          "adult": false,
          "overview": "",
          "release_date": "1995-10-20"
        }
     */

    @SerializedName("results")
    private ArrayList<Movie> objMovieCollection;

    public ArrayList<Movie> getObjMovies() {
        return objMovieCollection;
    }
}
