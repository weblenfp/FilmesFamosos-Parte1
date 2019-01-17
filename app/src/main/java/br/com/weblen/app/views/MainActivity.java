package br.com.weblen.app.views;

import android.content.Intent;
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

import java.util.ArrayList;

import br.com.weblen.app.R;
import br.com.weblen.app.data.MoviesAdapter;
import br.com.weblen.app.models.Movie;
import br.com.weblen.app.models.MovieCollection;
import br.com.weblen.app.utilities.APIClient;
import br.com.weblen.app.utilities.APIInterface;
import br.com.weblen.app.utilities.ApiTypes;
import br.com.weblen.app.utilities.EndlessRecyclerViewScrollListener;
import br.com.weblen.app.utilities.Helper;
import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static br.com.weblen.app.BuildConfig.VALUE_API_KEY;
import static br.com.weblen.app.utilities.ApiTypes.BY_POPULAR;
import static br.com.weblen.app.utilities.ApiTypes.BY_STARRED;
import static br.com.weblen.app.utilities.ApiTypes.BY_TOP_RATED;

public class MainActivity extends AppCompatActivity implements MoviesAdapter.MoviesAdapterClickListener {

    @BindView(R.id.rv_movies)
    RecyclerView mRecyclerView;
    @BindView(R.id.tv_error_message_display)
    TextView     mErrorMessage;
    @BindView(R.id.pb_loading_indicator)
    ProgressBar  mProgressBar;
    private        MoviesAdapter moviesAdapter;
    private static ApiTypes      currentApiType;
    private        Integer       currentApiPage = 1;
    private ArrayList<Movie> mMovies = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        if (Helper.isInternetAvailable(this) == false)
            showErrorInternetConnection();

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

        mRecyclerView.setHasFixedSize(true);

        moviesAdapter = new MoviesAdapter(this);
        mRecyclerView.setAdapter(moviesAdapter);

        showProgressBar();

        fetchMovies(BY_POPULAR);
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

            default:
                showErrorMessage();
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
                showErrorMessage();
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int selectedMenuId = item.getItemId();

        if (Helper.isInternetAvailable(this) == false) {
            showErrorInternetConnection();
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
                default:
                    break;
            }

        } catch (Exception e) {
            showErrorMessage();
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

    private void processFinish(Object output) {

        MovieCollection movies = (MovieCollection) output;

        if (movies != null && movies.getObjMovies().size() > 0) {
            showRecyclerView();
            if (mMovies.isEmpty()) {
                mMovies = movies.getObjMovies();
                moviesAdapter.setMoviesData(mMovies);
                moviesAdapter.notifyDataSetChanged();
            } else {
                int positionStart = moviesAdapter.getItemCount();
                mMovies.addAll(movies.getObjMovies());
                int itemCount = mMovies.size() - 1;
                moviesAdapter.notifyItemRangeInserted(positionStart, itemCount);
            }
        } else {
            showErrorMessage();
        }
    }

    private void showRecyclerView() {
        mProgressBar.setVisibility(View.INVISIBLE);
        mErrorMessage.setVisibility(View.INVISIBLE);
        mRecyclerView.setVisibility(View.VISIBLE);
    }

    private void showProgressBar() {
        mProgressBar.setVisibility(View.VISIBLE);
        mErrorMessage.setVisibility(View.INVISIBLE);
        mRecyclerView.setVisibility(View.INVISIBLE);
    }

    private void showErrorMessage() {
        mProgressBar.setVisibility(View.INVISIBLE);
        mRecyclerView.setVisibility(View.INVISIBLE);
        mErrorMessage.setVisibility(View.VISIBLE);
    }

    private void showErrorInternetConnection() {
        mProgressBar.setVisibility(View.INVISIBLE);
        mRecyclerView.setVisibility(View.INVISIBLE);
        mErrorMessage.setVisibility(View.VISIBLE);
        mErrorMessage.setText(R.string.error_no_internet_connection);
    }
}