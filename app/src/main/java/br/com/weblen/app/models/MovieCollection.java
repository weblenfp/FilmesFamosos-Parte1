package br.com.weblen.app.models;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class MovieCollection implements Parcelable {

    public static final Creator<MovieCollection> CREATOR = new Creator<MovieCollection>() {
        @Override
        public MovieCollection createFromParcel(Parcel in) {
            return new MovieCollection(in);
        }

        @Override
        public MovieCollection[] newArray(int size) {
            return new MovieCollection[size];
        }
    };
    @SerializedName("results")
    private             ArrayList<Movie>         objMovieCollection;

    protected MovieCollection(Parcel in) {
        objMovieCollection = in.createTypedArrayList(Movie.CREATOR);
    }

    public MovieCollection(ArrayList<Movie> movies) {
        objMovieCollection = movies;
    }

    public ArrayList<Movie> getMovies() {
        return objMovieCollection;
    }

    public void setMovies(ArrayList<Movie> movies) {
        objMovieCollection = movies;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeValue(objMovieCollection);
    }
}
