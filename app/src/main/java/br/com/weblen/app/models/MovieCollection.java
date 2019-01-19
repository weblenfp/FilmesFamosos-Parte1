package br.com.weblen.app.models;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

@SuppressWarnings("unused")
public class MovieCollection {

    @SerializedName("results")
    private ArrayList<Movie> objMovieCollection;

    public ArrayList<Movie> getObjMovies() {
        return objMovieCollection;
    }

    public ArrayList<Movie> getMovies() {
        return objMovieCollection;
    }

    public void setMovies(ArrayList<Movie> movies) { objMovieCollection = movies; }
}
