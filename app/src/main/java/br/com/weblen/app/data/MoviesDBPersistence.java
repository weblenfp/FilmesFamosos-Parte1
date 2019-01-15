package br.com.weblen.app.data;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;

import br.com.weblen.app.models.Movie;
import br.com.weblen.app.utilities.Constants;

public class MoviesDBPersistence {

    public static void onPersistMovieDB(Movie movie, Context context) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(MoviesContract.MoviesEntry.COLUMN_TITLE, movie.getTitle());
        contentValues.put(MoviesContract.MoviesEntry.COLUMN_MOVIE_ID, movie.getId());
        contentValues.put(MoviesContract.MoviesEntry.COLUMN_ORIGINAL_TITLE, movie.getOriginalTitle());
        contentValues.put(MoviesContract.MoviesEntry.COLUMN_OVERVIEW, movie.getOverview());
        contentValues.put(MoviesContract.MoviesEntry.COLUMN_ORIGINAL_LANG, movie.getOriginalLanguage());
        contentValues.put(MoviesContract.MoviesEntry.COLUMN_RELEASE_DATE, movie.getReleaseDate());
        contentValues.put(MoviesContract.MoviesEntry.COLUMN_POPULARITY, movie.getPopularity());
        contentValues.put(MoviesContract.MoviesEntry.COLUMN_VOTE_AVARAGE, movie.getVoteAverage());
        contentValues.put(MoviesContract.MoviesEntry.COLUMN_VOTE_COUNT, movie.getVoteCount());
        contentValues.put(MoviesContract.MoviesEntry.COLUMN_POSTER_PATH, movie.getPosterPath());
        contentValues.put(MoviesContract.MoviesEntry.COLUMN_BACKDROP_PATH, movie.getBackdropPath());
        context.getContentResolver().insert(MoviesContract.MoviesEntry.CONTENT_URI, contentValues);
    }

    public static boolean isInStorage(String movieId, Context context) {
        Cursor cursor = context.getContentResolver().query(MoviesContract.MoviesEntry.CONTENT_URI, null,
                MoviesContract.MoviesEntry.COLUMN_MOVIE_ID + " = '" + movieId + "'",
                null, null);
        return cursor != null && cursor.getCount() > 0;
    }

    public static void onDisfavor(Movie movie, Context context) {
        if (movie == null || context == null) return;
        long id = movie.getId();
        String idString = Long.toString(id);
        Uri uri = MoviesContract.MoviesEntry.CONTENT_URI;
        uri = uri.buildUpon().appendPath(idString).build();

        context.getContentResolver().delete(uri, null, null);
    }

    public static Movie fetchMovieDB(Cursor cursor, int index) {
        Movie movie = null;
        if (cursor != null && cursor.getColumnCount() > 0) {
            cursor.moveToPosition(index);
            int idIndex = cursor.getColumnIndex(MoviesContract.MoviesEntry.COLUMN_MOVIE_ID);
            int titleIndex = cursor.getColumnIndex(MoviesContract.MoviesEntry.COLUMN_TITLE);
            int originalTitleIndex = cursor.getColumnIndex(MoviesContract.MoviesEntry.COLUMN_ORIGINAL_TITLE);
            int overviewIndex = cursor.getColumnIndex(MoviesContract.MoviesEntry.COLUMN_OVERVIEW);
            int originalLangIndex = cursor.getColumnIndex(MoviesContract.MoviesEntry.COLUMN_ORIGINAL_LANG);
            int releaseDateIndex = cursor.getColumnIndex(MoviesContract.MoviesEntry.COLUMN_RELEASE_DATE);
            int popularityIndex = cursor.getColumnIndex(MoviesContract.MoviesEntry.COLUMN_POPULARITY);
            int voteAverageIndex = cursor.getColumnIndex(MoviesContract.MoviesEntry.COLUMN_VOTE_AVARAGE);
            int voteCountIndex = cursor.getColumnIndex(MoviesContract.MoviesEntry.COLUMN_VOTE_COUNT);
            int posterPathIndex = cursor.getColumnIndex(MoviesContract.MoviesEntry.COLUMN_POSTER_PATH);
            int backdropPathIndex = cursor.getColumnIndex(MoviesContract.MoviesEntry.COLUMN_BACKDROP_PATH);


            String title = cursor.getString(titleIndex);
            String originalTitle = cursor.getString(originalTitleIndex);
            String overview = cursor.getString(overviewIndex);
            String originalLang = cursor.getString(originalLangIndex);
            String releaseDate = cursor.getString(releaseDateIndex);
            float popularity = cursor.getFloat(popularityIndex);
            float voteAverage = cursor.getFloat(voteAverageIndex);
            int voteCount = cursor.getInt(voteCountIndex);
            String posterPath = cursor.getString(posterPathIndex);
            String backdropPath = cursor.getString(backdropPathIndex);
            long id = cursor.getLong(idIndex);

            movie = new Movie(voteCount, id, true, voteAverage, title, popularity, posterPath, originalLang, originalTitle,
                    backdropPath, false, overview, releaseDate, true);
        }

        return movie;
    }
}
