package br.com.weblen.app.views;

import android.content.Intent;
import android.database.Cursor;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

import br.com.weblen.app.R;
import br.com.weblen.app.adapters.MoviesAdapter;
import br.com.weblen.app.adapters.MoviesCursorAdapter;
import br.com.weblen.app.api.APIClient;
import br.com.weblen.app.api.APIInterface;
import br.com.weblen.app.api.ApiTypes;
import br.com.weblen.app.data.MoviesContract;
import br.com.weblen.app.data.MoviesDBPersistence;
import br.com.weblen.app.models.Movie;
import br.com.weblen.app.models.MovieCollection;
import br.com.weblen.app.utilities.Constants;
import br.com.weblen.app.utilities.EndlessRecyclerViewScrollListener;
import br.com.weblen.app.utilities.Helper;
import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static br.com.weblen.app.BuildConfig.VALUE_API_KEY;
import static br.com.weblen.app.api.ApiTypes.BY_POPULAR;
import static br.com.weblen.app.api.ApiTypes.BY_STARRED;
import static br.com.weblen.app.api.ApiTypes.BY_TOP_RATED;

public class MainActivity extends AppCompatActivity implements MoviesAdapter.MoviesAdapterClickListener,
        MoviesCursorAdapter.MoviesCursorAdapterClickListener {

    private static ApiTypes currentApiType = BY_POPULAR;
    @BindView(R.id.rv_movies)
    RecyclerView mRecyclerView;
    @BindView(R.id.tv_error_message_display)
    TextView     mErrorMessage;
    @BindView(R.id.pb_loading_indicator)
    ProgressBar  mProgressBar;
    private MovieCollection     mMovies          = null;
    private MoviesAdapter       mMoviesAdapter;
    private MoviesCursorAdapter mMoviesCursorAdapter;
    private ArrayList<Movie>    mMoviesArray     = new ArrayList<>();
    private MovieCollection     mMovieCollection = new MovieCollection(mMoviesArray);
    private Integer             currentApiPage   = 1;

    @Override
    protected void onPostResume() {
        super.onPostResume();
        if (currentApiType == BY_STARRED) {
            mMoviesArray.clear();
            showProgressBar();
            fetchMovies(currentApiType);
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {

        super.onSaveInstanceState(outState);
        outState.putParcelable(Constants.MOVIE_COLLECTION, mMovieCollection);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {

        super.onRestoreInstanceState(savedInstanceState);
        MovieCollection mMovieCollection = null;

        if (savedInstanceState != null) {
            if (savedInstanceState.containsKey(Constants.MOVIE_COLLECTION))
                mMovieCollection = savedInstanceState.getParcelable(Constants.MOVIE_COLLECTION);

            processFinish(mMovieCollection);
        }
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        if (Helper.isInternetAvailable(this) == false)
            showMessage(Constants.no_connection_message);

        int               spanCount         = 2;
        GridLayoutManager gridLayoutManager = new GridLayoutManager(this, spanCount);
        mRecyclerView.setLayoutManager(gridLayoutManager);

        mRecyclerView.addOnScrollListener(new EndlessRecyclerViewScrollListener(gridLayoutManager) {
            @Override
            public void onLoadMore(int page, int totalItemsCount, RecyclerView view) {
                // Triggered only when new data needs to be appended to the list
                // Add whatever code is needed to append new items to the bottom of the list
                loadNextDataFromApi(page);
            }
        });

        mMoviesArray.clear();

        mRecyclerView.setHasFixedSize(true);

        mMoviesAdapter = new MoviesAdapter(this);
        mMoviesCursorAdapter = new MoviesCursorAdapter(this);

        if (currentApiType == BY_STARRED) {
            mRecyclerView.setAdapter(mMoviesCursorAdapter);
        } else {
            mRecyclerView.setAdapter(mMoviesAdapter);
        }

        mMoviesAdapter = new MoviesAdapter(this);
        mRecyclerView.setAdapter(mMoviesAdapter);

        showProgressBar();

        fetchMovies(currentApiType);
    }

    private void loadNextDataFromApi(int page) {
        currentApiPage = ++page;
        if (currentApiType != BY_STARRED) fetchMovies(currentApiType);
    }

    private void fetchMovies(ApiTypes paramSearchType) {

        APIInterface          apiInterface = APIClient.getClient().create(APIInterface.class);
        Call<MovieCollection> call;
        currentApiType = paramSearchType;

        switch (currentApiType) {
            case BY_POPULAR:
                call = apiInterface.doGetPopularMovies(VALUE_API_KEY, currentApiPage.toString());
                break;
            case BY_TOP_RATED:
                call = apiInterface.doGetTopRatedMovies(VALUE_API_KEY, currentApiPage.toString());
                break;
            case BY_STARRED:
                processFinish(getMoviesStarred());

            default:
                return;
        }

        call.enqueue(new Callback<MovieCollection>() {
            @Override
            public void onResponse(@NonNull Call<MovieCollection> call, @NonNull Response<MovieCollection> response) {
                Log.d("TAG", response.code() + "");
                MovieCollection responseMovies = response.body();
                processFinish(responseMovies);
            }

            @Override
            public void onFailure(@NonNull Call<MovieCollection> call, @NonNull Throwable t) {
                call.cancel();
                showMessage(Constants.error_message);
            }
        });
    }

    @NotNull
    private MovieCollection getMoviesStarred() {
        Cursor           mMoviesCursor     = null;
        ArrayList<Movie> mMovieArray       = new ArrayList<>();
        MovieCollection  mMoviesCollection = new MovieCollection(mMovieArray);

        mMoviesCursor = getContentResolver().query(MoviesContract.MoviesEntry.CONTENT_URI,
                null,
                null,
                null,
                null);

        mMoviesCursor.moveToFirst();

        while (mMoviesCursor.moveToNext()) {
            mMovieArray.add(MoviesDBPersistence.cursorToMovieObject(mMoviesCursor, mMoviesCursor.getPosition()));
        }

        mMoviesCollection.setMovies(mMovieArray);

        if (mMoviesCollection.getMovies().size() == 0) showMessage(Constants.not_found_message);

        return mMoviesCollection;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int selectedMenuId = item.getItemId();
        mMoviesArray.clear();
        currentApiType = BY_POPULAR;
        currentApiPage = 1;

        if (selectedMenuId != R.id.menu_starred && Helper.isInternetAvailable(this) == false) {
            showMessage(Constants.no_connection_message);
            return false;
        }

        try {
            switch (selectedMenuId) {

                case R.id.menu_popularity:
                    showProgressBar();
                    fetchMovies(BY_POPULAR);
                    break;
                case R.id.menu_rating:
                    showProgressBar();
                    fetchMovies(BY_TOP_RATED);
                    break;
                case R.id.menu_starred:
                    showProgressBar();
                    fetchMovies(BY_STARRED);
                    break;
                default:
                    break;
            }
            mRecyclerView.scrollToPosition(0);

        } catch (Exception e) {
            showMessage(Constants.error_message);
        }

        return super.onOptionsItemSelected(item);
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
    @Override
    public void OnClick(Movie movie) {
        Intent intent = new Intent(this, MovieDetailActivity.class);
        intent.putExtra(Intent.EXTRA_REFERRER, movie);
        startActivity(intent);
    }

    private void processFinish(MovieCollection movies) {

        if (movies != null && movies.getMovies().size() > 0) {
            showRecyclerView();
            if (mMoviesArray != null && mMoviesArray.isEmpty()) {
                mMoviesArray = movies.getMovies();
                mMoviesAdapter.setMoviesData(mMoviesArray);
                mMovieCollection.setMovies(mMoviesArray);
                mMoviesAdapter.notifyDataSetChanged();
            } else {
                int positionStart = mMoviesAdapter.getItemCount();
                mMoviesArray.addAll(movies.getMovies());
                mMovieCollection.setMovies(mMoviesArray);
                int itemCount = mMoviesArray.size() - 1;
                mMoviesAdapter.notifyItemRangeInserted(positionStart, itemCount);
            }
        } else {
            showMessage(Constants.error_message);
        }
    }

    private void showRecyclerView() {
        mProgressBar.setVisibility(View.INVISIBLE);
        mErrorMessage.setVisibility(View.INVISIBLE);
        mRecyclerView.setVisibility(View.VISIBLE);
        mErrorMessage.setText("");
    }

    private void showProgressBar() {
        mProgressBar.setVisibility(View.VISIBLE);
        mErrorMessage.setVisibility(View.INVISIBLE);
        mRecyclerView.setVisibility(View.INVISIBLE);
    }

    private void showMessage(String message) {
        mProgressBar.setVisibility(View.INVISIBLE);
        mRecyclerView.setVisibility(View.INVISIBLE);
        mErrorMessage.setVisibility(View.VISIBLE);
        if (mErrorMessage.getText().toString().trim().equals(""))
            mErrorMessage.setText(message);
    }

    @Override
    public void onClick(Movie movie) {
        Intent intent = new Intent(this, MovieDetailActivity.class);
        intent.putExtra(Intent.EXTRA_REFERRER, movie);
        startActivity(intent);
    }
}