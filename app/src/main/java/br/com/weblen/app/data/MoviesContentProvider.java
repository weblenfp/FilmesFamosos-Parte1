package br.com.weblen.app.data;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import br.com.weblen.app.utilities.Constants;
import br.com.weblen.app.utilities.MoviesDBHelper;

import static br.com.weblen.app.data.MoviesContract.MoviesEntry.TABLE_NAME;

public class MoviesContentProvider extends ContentProvider {

    private static final int            MOVIES         = 300;
    private static final int            MOVIES_WITH_ID = 301;
    private static final UriMatcher     sUriMatcher    = buildUriMatcher();
    private              MoviesDBHelper mMoviesDbHelper;

    private static UriMatcher buildUriMatcher() {
        UriMatcher uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);

        String convention = "/#";
        String path       = Constants.PATH_MOVIES + convention;

        uriMatcher.addURI(Constants.AUTHORITY, Constants.PATH_MOVIES, MOVIES);
        uriMatcher.addURI(Constants.AUTHORITY, path, MOVIES_WITH_ID);

        return uriMatcher;
    }

    @Override
    public boolean onCreate() {
        Context context = getContext();
        mMoviesDbHelper = new MoviesDBHelper(context);
        return true;
    }

    @Nullable
    @Override
    public Cursor query(@NonNull Uri uri, @Nullable String[] projection, @Nullable String selection, @Nullable String[] selectionArgs, @Nullable String sortOrder) {

        final SQLiteDatabase db = mMoviesDbHelper.getReadableDatabase();

        int    match = sUriMatcher.match(uri);
        Cursor cursor;

        switch (match) {
            case MOVIES:
                cursor = db.query(TABLE_NAME,
                        projection,
                        selection,
                        selectionArgs,
                        null,
                        null,
                        sortOrder);
                break;
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }

        // Set a notification URI on the Cursor and return that Cursor
        //noinspection ConstantConditions
        cursor.setNotificationUri(getContext().getContentResolver(), uri);

        // Return the desired Cursor
        return cursor;
    }

    @Nullable
    @Override
    public String getType(@NonNull Uri uri) {
        return null;
    }

    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri, @Nullable ContentValues values) {
        final SQLiteDatabase db    = mMoviesDbHelper.getWritableDatabase();
        int                  match = sUriMatcher.match(uri);
        Uri                  returnUri;

        switch (match) {
            case MOVIES:
                long id = db.insert(TABLE_NAME, null, values);
                if (id > 0) {
                    returnUri = ContentUris.withAppendedId(MoviesContract.MoviesEntry.CONTENT_URI, id);
                } else {
                    throw new android.database.SQLException("Failed to insert row into " + uri);
                }
                break;
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }

        //noinspection ConstantConditions
        getContext().getContentResolver().notifyChange(uri, null);
        return returnUri;
    }

    @Override
    public int delete(@NonNull Uri uri, @Nullable String selection, @Nullable String[] selectionArgs) {
        final SQLiteDatabase db    = mMoviesDbHelper.getWritableDatabase();
        int                  match = sUriMatcher.match(uri);


        int movieDeleted; // starts as 0

        // Write the code to delete a single row of data
        // [Hint] Use selections to delete an item by its row ID
        switch (match) {
            // Handle the single item case, recognized by the ID included in the URI path
            case MOVIES_WITH_ID:
                String id = uri.getPathSegments().get(1);
                // Use selections/selectionArgs to filter for this ID
                movieDeleted = db.delete(TABLE_NAME, MoviesContract.MoviesEntry.COLUMN_MOVIE_ID + "=?", new String[]{id});
                break;
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }

        // Notify the resolver of a change and return the number of items deleted
        if (movieDeleted != 0) {
            // A task was deleted, set notification
            //noinspection ConstantConditions
            getContext().getContentResolver().notifyChange(uri, null);
        }

        // Return the number of tasks deleted
        return movieDeleted;
    }

    @Override
    public int update(@NonNull Uri uri, @Nullable ContentValues values, @Nullable String selection, @Nullable String[] selectionArgs) {
        return 0;
    }
}