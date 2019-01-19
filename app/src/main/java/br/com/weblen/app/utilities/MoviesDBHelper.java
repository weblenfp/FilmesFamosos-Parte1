package br.com.weblen.app.utilities;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.support.annotation.Nullable;

import br.com.weblen.app.data.MoviesContract;

public class MoviesDBHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "movies.db";
    private static final int DATABASE_VERSION          = 1;

    public MoviesDBHelper(@Nullable Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        final String CREATE_TABLE =
                "CREATE TABLE " + MoviesContract.MoviesEntry.TABLE_NAME + " (" +
                        MoviesContract.MoviesEntry._ID                      + " INTEGER PRIMARY KEY, " +
                        MoviesContract.MoviesEntry.COLUMN_MOVIE_ID          + " INTEGER NOT NULL, " +
                        MoviesContract.MoviesEntry.COLUMN_TITLE             + " TEXT NOT NULL, " +
                        MoviesContract.MoviesEntry.COLUMN_ORIGINAL_TITLE    + " TEXT NOT NULL, " +
                        MoviesContract.MoviesEntry.COLUMN_OVERVIEW          + " TEXT NOT NULL, " +
                        MoviesContract.MoviesEntry.COLUMN_ORIGINAL_LANG     + " TEXT NOT NULL, " +
                        MoviesContract.MoviesEntry.COLUMN_RELEASE_DATE      + " TEXT NOT NULL, " +
                        MoviesContract.MoviesEntry.COLUMN_POPULARITY        + " REAL NOT NULL, " +
                        MoviesContract.MoviesEntry.COLUMN_VOTE_AVERAGE      + " REAL NOT NULL, " +
                        MoviesContract.MoviesEntry.COLUMN_VOTE_COUNT        + " INTEGER NOT NULL, " +
                        MoviesContract.MoviesEntry.COLUMN_POSTER_PATH       + " TEXT NOT NULL, " +
                        MoviesContract.MoviesEntry.COLUMN_BACKDROP_PATH     + " TEXT NOT NULL); ";

        sqLiteDatabase.execSQL(CREATE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + MoviesContract.MoviesEntry.TABLE_NAME);
        onCreate(sqLiteDatabase);
    }
}
