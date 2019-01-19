package br.com.weblen.app.models;

import com.google.gson.annotations.SerializedName;
import java.util.ArrayList;

public class TrailerCollection {

    @SerializedName("results")
    private ArrayList<Trailer> objTrailerCollection;

    public ArrayList<Trailer> getObjTrailers() {
        return objTrailerCollection;
    }

    public ArrayList<Trailer> getTrailers() {
        return objTrailerCollection;
    }

    public void setTrailers(ArrayList<Trailer> trailers) {
        objTrailerCollection = trailers;
    }
}