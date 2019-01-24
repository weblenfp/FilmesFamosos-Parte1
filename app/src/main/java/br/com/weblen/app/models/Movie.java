package br.com.weblen.app.models;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

public class Movie implements Parcelable {

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
    @SerializedName("vote_count")
    private final       long           vote_count;
    @SerializedName("id")
    private final       long           id;
    @SerializedName("video")
    private final       boolean        video;
    @SerializedName("vote_average")
    private final       float          vote_average;
    @SerializedName("title")
    private final       String         title;
    @SerializedName("popularity")
    private final       float          popularity;
    @SerializedName("poster_path")
    private final       String         posterPath;
    @SerializedName("original_language")
    private final       String         original_language;
    @SerializedName("original_title")
    private final       String         original_title;
    @SerializedName("backdrop_path")
    private final       String         backdrop_path;
    @SerializedName("adult")
    private final       boolean        adult;
    @SerializedName("overview")
    private final       String         overview;
    @SerializedName("release_date")
    private final       String         release_date;
    private             boolean        starred;

    public Movie(long voteCount, long id, boolean video, float voteAverage, String title, float popularity, String posterPath, String originalLanguage, String originalTitle, String backdropPath, boolean adult, String overview, String releaseDate, boolean starred) {
        this.vote_count = voteCount;
        this.id = id;
        this.video = video;
        this.vote_average = voteAverage;
        this.title = title;
        this.popularity = popularity;
        this.posterPath = posterPath;
        this.original_language = originalLanguage;
        this.original_title = originalTitle;
        this.backdrop_path = backdropPath;
        this.adult = adult;
        this.overview = overview;
        this.release_date = releaseDate;
        this.starred = starred;
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
        starred = in.readByte() != 0;
    }

    public long getVoteCount() {
        return vote_count;
    }

    public long getId() {
        return id;
    }

    public boolean getVideo() {
        return video;
    }

    public float getVoteAverage() {
        return vote_average;
    }

    public String getTitle() {
        return title;
    }

    public float getPopularity() {
        return popularity;
    }

    public String getPosterPath() {
        return posterPath;
    }

    public String getOriginalLanguage() {
        return original_language;
    }

    public String getOriginalTitle() {
        return original_title;
    }

    public String getBackdropPath() {
        return backdrop_path;
    }

    public boolean getAdult() {
        return adult;
    }

    public String getOverview() {
        return overview;
    }

    public String getReleaseDate() {
        return release_date;
    }

    public Boolean isStarred() {
        return starred;
    }

    public void setStarred(boolean movieStarred) {
        starred = movieStarred;
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
        dest.writeByte((byte) (starred ? 1 : 0));
    }
}
