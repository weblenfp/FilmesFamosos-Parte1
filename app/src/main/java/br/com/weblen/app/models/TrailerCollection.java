package br.com.weblen.app.models;

import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.NonNull;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class TrailerCollection implements Parcelable {

    public static final Creator<TrailerCollection> CREATOR = new Creator<TrailerCollection>() {
        @Override
        public TrailerCollection createFromParcel(Parcel in) {
            return new TrailerCollection(in);
        }

        @Override
        public TrailerCollection[] newArray(int size) {
            return new TrailerCollection[size];
        }
    };
    @SerializedName("results")
    private             ArrayList<Trailer>         objTrailerCollection;

    private TrailerCollection(Parcel in) {
        objTrailerCollection = in.createTypedArrayList(Trailer.CREATOR);
    }

    public TrailerCollection(ArrayList<Trailer> trailers) {
        objTrailerCollection = trailers;
    }

    public ArrayList<Trailer> getTrailers() {
        return objTrailerCollection;
    }

    public void setTrailers(@NonNull ArrayList<Trailer> trailers) {
        objTrailerCollection = trailers;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeValue(objTrailerCollection);
    }
}