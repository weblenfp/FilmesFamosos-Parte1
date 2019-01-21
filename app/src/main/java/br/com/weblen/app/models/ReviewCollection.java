package br.com.weblen.app.models;

import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.NonNull;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class ReviewCollection implements Parcelable {

    public static final Creator<ReviewCollection> CREATOR = new Creator<ReviewCollection>() {
        @Override
        public ReviewCollection createFromParcel(Parcel in) {
            return new ReviewCollection(in);
        }

        @Override
        public ReviewCollection[] newArray(int size) {
            return new ReviewCollection[size];
        }
    };
    @SerializedName("results")
    private             ArrayList<ReviewResult>   objReviewCollection;

    public ReviewCollection(ArrayList<ReviewResult> reviews) {
        objReviewCollection = reviews;
    }

    protected ReviewCollection(Parcel in) {
        objReviewCollection = in.createTypedArrayList(ReviewResult.CREATOR);
    }

    public ArrayList<ReviewResult> getReviews() {
        return objReviewCollection;
    }

    public void setReviews(@NonNull ArrayList<ReviewResult> reviews) {
        objReviewCollection = reviews;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeValue(objReviewCollection);
    }
}
