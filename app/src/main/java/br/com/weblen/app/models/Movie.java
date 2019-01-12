package br.com.weblen.app.models;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

public class Movie implements Parcelable {

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

    @SerializedName("vote_count")
    private final long vote_count;

    @SerializedName("id")
    private final long id;

    @SerializedName("video")
    private final boolean video;

    @SerializedName("vote_average")
    private final float vote_average;

    @SerializedName("title")
    private final String title;

    @SerializedName("popularity")
    private final float popularity;

    @SerializedName("poster_path")
    private final String posterPath;

    @SerializedName("original_language")
    private final String original_language;

    @SerializedName("original_title")
    private final String original_title;

    @SerializedName("backdrop_path")
    private final String backdrop_path;

    @SerializedName("adult")
    private final boolean adult;

    @SerializedName("overview")
    private final String overview;

    @SerializedName("release_date")
    private final String release_date;

    @SuppressWarnings("unused")
    public static final Creator<Movie> CREATOR = new Creator<Movie>() {
        @Override
        public Movie createFromParcel(Parcel in) {
            return new Movie(in);
        }

        @Override
        public Movie[] newArray(int size) {
            return new Movie[size];
        }
    };

    public float getVoteAverage() {
        return vote_average;
    }

    public String getTitle() {
        return title;
    }

    public String getPosterPath() {
        return posterPath;
    }

    public String getOverview() {
        return overview;
    }

    public String getReleaseDate() {
        return release_date;
    }


    private Movie(Parcel in) {
        vote_count = in.readLong();
        id = in.readLong();
        video = in.readByte() != 0;
        vote_average = in.readFloat();
        title = in.readString();
        popularity = in.readFloat();
        posterPath = in.readString();
        original_language = in.readString();
        original_title = in.readString();
        backdrop_path = in.readString();
        adult = in.readByte() != 0;
        overview = in.readString();
        release_date = in.readString();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(vote_count);
        dest.writeLong(id);
        dest.writeByte((byte) (video ? 1 : 0));
        dest.writeFloat(vote_average);
        dest.writeString(title);
        dest.writeFloat(popularity);
        dest.writeString(posterPath);
        dest.writeString(original_language);
        dest.writeString(original_title);
        dest.writeString(backdrop_path);
        dest.writeByte((byte) (adult ? 1 : 0));
        dest.writeString(overview);
        dest.writeString(release_date);
    }
}
